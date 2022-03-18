package io.github.rkraneis.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import javax.inject.Inject;
import javax.inject.Named;

@Named("GreetingLambda")
public class GreetingLambda implements RequestHandler<Person, String> {

  private final GreetingService greetingProvider;

  @Inject
  public GreetingLambda(GreetingService greetingProvider) {
    this.greetingProvider = greetingProvider;
  }
  
  @Override
  @RequestAspect
  public String handleRequest(Person input, Context context) {
        return greetingProvider.greeting(input);
    }
}
