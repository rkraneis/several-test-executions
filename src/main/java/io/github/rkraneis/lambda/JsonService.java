package io.github.rkraneis.lambda;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class JsonService {
  
  private static final Logger log = LoggerFactory.getLogger(JsonService.class);
  private final ObjectMapper objectMapper;

  @Inject
  public JsonService(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public JsonNode json(Person person) {
    log.debug("Person: {}", person);
    return objectMapper.valueToTree(person);
  }
}
