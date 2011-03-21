package uk.ac.ebi.fgpt.magecomet.client.fileservice;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface FileServiceAsync {
	
	void writeFile(String fileName,String tableAsAString, AsyncCallback<String> callback);

}
