package com.pmobrien.rest;

import com.google.common.base.Strings;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

@Provider
public class RequestLoggerFilter implements ContainerRequestFilter {

  @Context
  private HttpServletRequest request;
  
  @Override
  public void filter(ContainerRequestContext context) throws IOException {
    String output = String.format("  %s %s", request.getMethod(), request.getRequestURL());
    if(!Strings.isNullOrEmpty(request.getQueryString())) {
      output += String.format("?%s", request.getQueryString());
    }
    
    System.out.println("Incoming Request:");
    System.out.println(output);
  }
}
