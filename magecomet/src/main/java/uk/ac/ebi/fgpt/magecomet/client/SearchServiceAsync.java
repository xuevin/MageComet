package uk.ac.ebi.fgpt.magecomet.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SuggestOracle;

public interface SearchServiceAsync {
	void getEFO(SuggestOracle.Request req,AsyncCallback<SuggestOracle.Response> callback);

}
