package uk.ac.ebi.fgpt.magecomet.server.services;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import uk.ac.ebi.fgpt.magecomet.client.service.fileservice.FileService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * This is the implementation of the FileService. It takes strings and writes them to file.
 * 
 * @author Vincent Xue
 * 
 */
public class FileServiceImpl extends RemoteServiceServlet implements FileService {
  public String writeFile(String fileName, String tableAsAString) {
    try {
      // Create file
      BufferedWriter bufferedWriter;
      String tmpPath = System.getProperty("java.io.tmpdir");
      
      File file = new File(tmpPath, fileName);
      bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "iso-8859-1"));
      bufferedWriter.write(tableAsAString);
      bufferedWriter.close();
      
      return (file.getAbsolutePath());
    } catch (Exception e) {// Catch exception if any
      System.err.println("Error: " + e.getMessage());
      return "Error With Output";
    }
  }
  
}
