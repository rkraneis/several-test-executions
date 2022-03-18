package io.github.rkraneis.lambda;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;


@Path("/")
@RegisterRestClient(baseUri = "https://postman-echo.com")
public interface Echo {

  @GET
  @Path("get")
  String get();

  @POST
  @Path("post")
  String post(String text);
}
