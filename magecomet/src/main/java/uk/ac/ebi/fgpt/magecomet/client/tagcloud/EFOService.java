package uk.ac.ebi.fgpt.magecomet.client.tagcloud;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * An interface for interacting with a server side ontology.
 * 
 * @author Vincent Xue
 * 
 */
@RemoteServiceRelativePath("EFOService")
public interface EFOService extends RemoteService {
  /**
   * @param efoName
   *          The efo label
   * @return EFO accession id
   */
  String getEfoAccessionIdByName(String efoName);
  
  /**
   * @param efoName
   * @return EFO description (if any)
   */
  String getEfoDescriptionByName(String efoName);
}
