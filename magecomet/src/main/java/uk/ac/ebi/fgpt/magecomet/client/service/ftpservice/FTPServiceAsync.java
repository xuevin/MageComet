package uk.ac.ebi.fgpt.magecomet.client.service.ftpservice;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Async call back interface.
 * 
 * @author Vincent Xue
 * 
 */
public interface FTPServiceAsync {
  void getExperimentJSON(String experimentAccession, AsyncCallback<String> callback);
}
