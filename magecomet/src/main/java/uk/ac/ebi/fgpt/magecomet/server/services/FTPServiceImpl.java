package uk.ac.ebi.fgpt.magecomet.server.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.util.HashMap;

import monq.jfa.Dfa;
import net.sf.json.JSONObject;

import org.apache.commons.net.ftp.FTPClient;

import uk.ac.ebi.fgpt.magecomet.client.service.ftpservice.FTPException;
import uk.ac.ebi.fgpt.magecomet.client.service.ftpservice.FTPService;
import uk.ac.ebi.fgpt.magecomet.server.AnnotareValidationException;
import uk.ac.ebi.fgpt.magecomet.server.JSONUtils;
import uk.ac.ebi.fgpt.magecomet.server.WhatIzItException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class FTPServiceImpl extends RemoteServiceServlet implements FTPService {
  private String arrayExpressFtp = "ftp.ebi.ac.uk";
  private String arrayExpressFtpPath = "/pub/databases/microarray/data/experiment/";
  
  private HashMap<String,File> hashOfAccessionFilesForSDRF = new HashMap<String,File>();
  private HashMap<String,File> hashOfAccessionFilesForIDF = new HashMap<String,File>();
  
  public String getExperimentJSON(String experimentAccession) throws FTPException {
    if (experimentAccession.length() < 6) {
      throw new FTPException("Too Short");
    }
    JSONObject responseJSONObject = new JSONObject();
    
    try {
      downloadAccession(experimentAccession.toUpperCase());
      
      File idfFile = hashOfAccessionFilesForIDF.get(experimentAccession);
      File sdrfFile = hashOfAccessionFilesForSDRF.get(experimentAccession);
      
      // Load SDRF
      responseJSONObject.put("sdrfArray", JSONUtils.getJSONArrayFromInputStream(sdrfFile.toURI().toURL()
          .openStream()));
      System.out.println("SDRF parsed and stored in JSON Array");
      
      // LOAD IDF
      responseJSONObject.put("idfArray", JSONUtils.getJSONArrayFromInputStream(idfFile.toURI().toURL()
          .openStream()));
      System.out.println("IDF parsed and stored in JSON Array");
      
      // WhatIzIt Items
      Dfa monqInput = (Dfa) getServletContext().getAttribute("monqInput");
      responseJSONObject.put("whatizitIDF", JSONUtils.getJSONArrayFromWhatIzIt(idfFile, monqInput));
      responseJSONObject.put("whatizitSDRF", JSONUtils.getJSONArrayFromWhatIzIt(sdrfFile, monqInput));
      System.out.println("WhatIzIt stored in JSON Array");
      
      // Error Items
      responseJSONObject.put("error", JSONUtils.getErrorArray(hashOfAccessionFilesForIDF
          .get(experimentAccession), hashOfAccessionFilesForSDRF.get(experimentAccession)));
      System.out.println("ERROR Items  stored in JSON Array");
      
    } catch (MalformedURLException e) {
      e.printStackTrace();
      throw new FTPException("Problem Parsing: Please Report");
    } catch (AnnotareValidationException e) {
      e.printStackTrace();
      System.err.println("There was a problem with validating");
    } catch (WhatIzItException e) {
      e.printStackTrace();
      System.err.println("There was a problem with the WhatIzIt mining");
    } catch (IOException e) {
      e.printStackTrace();
      throw new FTPException("Problem with local machine IO");
    } catch (NullPointerException e) {
      e.printStackTrace();
      System.err.println("There was a problem with setting the context");
    }
    
    return responseJSONObject.toString();
  }
  
  private void downloadAccession(String accession) throws FTPException {
    
    FTPClient client = new FTPClient();
    FileOutputStream fos_sdrf = null;
    FileOutputStream fos_idf = null;
    
    File temp_sdrf = null;
    File temp_idf = null;
    
    try {
      client.connect(arrayExpressFtp);
      client.login("anonymous", "");
      
      // Only download the files which are needed
      if (hashOfAccessionFilesForSDRF.containsKey(accession)
          && hashOfAccessionFilesForIDF.containsKey(accession)) {
        return;
      }
      
      try {
        File tempDir = File.createTempFile("magecometftp_", "tmp");
        tempDir.delete();
        tempDir.mkdir();
        
        temp_sdrf = new File(tempDir, (accession + ".sdrf.txt"));
        temp_sdrf.deleteOnExit();
        
        temp_idf = new File(tempDir, (accession + ".idf.txt"));
        temp_idf.deleteOnExit();
        
        fos_sdrf = new FileOutputStream(temp_sdrf);
        fos_idf = new FileOutputStream(temp_idf);
        
        String pipeline = accession.substring(2, 6);
        String sdrfFile = accession + ".sdrf.txt";
        String idfFile = accession + ".idf.txt";
        
        client.retrieveFile(arrayExpressFtpPath + pipeline + "/" + accession + "/" + sdrfFile, fos_sdrf);
        
        if (client.getReplyString().contains("226")) {
          System.out.println(sdrfFile + "\tFile Received");
          hashOfAccessionFilesForSDRF.put(accession, temp_sdrf);
        } else {
          System.out.println(sdrfFile + "\tFailed");
          throw new FTPException(sdrfFile);
        }
        
        client.retrieveFile(arrayExpressFtpPath + pipeline + "/" + accession + "/" + idfFile, fos_idf);
        
        if (client.getReplyString().contains("226")) {
          System.out.println(idfFile + "\tFile Received");
          hashOfAccessionFilesForIDF.put(accession, temp_idf);
        } else {
          System.out.println(idfFile + "\tFailed");
          throw new FTPException(idfFile);
        }
        
      } catch (IOException e) {
        e.printStackTrace();
      } finally {
        try {
          if (fos_sdrf != null) {
            fos_sdrf.close();
            fos_idf.close();
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      client.disconnect();
//    } catch (SocketException e) {
//      e.printStackTrace();
//    } catch (IOException e) {
//      e.printStackTrace();
    }
  }
}
