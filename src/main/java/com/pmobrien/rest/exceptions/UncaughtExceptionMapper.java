package com.pmobrien.rest.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class UncaughtExceptionMapper implements ExceptionMapper<Throwable> {

  /**
   * If it's a WebApplicationException (or any child class of), convert it to a JSON error message and retain the
   * original error code. Otherwise, return a 500.
   * 
   * @param t The uncaught exception.
   * 
   * @return The uncaught WebApplicationException as JSON, or a 500.
   */
  @Override
  public Response toResponse(Throwable t) {
    t.printStackTrace(System.out);
    
    try {
      throw t;
    } catch(WebApplicationException ex) {
      return Response.status(((WebApplicationException)ex).getResponse().getStatus())
          .entity(new ErrorMessage(((WebApplicationException)ex).getMessage()))
          .type(MediaType.APPLICATION_JSON)
          .build();
    } catch(Throwable ex) {
      return Response.serverError()
          .entity(new ErrorMessage("An unknown error occurred."))
          .type(MediaType.APPLICATION_JSON)
          .build();
    }
  }
  
  private static class ErrorMessage {
    
    private String message;

    public ErrorMessage(String message) {
      this.message = message;
    }

    public String getMessage() {
      return message;
    }

    public void setMessage(String message) {
      this.message = message;
    }
  }
}
