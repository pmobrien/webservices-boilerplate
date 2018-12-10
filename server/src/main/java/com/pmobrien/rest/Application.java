package com.pmobrien.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.io.Resources;
import com.pmobrien.rest.exceptions.UncaughtExceptionMapper;
import com.pmobrien.rest.neo.Sessions;
import com.pmobrien.rest.services.impl.HelloWorldService;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import org.eclipse.jetty.server.ForwardedRequestCustomizer;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

public class Application {

  private static final String PROPERTIES_FILE = "properties";
  private static final String WEBAPP_RESOURCE_PATH = "/com/pmobrien/rest/webapp";
  private static final String INDEX_HTML_PATH = String.format("%s/index.html", WEBAPP_RESOURCE_PATH);
  private static final String DEFAULT_PROPERTIES_PATH = "com/pmobrien/rest/conf/properties.json";
  
  private static ApplicationProperties properties;
  
  public static void main(String[] args) throws Exception {
    try {
      new Application().run(new Server());
    } catch(Exception ex) {
      ex.printStackTrace(System.out);
    } catch(Throwable t) {
      t.printStackTrace(System.out);
    }
  }
  
  private Application() throws Exception {
    properties = readApplicationProperties();
    
    Sessions.sessionOperation(session -> {});
  }
  
  public static ApplicationProperties getProperties() {
    return properties;
  }
  
  private ApplicationProperties readApplicationProperties() throws Exception {
    if(Strings.isNullOrEmpty(System.getProperty(PROPERTIES_FILE))) {
      return new ObjectMapper().readValue(
          Resources.toString(Resources.getResource(DEFAULT_PROPERTIES_PATH), Charsets.UTF_8),
          ApplicationProperties.class
      );
    } else {
      File file = new File(System.getProperty(PROPERTIES_FILE));
      
      if(!file.exists()) {
        throw new RuntimeException(String.format("Properties file does not exist at '%s'.", file.getPath()));
      }
      
      return new ObjectMapper().readValue(file, ApplicationProperties.class);
    }
  }
  
  private void run(Server server) {
    try {      
      server.setHandler(configureHandlers());
      server.addConnector(configureConnector(server));
      
      server.start();
      server.join();
    } catch(Exception ex) {
      throw new RuntimeException(ex);
    } finally {
      server.destroy();
    }
  }
  
  private HandlerList configureHandlers() throws MalformedURLException, URISyntaxException {
    HandlerList handlers = new HandlerList();
    handlers.setHandlers(
        new Handler[] {
          configureApiHandler(),
          configureStaticHandler()
        }
    );
    
    return handlers;
  }
  
  private ServletContextHandler configureApiHandler() {
    ServletContextHandler handler = new ServletContextHandler();
    handler.setContextPath("/api");
    
    handler.addServlet(
        new ServletHolder(
            new ServletContainer(
                new ResourceConfig()
                    .register(HelloWorldService.class)
                    .register(UncaughtExceptionMapper.class)
            )
        ),
        "/*"
    );
    
    return handler;
  }
  
  private ServletContextHandler configureStaticHandler() throws MalformedURLException, URISyntaxException {
    ServletContextHandler handler = new ServletContextHandler();
    handler.setContextPath("/");
    
    handler.setBaseResource(
        Resource.newResource(
            URI.create(
                this.getClass().getResource(INDEX_HTML_PATH)
                    .toURI()
                    .toASCIIString()
                    .replaceFirst("/index.html$", "/")
            )
        )
    );
    
    handler.setWelcomeFiles(new String[] { "index.html" });
    handler.addServlet(DefaultServlet.class, "/");
    
    return handler;
  }
  
  private ServerConnector configureConnector(Server server) {
    HttpConfiguration config = new HttpConfiguration();
    config.addCustomizer(new SecureRequestCustomizer());
    config.addCustomizer(new ForwardedRequestCustomizer());
    
    HttpConnectionFactory httpConnectionFactory = new HttpConnectionFactory(config);
    ServerConnector httpConnector = new ServerConnector(server, httpConnectionFactory);
    httpConnector.setPort(Application.getProperties().getConfiguration().getHttp().getPort());
    server.addConnector(httpConnector);
    
    if(Application.properties.getConfiguration().getHttps().isEnabled()) {
      // TODO: check https properties

      SslContextFactory sslContextFactory = new SslContextFactory();
      sslContextFactory.setKeyStoreType("PKCS12");
      sslContextFactory.setKeyStorePath(Application.properties.getConfiguration().getHttps().getKeyStorePath());
      sslContextFactory.setKeyStorePassword(Application.properties.getConfiguration().getHttps().getKeyStorePassword());
      sslContextFactory.setKeyManagerPassword(Application.properties.getConfiguration().getHttps().getKeyStorePassword());
      
      ServerConnector connector = new ServerConnector(server, sslContextFactory, httpConnectionFactory);
      connector.setPort(Application.properties.getConfiguration().getHttps().getPort());
      
      return connector;
    } else {
      return new ServerConnector(server, httpConnectionFactory);
    }
  }
}
