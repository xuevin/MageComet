package uk.ac.ebi.fgpt.magecomet.client.service.validationservice;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Passes the SDRF and IDF as strings to the server for validation
 * 
 * @author Vincent Xue
 * 
 */
@RemoteServiceRelativePath("ValidationService")
public interface ValidationService extends RemoteService {
  /**
   * @param currentIDFTitle
   *          the idf title (ie E-GEOD-13367.idf.txt)
   * @param idfAsString
   *          the string representation of the 2D IDF array
   * @param currentSDRFTitle
   *          the sdrf title (ie E-GEOD-13367.sdrf.txt)
   * @param sdrfAsString
   *          the string representation of the 2D SDRF array
   * @return A JSON string of the errors
   */
  String validate(String currentIDFTitle, String idfAsString, String currentSDRFTitle, String sdrfAsString);
}
