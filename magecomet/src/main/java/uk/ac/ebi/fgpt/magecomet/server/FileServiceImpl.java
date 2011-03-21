package uk.ac.ebi.fgpt.magecomet.server;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import uk.ac.ebi.fgpt.magecomet.client.fileservice.FileService;


public class FileServiceImpl extends RemoteServiceServlet implements FileService{
	public String writeFile(String fileName,String tableAsAString) {
		try {
			// Create file
			BufferedWriter bufferedWriter;
			File file = new File("/tmp/"+fileName);
			bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"iso-8859-1"));
			bufferedWriter.write(tableAsAString);
			bufferedWriter.close();
			
			return(file.getAbsolutePath());   					
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
			return "Error With Output";
		}
	}
	
}
