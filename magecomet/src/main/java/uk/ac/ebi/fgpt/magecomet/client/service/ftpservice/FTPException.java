package uk.ac.ebi.fgpt.magecomet.client.service.ftpservice;

import java.io.Serializable;

/**
 * General FTP exception
 * 
 * @author Vincent Xue
 * 
 */
public class FTPException extends Exception implements Serializable {
  private String experimentAccession;
  
  public FTPException() {}
  
  public FTPException(String experimentAccession) {
    this.experimentAccession = experimentAccession;
  }
  
  public String getAccession() {
    return this.experimentAccession;
  }
}
