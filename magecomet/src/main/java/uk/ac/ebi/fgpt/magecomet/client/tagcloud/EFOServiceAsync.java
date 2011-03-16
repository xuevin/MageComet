package uk.ac.ebi.fgpt.magecomet.client.tagcloud;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface EFOServiceAsync {
	void getEfoAccessionIdByName(String efoName,AsyncCallback<String> callback);
	void getEfoDescriptionByName(String efoName,AsyncCallback<String> callback);

}
