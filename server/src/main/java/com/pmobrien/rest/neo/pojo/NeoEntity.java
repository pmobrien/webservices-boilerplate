package com.pmobrien.rest.neo.pojo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
public class NeoEntity {

  @Id
  @GeneratedValue
  private Long id;

  public Long getId() {
    return id;
  }
  
  public String toJson() {
    try {
      return new ObjectMapper()
          .configure(SerializationFeature.INDENT_OUTPUT, true)
          .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
          .writeValueAsString(this);
    } catch(JsonProcessingException ex) {
      throw new RuntimeException(ex);
    }
  }
}
