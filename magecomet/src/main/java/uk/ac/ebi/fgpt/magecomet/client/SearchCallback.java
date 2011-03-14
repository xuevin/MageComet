package uk.ac.ebi.fgpt.magecomet.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SuggestOracle;

public class SearchCallback implements AsyncCallback<SuggestOracle.Response>{
	private SuggestOracle.Callback callback; 
	private SuggestOracle.Request req; 
	
	public SearchCallback(SuggestOracle.Request req, SuggestOracle.Callback callback){
		this.req = req;
		this.callback = callback;
	}
	public void onFailure(Throwable arg0) {
		System.out.println("Bad");
//		callback.onSuggestionsReady(req,new SuggestOracle.Response());
	}
	public void onSuccess(SuggestOracle.Response response) {
		callback.onSuggestionsReady(req, response);
	}
}
