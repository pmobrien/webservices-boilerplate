package com.pmobrien.rest.services.impl;

import com.cleo.graph.pojo.OmniUser;
import com.cleo.graph.pojo.Share;
import com.pmobrien.rest.neo.NeoConnector;
import com.pmobrien.rest.services.ISharesService;
import java.util.Collection;

public class SharesService implements ISharesService {

  @Override
  public Collection<Share> getAll() {
    return NeoConnector.getInstance().returningSessionOperation(session -> {
      return session.loadAll(Share.class);
    });
  }

  @Override
  public Share get(String uuid) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public Share post(OmniUser user) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void delete(String uuid) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
}
