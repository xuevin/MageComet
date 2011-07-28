package uk.ac.ebi.fgpt.magecomet.server.services;

import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import monq.ie.Term2Re;
import monq.jfa.CompileDfaException;
import monq.jfa.Dfa;
import monq.jfa.DfaRun;
import monq.jfa.Nfa;
import monq.jfa.ReSyntaxException;
import monq.jfa.actions.Copy;
import uk.ac.ebi.fgpt.magecomet.client.service.searchservice.SearchSuggestion;
import uk.ac.ebi.fgpt.magecomet.server.WhatIzItException;
import uk.ac.ebi.ontocat.OntologyService;
import uk.ac.ebi.ontocat.OntologyServiceException;
import uk.ac.ebi.ontocat.OntologyTerm;
import uk.ac.ebi.ontocat.file.FileOntologyService;

public class InitilizationServletContextListener implements ServletContextListener {
  
  private ServletContext context = null;
  
  @Override
  public void contextDestroyed(ServletContextEvent arg0) {

  }
  
  @Override
  public void contextInitialized(ServletContextEvent arg0) {
    context = arg0.getServletContext();
    try {
      System.out.println("Downloading EFO Service");
      // During Testing
      OntologyService ontoService;
      // ontoService = new FileOntologyService(new URI("http://www.ebi.ac.uk/efo/efo.owl"));
      // For local install
      ontoService = new FileOntologyService(getClass().getClassLoader().getResource("efo.owl").toURI());
      
      System.out.println("Finished Downloading EFO");
      context.setAttribute("ontoService", ontoService);
      context.setAttribute("monqInput", getDfa(getDictionaryTerms(ontoService)));
      context.setAttribute("ontoCatDict", getArrayOfSearchSuggestions(ontoService));
    } catch (URISyntaxException e) {
      System.err.println("EFO IS NOT FOUND");
      e.printStackTrace();
    } catch (OntologyServiceException e) {
      e.printStackTrace();
    } catch (WhatIzItException e) {
      e.printStackTrace();
    }
  }
  
  private Dfa getDfa(String... dictionary) throws WhatIzItException {
    try {
      Nfa nfa = new Nfa(Nfa.NOTHING);
      // If there is a clash, it doesn't matter.
      int i = 0;
      for (String item : dictionary) {
        
        nfa = nfa.or(Term2Re.convert(item), new DoCount(item).setPriority(i));
        
        i++;
      }
      // Use only complete matches
      nfa = nfa.or("[A-Za-z0-9]+", new Copy(Integer.MIN_VALUE));
      Dfa dfa = nfa.compile(DfaRun.UNMATCHED_DROP);
      return dfa;
    } catch (ReSyntaxException e) {
      throw new WhatIzItException(e);
    } catch (CompileDfaException e) {
      throw new WhatIzItException(e);
    }
  }
  
  private String[] getDictionaryTerms(OntologyService ontoCat) throws OntologyServiceException {
    ArrayList<String> dict = new ArrayList<String>();
    for (OntologyTerm ontoTerm : ontoCat.getAllTerms(null)) {
      dict.add(ontoTerm.getLabel());
    }
    String[] dictArray = new String[dict.size()];
    dict.toArray(dictArray);
    return dictArray;
  }
  
  private SearchSuggestion[] getArrayOfSearchSuggestions(OntologyService ontoCat) {
    SearchSuggestion[] sortedListOfTerms;
    try {
      // TODO perhaps this is not the most efficient;
      ArrayList<SearchSuggestion> listOfTerms = new ArrayList<SearchSuggestion>();
      for (OntologyTerm ontoTerm : ontoCat.getAllTerms(null)) {
        listOfTerms.add(new SearchSuggestion(ontoTerm.getLabel(), ontoTerm.getLabel(), ontoTerm
            .getAccession()));
        // Add synonymns
        for (String syn : ontoCat.getSynonyms(ontoTerm)) {
          listOfTerms.add(new SearchSuggestion(syn, ontoTerm.getLabel(), ontoTerm.getAccession()));
        }
      }
      java.util.Collections.sort(listOfTerms);
      sortedListOfTerms = new SearchSuggestion[listOfTerms.size()];
      for (int i = 0; i < listOfTerms.size(); i++) {
        sortedListOfTerms[i] = listOfTerms.get(i);
      }
      return sortedListOfTerms;
    } catch (OntologyServiceException e) {
      e.printStackTrace();
    }
    return new SearchSuggestion[0];
  }
}
