package com.pmobrien.rest.services.impl;

import com.cleo.graph.pojo.GraphEntityFactory;
import com.cleo.graph.pojo.Resource;
import com.cleo.graph.pojo.api.Collection;
import com.cleo.graph.pojo.api.requests.ResourcePatch;
import com.pmobrien.rest.neo.NeoConnector;
import com.pmobrien.rest.services.IResourcesService;
import java.util.UUID;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class ResourcesService implements IResourcesService {

  @Override
  public Collection<Resource> getAll() {
    return Collection.<Resource>of(
        NeoConnector.getInstance().returningSessionOperation(session -> {
          return session.loadAll(Resource.class);
        })
    );
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
}
