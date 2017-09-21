package com.pmobrien.rest.services.impl;

import com.cleo.graph.pojo.GraphEntityFactory;
import com.cleo.graph.pojo.Resource;
import com.pmobrien.rest.neo.NeoConnector;
import com.pmobrien.rest.services.IResourcesService;
import java.util.Collection;

public class ResourcesService implements IResourcesService {

  @Override
  public Collection<Resource> getAll() {
    return NeoConnector.getInstance().returningSessionOperation(session -> {
      return session.loadAll(Resource.class);
    });
  }

  @Override
  public Resource get(String uuid) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
}
