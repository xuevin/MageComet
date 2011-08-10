package uk.ac.ebi.fgpt.magecomet.client.service.fileservice;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Interface for saving a document given its string representation
 * 
 * @author Vincent Xue
 * 
 */
@RemoteServiceRelativePath("FileService")
public interface FileService extends RemoteService {
  /**
   * Send the string to the server so the user can download it.
   * 
   * @param experimentAccession
   *          Experiment accession
   * @param tableAsAString
   *          String representation of a 2d table.
   * @return the url where the file is found.
   */
  String writeFile(String experimentAccession, String tableAsAString);
}
