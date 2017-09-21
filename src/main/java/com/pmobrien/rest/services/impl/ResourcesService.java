package com.pmobrien.rest.services.impl;

import com.cleo.graph.pojo.GraphEntityFactory;
import com.cleo.graph.pojo.Resource;
import com.cleo.graph.pojo.api.requests.ResourcePatch;
import com.pmobrien.rest.neo.NeoConnector;
import com.pmobrien.rest.services.IResourcesService;
import java.util.Collection;
import java.util.UUID;

public class ResourcesService implements IResourcesService {

  @Override
  public Collection<Resource> getAll() {
    return NeoConnector.getInstance().returningSessionOperation(session -> {
      return session.loadAll(Resource.class);
    });
  }

  @Override
  public Resource get(String uuid) {
    return NeoConnector.getInstance().returningSessionOperation(session -> {
      return session.load(Resource.class, UUID.fromString(uuid));
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
