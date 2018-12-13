package com.pmobrien.rest.neo.accessors;

import com.pmobrien.rest.neo.Sessions;
import com.pmobrien.rest.neo.pojo.HelloWorld;

public class HelloWorldAccessor {

  public HelloWorldAccessor() {}
  
  public HelloWorld getHelloWorld() {
    return Sessions.returningSessionOperation(
        session -> session.loadAll(HelloWorld.class).toArray(new HelloWorld[] {})[0]
    );
  }
}
