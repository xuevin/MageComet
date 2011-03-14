package uk.ac.ebi.fgpt.magecomet.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.SuggestOracle;


public class SearchOracle extends SuggestOracle{
	private SearchServiceAsync searchService = GWT.create(SearchService.class);
	@Override
	public void requestSuggestions(Request req, Callback callback) {
		//Adds a layer of callbacks (this makes it call the server)
//		SearchService.Util.getInstance().getEFO(req, new SearchCallback(req, callback));
		searchService.getEFO(req, new SearchCallback(req, callback));
	}
    public boolean isDisplayStringHTML() { return true; }

}

