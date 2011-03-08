package uk.ac.ebi.fgpt.magecomet.server;


import java.net.URISyntaxException;
import java.util.List;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import uk.ac.ebi.fgpt.magecomet.client.EFOService;
import uk.ac.ebi.ontocat.OntologyServiceException;
import uk.ac.ebi.ontocat.OntologyTerm;
import uk.ac.ebi.ontocat.OntologyService.SearchOptions;
import uk.ac.ebi.ontocat.file.FileOntologyService;


public class EFOServiceImpl extends RemoteServiceServlet implements EFOService{
	private FileOntologyService ontoService;

	public EFOServiceImpl(){
		intialize();
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
		try {
			ontoService = new FileOntologyService(this.getClass().getClassLoader().getResource("EFO_inferred_v142.owl").toURI());
		} catch (URISyntaxException e) {
			System.err.println("DEFAULT EFO_inferred_v142.owl IS NOT FOUND");
			e.printStackTrace();
		}		
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
