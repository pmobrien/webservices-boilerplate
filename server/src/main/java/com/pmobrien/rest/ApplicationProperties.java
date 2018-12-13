package com.pmobrien.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.io.Resources;
import java.io.File;
import java.io.IOException;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplicationProperties {
  
  private static final String SYSTEM_PROPERTY = "properties";
  private static final String DEFAULT_PATH = "com/pmobrien/rest/conf/properties.json";

  private Configuration configuration;
  
  public ApplicationProperties() {}

  public Configuration getConfiguration() {
    return configuration;
  }

  public void setConfiguration(Configuration configuration) {
    this.configuration = configuration;
  }
  
  public static class Configuration {
    
    private Http http;
    private Https https;
    private Neo neo;
    
    public Configuration() {}

    public Http getHttp() {
      return http;
    }

    public void setHttp(Http http) {
      this.http = http;
    }

    public Https getHttps() {
      return https;
    }

    public void setHttps(Https https) {
      this.https = https;
    }

    public Neo getNeo() {
      return neo;
    }

    public void setNeo(Neo neo) {
      this.neo = neo;
    }
    
    public static class Http {
      
      private Integer port;
      
      public Http() {}

      public Integer getPort() {
        return port;
      }

      public void setPort(Integer port) {
        this.port = port;
      }
    }
    
    public static class Https {
      
      private Boolean enabled;
      private Integer port;
      private String keyStorePath;
      private String keyStorePassword;

      public Boolean isEnabled() {
        return enabled;
      }

      public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
      }

      public Integer getPort() {
        return port;
      }

      public void setPort(Integer port) {
        this.port = port;
      }

      public String getKeyStorePath() {
        return keyStorePath;
      }

      public void setKeyStorePath(String keyStorePath) {
        this.keyStorePath = keyStorePath;
      }

      public String getKeyStorePassword() {
        return keyStorePassword;
      }

      public void setKeyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
      }
    }
    
    public static class Neo {
      
      private String storage;
      private Boolean boltEnabled;
      private String boltUri;
      
      public Neo() {}

      public String getStorage() {
        return storage;
      }

      public void setStorage(String storage) {
        this.storage = storage;
      }

      public Boolean isBoltEnabled() {
        return boltEnabled;
      }

      public void setBoltEnabled(Boolean boltEnabled) {
        this.boltEnabled = boltEnabled;
      }

      public String getBoltUri() {
        return boltUri;
      }

      public void setBoltUri(String boltUri) {
        this.boltUri = boltUri;
      }
    }
  }
  
  public static ApplicationProperties load() {
    try {
      if(Strings.isNullOrEmpty(System.getProperty(SYSTEM_PROPERTY))) {
        return new ObjectMapper().readValue(
            Resources.toString(Resources.getResource(DEFAULT_PATH), Charsets.UTF_8),
            ApplicationProperties.class
        );
      } else {
        File file = new File(System.getProperty(SYSTEM_PROPERTY));

        if(!file.exists()) {
          throw new RuntimeException(String.format("Properties file does not exist at '%s'.", file.getPath()));
        }

        return new ObjectMapper().readValue(file, ApplicationProperties.class);
      }
    } catch(IOException ex) {
      throw new RuntimeException(ex);
    }
  }
}
