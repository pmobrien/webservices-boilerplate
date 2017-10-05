package com.pmobrien.rest.services;

import com.cleo.graph.pojo.Resource;
import com.cleo.graph.pojo.api.Collection;
import com.cleo.graph.pojo.api.requests.ResourcePatch;
import com.pmobrien.rest.util.PATCH;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/resources")
public interface IResourcesService {

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Collection<Resource> getAll();

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Resource get(@PathParam("id") String uuid);
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Resource post(Resource resource);
  
  @DELETE
  @Path("/{id}")
  public void delete(@PathParam("id") String uuid);
  
  @PATCH
  @Path("/{id}")
  public Resource patch(@PathParam("id") String uuid, ResourcePatch patch);
}
