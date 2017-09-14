package com.pmobrien.rest.services;

import com.cleo.graph.pojo.OmniUser;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/omniusers")
public interface IOmniUsersService {

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public OmniUser get(@PathParam("id") String uuid);
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public OmniUser post(OmniUser user);
}
