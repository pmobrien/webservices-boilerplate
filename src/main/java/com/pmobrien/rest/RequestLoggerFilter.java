package com.pmobrien.rest;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;

public class RequestLoggerFilter implements ContainerRequestFilter {

  @Context
  private HttpServletRequest request;
  
  @Override
  public void filter(ContainerRequestContext context) throws IOException {    
    System.out.println("Incoming Request:");
    System.out.println(String.format("  %s %s", request.getMethod(), request.getRequestURL()));
  }
}
