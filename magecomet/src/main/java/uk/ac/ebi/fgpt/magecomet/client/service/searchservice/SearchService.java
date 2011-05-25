package uk.ac.ebi.fgpt.magecomet.client.service.searchservice;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.client.ui.SuggestOracle;

@RemoteServiceRelativePath("SearchService")
public interface SearchService extends RemoteService{
	SuggestOracle.Response getEFO(SuggestOracle.Request req);
//	public static class Util {
//		public static SearchServiceAsync getInstance(){
//			SearchServiceAsync searchService= (SearchServiceAsync)GWT.create(SearchService.class);
//		((ServiceDefTarget)searchService).setServiceEntryPoint(GWT.getModuleBaseURL()+"SearchService");
//		return searchService;
//		}
//	}
}



