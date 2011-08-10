package uk.ac.ebi.fgpt.magecomet.client.service.validationservice;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Async call back interface.
 * 
 * @author Vincent Xue
 * 
 */
public interface ValidationServiceAsync {
  
  void validate(String currentIDFTitle,
                String idfAsString,
                String currentSDRFTitle,
                String sdrfAsString,
                AsyncCallback<String> validationServiceCallback);
  
}
