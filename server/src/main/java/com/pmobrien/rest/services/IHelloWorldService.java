package com.pmobrien.rest.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/hello-world")
public interface IHelloWorldService {

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public Response helloWorld();
}
