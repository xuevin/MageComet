package uk.ac.ebi.fgpt.magecomet.server;

import java.util.ArrayList;
import java.util.List;

import uk.ac.ebi.fgpt.magecomet.client.searchservice.SearchService;
import uk.ac.ebi.fgpt.magecomet.client.searchservice.SearchSuggestion;

import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Request;
import com.google.gwt.user.client.ui.SuggestOracle.Response;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;


public class SearchServiceImpl extends RemoteServiceServlet implements SearchService{

	private SearchSuggestion[] sortedListOfTerms;
	
	public Response getEFO(Request req) {
		//Instantiate once
		if(sortedListOfTerms==null){
			sortedListOfTerms = (SearchSuggestion[]) getServletContext().getAttribute("ontoCatDict");
		}
		SuggestOracle.Response response=new SuggestOracle.Response();
		
		String query = req.getQuery();
		
		List<SearchSuggestion> listOfEFOTerms = new ArrayList<SearchSuggestion>();
		
		
		for(int i =0;i<sortedListOfTerms.length;i++){
			if(sortedListOfTerms[i].getTerm().toLowerCase().startsWith(query.toLowerCase())){
				//Add only as many queries as you can show
				for(int j = 0 ;j<req.getLimit();j++){
					if(i+j<sortedListOfTerms.length){
						listOfEFOTerms.add(sortedListOfTerms[i+j]);	
					}
				}
				response.setSuggestions(listOfEFOTerms);
				return response;
			}
		}

		
//		if(req.getQuery().length()>3){
//			List<SearchSuggestion> listOfEFOTerms = new ArrayList<SearchSuggestion>();
//			try {
//				List<OntologyTerm> ontologyTerms = ontoCat.searchAll(req.getQuery(), SearchOptions.INCLUDE_PROPERTIES);
//				for(int i = 0 ;i<req.getLimit();i++){
//					listOfEFOTerms.add(new SearchSuggestion(ontologyTerms.get(i).getLabel()));	
//				}
//				response.setSuggestions(listOfEFOTerms);
//			} catch (OntologyServiceException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//		}

		return response;
	}

}
