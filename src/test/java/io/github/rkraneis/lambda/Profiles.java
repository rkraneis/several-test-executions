package io.github.rkraneis.lambda;

import io.quarkus.test.junit.QuarkusTestProfile;
import java.util.Map;
import java.util.Set;

public class Profiles {

    public static class Greeting implements QuarkusTestProfile {

        @Override
        public Map<String, String> getConfigOverrides() {
            return Map.of("quarkus.lambda.handler", "GreetingLambda");
        }

        @Override
        public Set<String> tags() {
            return Set.of("GreetingLambda");
        }

    }

    public static class Json implements QuarkusTestProfile {

        @Override
        public Map<String, String> getConfigOverrides() {
            return Map.of("quarkus.lambda.handler", "JsonLambda");
        }

        @Override
        public Set<String> tags() {
            return Set.of("JsonLambda");
        }

    }

}
