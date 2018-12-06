package com.pmobrien.rest.neo;

import java.util.function.Consumer;
import java.util.function.Function;

public class Sessions {

  public static void sessionOperation(Consumer<org.neo4j.ogm.session.Session> function) {
    try(Session session = new Session()) {
      function.accept(session.get());
    }
  }
  
  public static <T> T returningSessionOperation(Function<org.neo4j.ogm.session.Session, T> function) {
    try(Session session = new Session()) {
      return function.apply(session.get());
    }
  }

  private static class Session implements AutoCloseable {

    private final org.neo4j.ogm.session.Session session;
    
    private Session() {
      session = NeoConnector.getInstance().newSession();
    }
    
    private org.neo4j.ogm.session.Session get() {
      return session;
    }
    
    @Override
    public void close() {
      session.clear();
    }
  }
}
