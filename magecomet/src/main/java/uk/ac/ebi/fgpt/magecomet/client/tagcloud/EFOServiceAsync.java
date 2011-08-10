package uk.ac.ebi.fgpt.magecomet.client.tagcloud;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * A simple async call back interface
 * 
 * @author Vincent Xue
 * 
 */
public interface EFOServiceAsync {
  void getEfoAccessionIdByName(String efoName, AsyncCallback<String> callback);
  
  void getEfoDescriptionByName(String efoName, AsyncCallback<String> callback);
  
}
