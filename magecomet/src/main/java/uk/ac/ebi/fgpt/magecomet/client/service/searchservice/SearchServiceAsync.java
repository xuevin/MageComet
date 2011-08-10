package uk.ac.ebi.fgpt.magecomet.client.service.searchservice;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SuggestOracle;

/**
 * Async call back interface.
 * 
 * @author Vincent Xue
 * 
 */
public interface SearchServiceAsync {
  void getEFO(SuggestOracle.Request req, AsyncCallback<SuggestOracle.Response> callback);
  
}
