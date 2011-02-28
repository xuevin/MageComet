package uk.ac.ebi.fgpt.magecomet.server;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.MalformedURLException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import uk.ac.ebi.fgpt.magecomet.client.FileService;


public class FileServiceImpl extends RemoteServiceServlet implements FileService{
	@Override
	public String writeFile(String experimentAccession,String tableAsAString) {
		try {
			// Create file
			BufferedWriter bufferedWriter;
			File file = new File("/tmp/"+experimentAccession+".sdrf.txt");
			bufferedWriter = new BufferedWriter(new FileWriter(file));
			bufferedWriter.write(tableAsAString);
			bufferedWriter.close();
			return(file.toURI().toURL().toString());   					
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
			return "Error With Output";
		}
	}
	
}
