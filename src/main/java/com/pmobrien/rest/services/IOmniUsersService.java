package com.pmobrien.rest.services;

import com.cleo.graph.pojo.OmniUser;
import com.cleo.graph.pojo.api.Collection;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/omniusers")
public interface IOmniUsersService {
  
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Collection<OmniUser> getAll(@QueryParam("resourceId") String resourceId);

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public OmniUser get(@PathParam("id") String uuid);
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public OmniUser post(OmniUser user);
  
  @DELETE
  @Path("/{id}")
  public void delete(@PathParam("id") String uuid);
}
