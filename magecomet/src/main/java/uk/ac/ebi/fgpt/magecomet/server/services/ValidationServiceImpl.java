package uk.ac.ebi.fgpt.magecomet.server.services;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;


import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import uk.ac.ebi.fgpt.magecomet.client.validationservice.ValidationService;
import uk.ac.ebi.fgpt.magecomet.server.JSONUtils;


public class ValidationServiceImpl extends RemoteServiceServlet implements ValidationService{
	public String validate(String idfFileName,String idfAsString,String sdrfFileName, String sdrfAsString) {
		try {
			
			
			// Create file
			BufferedWriter idfBufferedWriter;
			File idfFile = new File("/tmp/"+idfFileName);
			idfBufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(idfFile),"iso-8859-1"));
			idfBufferedWriter.write(idfAsString);
			idfBufferedWriter.close();
			System.out.println(idfFileName + " was written successfully");
		
			// Create file
			BufferedWriter sdrfBufferedWriter;
			File sdrfFile = new File("/tmp/"+sdrfFileName);
			sdrfBufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(sdrfFile),"iso-8859-1"));
			sdrfBufferedWriter.write(sdrfAsString);
			sdrfBufferedWriter.close();
			System.out.println(sdrfFileName + " was written successfully");
			
			return JSONUtils.getErrorArray(idfFile, sdrfFile).toString();
			
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
			return "Error With Output";
		}
	}
	
}
