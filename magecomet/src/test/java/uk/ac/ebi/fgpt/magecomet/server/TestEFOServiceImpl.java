package uk.ac.ebi.fgpt.magecomet.server;

import static org.junit.Assert.*;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import uk.ac.ebi.ontocat.OntologyServiceException;
import uk.ac.ebi.ontocat.OntologyTerm;
import uk.ac.ebi.ontocat.OntologyService.SearchOptions;
import uk.ac.ebi.ontocat.file.FileOntologyService;


public class TestEFOServiceImpl {
	private FileOntologyService ontoService;

	@Test
	public void testNotNull(){
		EFOServiceImpl test = new EFOServiceImpl();
		assertNotNull(test);
	}
	@Test
	public void testGetEfoAccessionIdByName(){
		EFOServiceImpl impl = new EFOServiceImpl();
		assertEquals("EFO_0000860",impl.getEfoAccessionIdByName("thymus"));
	}
	@Test
	public void testgetEfoDescriptionByName() {
		EFOServiceImpl impl = new EFOServiceImpl();
		String match ="An experimental factor in Array Express which are essentially the variable aspects of an experiment design which can be used to describe an experiment, or set of experiments, in an increasingly detailed manner.";
		assertEquals(match,impl.getEfoDescriptionByName("experimental factor"));
	}
	private void intialize() {
		try {
			ontoService = new FileOntologyService(this.getClass().getClassLoader().getResource("EFO_inferred_v142.owl").toURI());
		} catch (URISyntaxException e) {
			System.err.println("DEFAULT EFO_inferred_v142.owl IS NOT FOUND");
			e.printStackTrace();
		}		
	}
	@Test
	public void testGetEfoAccessionIdByName_Development() {
		String efoName = "blood";
		
		if(ontoService==null){
			intialize();
		}
		try {
			List<OntologyTerm> ontoTerm = ontoService.searchAll(efoName,SearchOptions.EXACT);
			if(ontoTerm.size()!=1){
				
			}else{
				System.out.println(ontoTerm.get(0).getAccession());
			}
			
		} catch (OntologyServiceException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testgetEfoDescriptionByName_Development() {
		String efoName = "heart";

		if(ontoService==null){
			intialize();
		}
		try {
			List<OntologyTerm> ontoTerm = ontoService.searchAll(efoName,SearchOptions.EXACT);
			if(ontoTerm.size()!=1){
				
			}else{
				for(String def:ontoService.getDefinitions(ontoTerm.get(0))){
					System.out.println(def);	
				}
				
				
			}
			
		} catch (OntologyServiceException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void getAllTerms_Development(){
		if(ontoService==null){
			intialize();
		}
		try {
			
			Set<OntologyTerm> allTerms =  ontoService.getAllTerms(null);
			for(OntologyTerm ontoTerm :allTerms){
				System.out.println(ontoTerm.getLabel());
			}
			System.out.println(allTerms.size());
		} catch (OntologyServiceException e) {
			e.printStackTrace();
		}
	}

}
