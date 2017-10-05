package com.pmobrien.rest.services.impl;

import com.cleo.graph.pojo.GraphEntityFactory;
import com.cleo.graph.pojo.OmniUser;
import com.cleo.graph.pojo.Resource;
import com.cleo.graph.pojo.Share;
import com.cleo.graph.pojo.api.Collection;
import com.cleo.graph.pojo.api.requests.AddShareRequest;
import com.cleo.graph.pojo.api.responses.AddShareResponse;
import com.google.common.collect.Lists;
import com.pmobrien.rest.neo.NeoConnector;
import com.pmobrien.rest.services.ISharesService;
import java.util.Date;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.cypher.ComparisonOperator;
import org.neo4j.ogm.cypher.Filter;

public class SharesService implements ISharesService {

  @Context
  private HttpServletRequest request;
  
  @Override
  public Collection<Share> getAll() {
    return Collection.<Share>of(
        NeoConnector.getInstance().returningSessionOperation(session -> {
          return session.loadAll(Share.class);
        })
    );
  }

  @Override
  public Share get(String uuid) {
    return NeoConnector.getInstance().returningSessionOperation(session -> {
      return session.load(Share.class, UUID.fromString(uuid));
    });
  }

  @Override
  public AddShareResponse post(AddShareRequest addShareRequest) {
    NeoConnector.getInstance().sessionOperation(session -> {
      addShareRequest.getUsers().stream()
          .map(id -> session.load(OmniUser.class, id))
          .forEach((user) -> {
            Resource resource = session.load(Resource.class, addShareRequest.getResourceId());
            OmniUser supervisor = session.load(OmniUser.class, UUID.fromString(request.getParameter("user")));
            
            session.save(
                GraphEntityFactory.create(Share.class)
                    .setUser(user)
                    .setCreated(new Date())
                    .setResource(resource)
                    .setSupervisedBy(supervisor)
            );
      });
    });
    
    // TODO: should return only shares that were created on this request, not all shares for the Resource.
    return NeoConnector.getInstance().returningSessionOperation(session -> {
      Filter filter = new Filter("uuid", ComparisonOperator.EQUALS, addShareRequest.getResourceId());
      filter.setNestedPropertyName("resource");
      filter.setNestedPropertyType(Resource.class);
      filter.setRelationshipType("PROXY_FOR");
      filter.setRelationshipDirection(Relationship.OUTGOING);
      
      AddShareResponse response = new AddShareResponse();
      response.setShares(Lists.newArrayList(session.loadAll(Share.class, filter)));
      
      return response;
    });
  }

  @Override
  public void delete(String uuid) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
}
