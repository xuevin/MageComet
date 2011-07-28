package uk.ac.ebi.fgpt.magecomet.server;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.junit.Test;

import uk.ac.ebi.ontocat.OntologyService;
import uk.ac.ebi.ontocat.OntologyServiceException;
import uk.ac.ebi.ontocat.OntologyTerm;
import uk.ac.ebi.ontocat.OntologyService.SearchOptions;
import uk.ac.ebi.ontocat.file.FileOntologyService;

public class SearchServiceImplTest {
	private FileOntologyService ontoService;
	
	@Test
	public void testcachedService() throws OntologyServiceException, URISyntaxException{
		  OntologyService osBP;
		  OntologyService os;

			System.out.println("Downloading Ontology");
			osBP = new FileOntologyService(new URI("http://www.ebi.ac.uk/efo/efo.owl"));
			os=osBP;
//			System.out.println("Creating cached version");
//			os = CachedServiceDecorator.getService(osBP);
			List<OntologyTerm> ontoTerms = osBP.searchAll("cancer", SearchOptions.INCLUDE_PROPERTIES);
			System.out.println(ontoTerms.get(0).getLabel());

		  
}

}
