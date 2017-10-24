package com.pmobrien.rest.services.impl;

import com.cleo.graph.pojo.GraphEntityFactory;
import com.cleo.graph.pojo.Resource;
import com.cleo.graph.pojo.api.Collection;
import com.cleo.graph.pojo.api.requests.ResourcePatch;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.pmobrien.rest.neo.NeoConnector;
import com.pmobrien.rest.services.IResourcesService;
import java.util.UUID;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class ResourcesService implements IResourcesService {

  @Override
  public Collection<Resource> getAll(String parentId) {
    if(parentId == null) {
      return Collection.of(
          NeoConnector.getInstance().returningSessionOperation(session -> session.loadAll(Resource.class))
      );
    } else {
      return Collection.of(
          NeoConnector.getInstance().returningSessionOperation(session ->
              Lists.newArrayList(
                  session.query(
                      Resource.class,
                      Queries.GET_CHILDREN_BY_PARENT_ID,
                      ImmutableMap.<String, String>builder()
                          .put("parentId", UUID.fromString(parentId).toString())
                          .build()
                  )
              )
          )
      );
    }
  }

  @Override
  public Resource get(String uuid) {
    return NeoConnector.getInstance().returningSessionOperation(session -> {
      Resource resource = session.load(Resource.class, UUID.fromString(uuid));

      if(resource == null) {
        throw new WebApplicationException(Response.Status.NOT_FOUND);
      }

      return resource;
    });
  }

  @Override
  public Resource post(Resource resource) {
    Resource save = GraphEntityFactory.create(Resource.class)
        .setName(resource.getName());
    
    return NeoConnector.getInstance().returningSessionOperation(session -> {
      session.save(save);

      return session.load(Resource.class, save.getUuid());
    });
  }

  @Override
  public void delete(String uuid) {
    NeoConnector.getInstance().sessionOperation(session ->
        session.delete(session.load(Resource.class, UUID.fromString(uuid)))
    );
  }

  @Override
  public Resource patch(String uuid, ResourcePatch patch) {
    return NeoConnector.getInstance().returningSessionOperation(session -> {
      Resource resource = session.load(Resource.class, UUID.fromString(uuid));
      
      session.save(
          resource.setName(patch.getName())
      );
      
      return session.load(Resource.class, UUID.fromString(uuid));
    });
  }
  
  private static class Queries {

    private static final String GET_CHILDREN_BY_PARENT_ID =
        new StringBuilder()
            .append("MATCH (child:Resource)<-[PARENT_OF]-(Resource { uuid: {parentId} })").append(System.lineSeparator())
            .append("RETURN child")
            .toString();
  }
}
