package io.github.rkraneis.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import javax.inject.Inject;
import javax.inject.Named;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named("JsonLambda")
public class JsonLambda implements RequestHandler<Person, JsonNode> {

    private static final Logger log = LoggerFactory.getLogger(JsonLambda.class);

    private final JsonService jsonProvider;
    private final Echo echo;

    @Inject
    public JsonLambda(JsonService jsonProvider, @RestClient Echo echo) {
        this.jsonProvider = jsonProvider;
        this.echo = echo;
    }

    @Override
    @RequestAspect
    public JsonNode handleRequest(Person input, Context context) {
        final var text = echo.get();
        log.debug(text);
        return jsonProvider.json(input);
    }
}
