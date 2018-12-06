package com.pmobrien.rest.neo;

import com.google.common.base.Strings;
import com.google.common.base.Suppliers;
import com.pmobrien.rest.Application;
import java.io.File;
import java.util.function.Supplier;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.kernel.configuration.BoltConnector;
import org.neo4j.kernel.configuration.Connector.ConnectorType;
import org.neo4j.ogm.drivers.embedded.driver.EmbeddedDriver;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;

public class NeoConnector {

  private static final String POJO_PACKAGE = "com.pmobrien.rest.neo.pojo";
  
  private static final NeoConnector INSTANCE = new NeoConnector();
  private static final Supplier<SessionFactory> SESSION_FACTORY = Suppliers.memoize(() -> initializeSessionFactory());
  
  private NeoConnector() {}
  
  public static NeoConnector getInstance() {
    return INSTANCE;
  }
  
  protected Session newSession() {
    return SESSION_FACTORY.get().openSession();
  }
  
  private static SessionFactory initializeSessionFactory() {
    BoltConnector bolt = new BoltConnector("0");
    
    return new SessionFactory(
        new EmbeddedDriver(
            new GraphDatabaseFactory()
                .newEmbeddedDatabaseBuilder(new File(uri()))
                .setConfig(bolt.type, ConnectorType.BOLT.name())
                .setConfig(bolt.enabled, Application.getProperties().getConfiguration().getNeo().isBoltEnabled().toString())
                .setConfig(bolt.listen_address, Application.getProperties().getConfiguration().getNeo().getBoltUri())
                .setConfig(bolt.advertised_address, Application.getProperties().getConfiguration().getNeo().getBoltUri())
                .setConfig(bolt.encryption_level, BoltConnector.EncryptionLevel.DISABLED.name())
                .newGraphDatabase()
        ),
        POJO_PACKAGE
    );
  }

  private static String uri() {
    if(Strings.isNullOrEmpty(Application.getProperties().getConfiguration().getNeo().getStorage())) {
      throw new RuntimeException("configuration.neo.storage property is required!");
    }
    
    return Application.getProperties().getConfiguration().getNeo().getStorage();
  }
}
