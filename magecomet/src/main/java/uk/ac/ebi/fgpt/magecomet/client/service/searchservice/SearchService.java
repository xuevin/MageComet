package uk.ac.ebi.fgpt.magecomet.client.service.searchservice;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.client.ui.SuggestOracle;

/**
 * Given SuggestOracle.Request, return back SuggestOracle.Response
 * 
 * @author Vincent Xue
 * 
 */
@RemoteServiceRelativePath("SearchService")
public interface SearchService extends RemoteService {
  /**
   * @param req
   *          The original SuggestOracle.Request
   * @return The new SuggestOracle.Response
   */
  SuggestOracle.Response getEFO(SuggestOracle.Request req);
}
