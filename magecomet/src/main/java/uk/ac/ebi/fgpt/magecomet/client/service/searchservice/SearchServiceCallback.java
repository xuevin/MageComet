package uk.ac.ebi.fgpt.magecomet.client.service.searchservice;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SuggestOracle;

/**
 * This class rewraps the original callback with the server callback
 * 
 * @author Vincent Xue
 * 
 */
public class SearchServiceCallback implements AsyncCallback<SuggestOracle.Response> {
  private SuggestOracle.Callback callback;
  private SuggestOracle.Request req;
  
  public SearchServiceCallback(SuggestOracle.Request req, SuggestOracle.Callback callback) {
    this.req = req;
    this.callback = callback;
  }
  
  public void onFailure(Throwable arg0) {
    System.out.println("Bad");
  }
  
  public void onSuccess(SuggestOracle.Response response) {
    callback.onSuggestionsReady(req, response);
  }
}
