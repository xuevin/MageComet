package uk.ac.ebi.fgpt.magecomet.server;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

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
            OntologyService liveEFO = new FileOntologyService(new URI("http://www.ebi.ac.uk/efo/efo.owl"));
//			OntologyService liveEFO = new FileOntologyService(getClass().getClassLoader().getResource("EFO_inferred_v142.owl").toURI());
			context.setAttribute("ontoService", liveEFO);
            context.setAttribute("monqInput", termsToInputReader(liveEFO.getAllTerms(null)));
		} catch (URISyntaxException e) {
			System.err.println("EFO IS NOT FOUND");
			e.printStackTrace();
		} catch (OntologyServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private InputStreamReader termsToInputReader(Set<OntologyTerm> allTerms) {
		InputStream inputStream = null;
		
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
		}
		//output closing tag
		input.append("\n</mwt>");

		try {
			inputStream = new ByteArrayInputStream(input.toString().getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return new InputStreamReader(inputStream);
		
		
	}
}
