package uk.ac.ebi.fgpt.magecomet.server;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import uk.ac.ebi.fgpt.magecomet.client.searchservice.SearchSuggestion;
import uk.ac.ebi.ontocat.OntologyService;
import uk.ac.ebi.ontocat.OntologyServiceException;
import uk.ac.ebi.ontocat.OntologyTerm;
import uk.ac.ebi.ontocat.file.FileOntologyService;




public class InitilizationServletContextListener implements ServletContextListener{

	private ServletContext context = null;
	public void contextDestroyed(ServletContextEvent arg0) {
		
		
	}

	public void contextInitialized(ServletContextEvent arg0) {
		context = arg0.getServletContext();
		try {
			System.out.println("Downloading EFO Service");
			//During Testing
			OntologyService ontoService;
            ontoService = new FileOntologyService(new URI("http://www.ebi.ac.uk/efo/efo.owl"));
//			ontoService = new FileOntologyService(getClass().getClassLoader().getResource("EFO_inferred_v142.owl").toURI());
			context.setAttribute("ontoService", ontoService);
            context.setAttribute("monqInput", termsToInputReader(ontoService.getAllTerms(null)));
            context.setAttribute("ontoCatDict", createDictionary(ontoService));
		} catch (URISyntaxException e) {
			System.err.println("EFO IS NOT FOUND");
			e.printStackTrace();
		} catch (OntologyServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private String termsToInputReader(Set<OntologyTerm> allTerms) {
		StringBuilder input = new StringBuilder();
	 
		//Print Headers
		input.append("<?xml version='1.0'?>");
		input.append("\n<mwt>");
		input.append("\n<template><span style=\"color:red;text-decoration: overline;\">");
		input.append("\n<a title=\"%1\" label=\"%2\">%0</a>");
		input.append("\n</span></template>");
		
		for(OntologyTerm term:allTerms){
			input.append("\n");
			input.append("<t p1=");
			input.append("\""+term.getAccession()+"\"");
			input.append(" p2=\""+term.getLabel()+"\"");
			input.append(">"+term.getLabel()+"</t>");
//			try {
//				for(String syn:ontoService.getSynonyms(term)){
//					String syn2 = syn.replaceAll("[^a-zA-Z0-9]", "");
//					input.append("\n");
//					input.append("<t p1=");
//					input.append("\""+term.getAccession()+"\"");
//					input.append(" p2=\""+syn2+"\"");
//					input.append(">"+syn2+"</t>");	
//				}
//			} catch (OntologyServiceException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
		//output closing tag
		input.append("\n</mwt>");
		
		return input.toString();
		
		
	}
	private SearchSuggestion[] createDictionary(OntologyService ontoCat){
		SearchSuggestion[] sortedListOfTerms;
		try {
			//TODO perhaps this is not the most efficient;
			ArrayList<SearchSuggestion> listOfTerms=new ArrayList<SearchSuggestion>();
			for(OntologyTerm ontoTerm:ontoCat.getAllTerms(null)){
				listOfTerms.add(new SearchSuggestion(ontoTerm.getLabel(),ontoTerm.getLabel(), ontoTerm.getAccession()));
				//Add synonymns
				for(String syn:ontoCat.getSynonyms(ontoTerm)){
					listOfTerms.add(new SearchSuggestion(syn,ontoTerm.getLabel(), ontoTerm.getAccession()));
				}
			}
			java.util.Collections.sort(listOfTerms);
			sortedListOfTerms = new SearchSuggestion[listOfTerms.size()];
			for(int i=0;i<listOfTerms.size();i++){
				sortedListOfTerms[i]=listOfTerms.get(i);
			}
			return sortedListOfTerms;
		} catch (OntologyServiceException e) {
			e.printStackTrace();
		}
		return new SearchSuggestion[0];
	}
}
