package io.github.rkraneis.lambda;

import javax.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped 
public class GreetingService {
  
  private static final Logger log = LoggerFactory.getLogger(GreetingService.class);

  public String greeting(Person person) {
    log.debug("Person: {}", person);
    return "Hello " + person.getName();
  }

}
