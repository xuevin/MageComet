package uk.ac.ebi.fgpt.magecomet.server.services;


import java.util.List;

import uk.ac.ebi.fgpt.magecomet.client.tagcloud.EFOService;
import uk.ac.ebi.ontocat.OntologyService;
import uk.ac.ebi.ontocat.OntologyServiceException;
import uk.ac.ebi.ontocat.OntologyTerm;
import uk.ac.ebi.ontocat.OntologyService.SearchOptions;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;


public class EFOServiceImpl extends RemoteServiceServlet implements EFOService{
	private OntologyService ontoService;
	public EFOServiceImpl(){
		super();
	}
	public String getEfoAccessionIdByName(String efoName) {
		if(ontoService==null){
			intialize();
		}
		try {
			List<OntologyTerm> ontoTerm = ontoService.searchAll(efoName,SearchOptions.EXACT);
			if(ontoTerm.size()!=1){
				return "Multiple Matches";
			}else{
				return(ontoTerm.get(0).getAccession());
			}
			
		} catch (OntologyServiceException e) {
			e.printStackTrace();
		}
		return "ERROR";
	}
	private void intialize() {
//		try {
//            OntologyService liveEFO = new FileOntologyService(new URI("http://www.ebi.ac.uk/efo/efo.owl"));
////			ontoService = new FileOntologyService(getClass().getClassLoader().getResource("EFO_inferred_v142.owl").toURI());
//			ontoService=liveEFO;
//		} catch (URISyntaxException e) {
//			System.err.println("EFO IS NOT FOUND");
//			e.printStackTrace();
//		}
		ontoService=(OntologyService) getServletContext().getAttribute("ontoService");

	}
	public String getEfoDescriptionByName(String efoName) {
		if(ontoService==null){
			intialize();
		}
		try {
			List<OntologyTerm> ontoTerm = ontoService.searchAll(efoName,SearchOptions.EXACT);
			if(ontoTerm.size()!=1){
				return "Multiple Matches";
			}else{
				String out="";
				for(String def:ontoService.getDefinitions(ontoTerm.get(0))){
					out+=def+"\n";	
				}
				return out.trim();
			}
			
		} catch (OntologyServiceException e) {
			e.printStackTrace();
		}
		return "ERROR";
	}
	
	
}
