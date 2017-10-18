package com.pmobrien.rest.services.impl;

import com.cleo.graph.pojo.GraphEntityFactory;
import com.cleo.graph.pojo.OmniUser;
import com.cleo.graph.pojo.api.Collection;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.pmobrien.rest.neo.NeoConnector;
import com.pmobrien.rest.services.IOmniUsersService;
import java.util.UUID;

public class OmniUsersService implements IOmniUsersService {
  
  @Override
  public Collection<OmniUser> getAll(String resourceId) {
    if(resourceId == null) {
      return Collection.<OmniUser>of(
          NeoConnector.getInstance().returningSessionOperation(session -> {
            return session.loadAll(OmniUser.class);
          })
      );
    } else {
      return Collection.of(
          NeoConnector.getInstance().returningSessionOperation(session ->
              Lists.newArrayList(
                  session.query(
                      OmniUser.class,
                      Queries.GET_OMNIUSERS_BY_RESOURCE_ID,
                      ImmutableMap.<String, String>builder()
                          .put("resourceId", UUID.fromString(resourceId).toString())
                          .build()
                  )
              )
          )
      );
    }
  }

  @Override
  public OmniUser get(String uuid) {
    return NeoConnector.getInstance().returningSessionOperation(session -> {
      return session.load(OmniUser.class, UUID.fromString(uuid));
    });
  }

  @Override
  public OmniUser post(OmniUser user) {
    OmniUser save = GraphEntityFactory.create(OmniUser.class)
        .setUserId(user.getUserId())
        .setVlId(user.getVlId());
    
    NeoConnector.getInstance().sessionOperation(session ->
        session.save(save)
    );
    
    return get(save.getUuid().toString());
  }

  @Override
  public void delete(String uuid) {
    NeoConnector.getInstance().sessionOperation(session ->
        session.delete(session.load(OmniUser.class, UUID.fromString(uuid)))
    );
  }
  
  private static class Queries {
    
    private static final String GET_OMNIUSERS_BY_RESOURCE_ID =
        new StringBuilder()
            .append("MATCH (resource:Resource { uuid: {resourceId} })<-[PROXY_FOR]-(share:Share)<-[HAS_ACCESS_TO]-(user:OmniUser)").append(System.lineSeparator())
            .append("RETURN user").append(System.lineSeparator())
            .append("UNION").append(System.lineSeparator())
            .append("MATCH (user:OmniUser)-[HAS_ACCESS_TO]->(share:Share)-[PROXY_FOR]->(parent:Resource)-[PARENT_OF*]->(resource)").append(System.lineSeparator())
            .append("RETURN user").append(System.lineSeparator())
            .toString();
  }
}
