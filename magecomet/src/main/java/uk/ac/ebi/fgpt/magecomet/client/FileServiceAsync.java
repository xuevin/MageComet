package uk.ac.ebi.fgpt.magecomet.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface FileServiceAsync {
	
	void getURL(String experiemntAccession,String tableAsAString, AsyncCallback<String> callback);

}
