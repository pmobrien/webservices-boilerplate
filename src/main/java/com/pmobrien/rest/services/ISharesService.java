package com.pmobrien.rest.services;

import com.cleo.graph.pojo.Share;
import com.cleo.graph.pojo.api.requests.AddShareRequest;
import com.cleo.graph.pojo.api.responses.AddShareResponse;
import java.util.Collection;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/shares")
public interface ISharesService {

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Collection<Share> getAll();

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Share get(@PathParam("id") String uuid);
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public AddShareResponse post(AddShareRequest addShareRequest);
  
  @DELETE
  @Path("/{id}")
  public void delete(@PathParam("id") String uuid);
}
