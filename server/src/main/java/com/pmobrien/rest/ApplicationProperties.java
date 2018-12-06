package com.pmobrien.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplicationProperties {

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
    private Neo neo;
    
    public Configuration() {}

    public Http getHttp() {
      return http;
    }

    public void setHttp(Http http) {
      this.http = http;
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
}
