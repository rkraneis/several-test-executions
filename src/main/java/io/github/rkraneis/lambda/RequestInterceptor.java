package io.github.rkraneis.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import io.quarkus.arc.Arc;
import io.smallrye.common.constraint.Nullable;
import javax.annotation.Priority;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

@RequestAspect
@Priority(0)
@Interceptor
public class RequestInterceptor {

  private static final Logger log = LoggerFactory.getLogger(RequestInterceptor.class);

  @AroundInvoke
  public Object interceptHandleRequest(InvocationContext invocationContext) throws Exception {
    Arc.container().requestContext().activate();

    Object target = invocationContext.getTarget();
    Object[] parameters = invocationContext.getParameters();
    Object input;
    Context lambdaContext;
    boolean isRequestStreamHandler;
    if (target instanceof RequestHandler) {
      input = parameters[0];
      lambdaContext = (Context) parameters[1];
      isRequestStreamHandler = false;
    } else if (target instanceof RequestStreamHandler) {
      input = null;
      lambdaContext = (Context) parameters[2];
      isRequestStreamHandler = true;
    } else {
      log.warn("unexpectedly intercepted '{}'", target);
      return invocationContext.proceed();
    }

    Object result;
    try {
      before(target, lambdaContext);
      result = invocationContext.proceed();
    } catch (Exception e) {
      if (isRequestStreamHandler) {
        log.error("{}: Could not process inputstream", target.getClass(), e);
        throw new RuntimeException(target.getClass() + ": Could not process inputstream", e);
      } else {
        if (input != null) {
          log.error("{}: Could not process input object {}", target.getClass(), input, e);
        } else {
          log.error("{}: Could not process null input object", target.getClass(), e);
        }
        throw new RuntimeException(
            target.getClass() + ": Could not process input object " + input, e);
      }
    } finally {
      after();

      Arc.container().requestContext().deactivate();
    }
    return result;
  }

  private static void before(Object handler, Context context) {
    final String handlerClass = handler.getClass().getName();
    putMdc("HandlerClass", handlerClass);
    putMdc("AwsRequestId", context.getAwsRequestId());
    putMdc("FunctionName", context.getFunctionName());
    putMdc("FunctionVersion", context.getFunctionVersion());
    putMdc("InvokedFunctionArn", context.getInvokedFunctionArn());
    putMdc("LogGroupName", context.getLogGroupName());
    putMdc("LogStreamName", context.getLogStreamName());
  }

  private static void putMdc(final String key, final @Nullable String value) {
    if (value != null) {
      MDC.put(key, value);
    }
  }

  private static void after() {
    MDC.clear();
  }
}
