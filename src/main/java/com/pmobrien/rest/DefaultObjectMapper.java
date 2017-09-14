package com.pmobrien.rest;

import com.cleo.graph.pojo.GraphEntity;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collection;
import javax.ws.rs.ext.ContextResolver;
import org.apache.commons.lang3.StringUtils;

public class DefaultObjectMapper implements ContextResolver<ObjectMapper> {

  @Override
  public ObjectMapper getContext(Class<?> type) {
    return new ObjectMapper()
        .configure(SerializationFeature.INDENT_OUTPUT, true)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .registerModule(new SimpleModule().addSerializer(GraphEntity.class, new GraphEntitySerializer()));
  }
  
  private static class GraphEntitySerializer extends StdSerializer<GraphEntity> {

    public GraphEntitySerializer() {
      this(null);
    }

    public GraphEntitySerializer(Class<GraphEntity> t) {
      super(t);
    }

    /**
     * By default, if entities contain references to each other, the writeValueAsString method from ObjectMapper will
     * attempt to recursively serialize these, eventually throwing a StackOverflow exception. Rather than just ignoring
     * references to other entities, this serialize method will just replace the reference with that entity's UUID.
     * 
     * @param entity
     * @param generator
     * @param provider
     * 
     * @throws IOException
     * @throws JsonProcessingException 
     */
    @Override
    public void serialize(GraphEntity entity, JsonGenerator generator, SerializerProvider provider)
        throws IOException, JsonProcessingException {
      generator.writeStartObject();
      
      for(Method method : entity.getClass().getMethods()) {
        if(!"getClass".equals(method.getName()) && method.getName().contains("get")) {
          try {
            Object value = method.invoke(entity);
            if(value != null) {
              String name = StringUtils.uncapitalize(
                  method.getName().substring(method.getName().indexOf("get") + "get".length())
              );
              
              if(value instanceof GraphEntity) {
                generator.writeObjectFieldStart(name);
                generator.writeStringField("uuid", ((GraphEntity)value).getUuid().toString());
                generator.writeEndObject();
              } else if(value instanceof Collection) {
                generator.writeArrayFieldStart(name);
                
                for(Object item : ((Collection)value)) {
                  if(item instanceof GraphEntity) {
                    generator.writeStartObject();
                    generator.writeStringField("uuid", ((GraphEntity)item).getUuid().toString());
                    generator.writeEndObject();
                  } else {
                    generator.writeObject(item);
                  }
                }
                
                generator.writeEndArray();
              } else {
                if(value instanceof Number) {
                  generator.writeNumberField(name, ((Number)value).longValue());
                } else {
                  generator.writeStringField(name, value.toString());
                }
              }
            }
          } catch(ReflectiveOperationException ex) {
            throw new RuntimeException(ex);
          }
        }
      }
      
      generator.writeEndObject();
    }
  }
}
