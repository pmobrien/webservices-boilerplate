package com.pmobrien.rest.services.impl;

import com.cleo.api.graph.pojo.OmniUser;
import com.pmobrien.rest.neo.NeoConnector;
import com.pmobrien.rest.services.IOmniUsersService;
import java.util.UUID;

public class OmniUsersService implements IOmniUsersService {

  @Override
  public OmniUser get(String uuid) {
    return NeoConnector.getInstance().sessionOperation(session -> session.load(OmniUser.class, UUID.fromString(uuid)));
  }
}
