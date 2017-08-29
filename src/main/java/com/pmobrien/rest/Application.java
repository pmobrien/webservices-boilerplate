package com.pmobrien.rest;

import com.pmobrien.rest.exceptions.UncaughtExceptionMapper;
import com.pmobrien.rest.services.impl.HelloWorldService;
import com.pmobrien.rest.services.impl.OmniUsersService;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

public class Application {

  public static void main(String[] args) throws Exception {
    Server server = new Server(8080);
    
    new ServletContextHandler(server, "/api").addServlet(
        new ServletHolder(
            new ServletContainer(
                new ResourceConfig()
                    .register(OmniUsersService.class)
                    .register(HelloWorldService.class)
                    .register(UncaughtExceptionMapper.class)
            )
        ),
        "/*"
    );

    try {
      server.start();
      server.join();
    } catch(Exception ex) {
      ex.printStackTrace(System.out);
    } finally {
      server.destroy();
    }
  }
}
