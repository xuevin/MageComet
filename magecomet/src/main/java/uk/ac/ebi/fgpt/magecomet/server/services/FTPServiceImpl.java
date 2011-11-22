package uk.ac.ebi.fgpt.magecomet.server.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.util.HashMap;

import monq.jfa.Dfa;
import net.sf.json.JSONObject;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.ebi.fgpt.magecomet.client.service.ftpservice.FTPException;
import uk.ac.ebi.fgpt.magecomet.client.service.ftpservice.FTPService;
import uk.ac.ebi.fgpt.magecomet.server.AnnotareValidationException;
import uk.ac.ebi.fgpt.magecomet.server.JSONUtils;
import uk.ac.ebi.fgpt.magecomet.server.WhatIzItException;

import com.google.common.io.ByteStreams;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * This is the implementation of FTPService. It downloads files from ArrayExress
 * 
 * @author Vincent Xue
 * 
 */
public class FTPServiceImpl extends RemoteServiceServlet implements FTPService {
  private Logger logger = LoggerFactory.getLogger(this.getClass()); 
  
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
//      downloadAccessionViaFTP(experimentAccession.toUpperCase());
      downloadAccessionViaRest(experimentAccession.toUpperCase());
      File idfFile = hashOfAccessionFilesForIDF.get(experimentAccession);
      File sdrfFile = hashOfAccessionFilesForSDRF.get(experimentAccession);
      
      // Load SDRF
      responseJSONObject.put("sdrfArray", JSONUtils.getJSONArrayFromInputStream(sdrfFile.toURI().toURL()
          .openStream()));
      logger.info("SDRF parsed and stored in JSON Array");
      
      // LOAD IDF
      responseJSONObject.put("idfArray", JSONUtils.getJSONArrayFromInputStream(idfFile.toURI().toURL()
          .openStream()));
      logger.info("IDF parsed and stored in JSON Array");
      
      // WhatIzIt Items
      Dfa monqInput = (Dfa) getServletContext().getAttribute("monqInput");
      responseJSONObject.put("whatizitIDF", JSONUtils.getJSONArrayFromWhatIzIt(idfFile, monqInput));
      responseJSONObject.put("whatizitSDRF", JSONUtils.getJSONArrayFromWhatIzIt(sdrfFile, monqInput));
      logger.info("WhatIzIt stored in JSON Array");
      
      // Error Items
      responseJSONObject.put("error", JSONUtils.getErrorArray(hashOfAccessionFilesForIDF
          .get(experimentAccession), hashOfAccessionFilesForSDRF.get(experimentAccession)));
      logger.info("ERROR Items  stored in JSON Array");
      
    } catch (MalformedURLException e) {
      throw new FTPException("Problem Parsing: Please Report", e);
    } catch (AnnotareValidationException e) {
      logger.warn("There was a problem with validating");
    } catch (WhatIzItException e) {
      logger.warn("There was a problem with the WhatIzIt mining");
    } catch (IOException e) {
      throw new FTPException("Problem with local machine IO",e);
    } catch (NullPointerException e) {
      logger.warn("There was a problem with setting the context");
    }
    return responseJSONObject.toString();
  }
  
  private void downloadAccessionViaRest(String accession) throws IOException {
    // Only download the files which are needed
    if (hashOfAccessionFilesForSDRF.containsKey(accession)
        && hashOfAccessionFilesForIDF.containsKey(accession)) {
      return;
    }
    
    logger.info("Starting Download: " + accession);
    
    // Empty files
    File temp_sdrf = null;
    File temp_idf = null;
    
    try {
      // Rest Urls
      URL idfURL = new URL("http://www.ebi.ac.uk/arrayexpress/files/" + accession + "/" + accession
                           + ".idf.txt");
      URL sdrfURL = new URL("http://www.ebi.ac.uk/arrayexpress/files/" + accession + "/" + accession
                            + ".sdrf.txt");
      
      // Make temporary file structure
      File tempDir = File.createTempFile("magecometftp_", "tmp");
      tempDir.delete();
      tempDir.mkdir();
      
      temp_sdrf = new File(tempDir, (accession + ".sdrf.txt"));
      temp_sdrf.deleteOnExit();
      
      temp_idf = new File(tempDir, (accession + ".idf.txt"));
      temp_idf.deleteOnExit();
      // Download IDF
      HttpURLConnection conn = (HttpURLConnection) idfURL.openConnection();
      if (conn.getResponseCode() != 200) {
        throw new IOException(conn.getResponseMessage());
      }
      OutputStream out = new FileOutputStream(temp_idf);
      ByteStreams.copy(conn.getInputStream(), out);
      out.close();
      conn.disconnect();
      hashOfAccessionFilesForIDF.put(accession, temp_idf);
      
      // Download SDRF
      conn = (HttpURLConnection) sdrfURL.openConnection();
      if (conn.getResponseCode() != 200) {
        throw new IOException(conn.getResponseMessage());
      }
      out = new FileOutputStream(temp_sdrf);
      ByteStreams.copy(conn.getInputStream(), out);
      out.close();
      conn.disconnect();
      hashOfAccessionFilesForSDRF.put(accession, temp_sdrf);
      
      logger.info("IDF and SDRF successfully saved");
    } catch (NullPointerException e) {
      logger.warn("Some IO Exception");
    }
  }
  
  private void downloadAccessionViaFTP(String accession) throws FTPException {
    
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
    } catch (SocketException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
