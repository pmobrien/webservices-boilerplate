package com.pmobrien.rest.services.impl;

import com.pmobrien.rest.neo.accessors.HelloWorldAccessor;
import com.pmobrien.rest.services.IHelloWorldService;
import javax.ws.rs.core.Response;

public class HelloWorldService implements IHelloWorldService {

  @Override
  public Response helloWorld() {
    return Response.ok(new HelloWorldAccessor().getHelloWorld()).build();
  }
}
