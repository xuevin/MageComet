package uk.ac.ebi.fgpt.magecomet.client.service.ftpservice;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Interface for getting a JSON object from an ArrayExpress Accession
 * 
 * @author Vincent Xue
 * 
 */
@RemoteServiceRelativePath("FTPService")
public interface FTPService extends RemoteService {
  /**
   * Gets the JSON object given an experiment accession
   * 
   * @param experimentAccession
   *          ArrayExpress accession
   * @return String representation of JSON object
   * @throws FTPException
   */
  String getExperimentJSON(String experimentAccession) throws FTPException;
}
