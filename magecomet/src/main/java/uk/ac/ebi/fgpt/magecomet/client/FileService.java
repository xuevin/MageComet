package uk.ac.ebi.fgpt.magecomet.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("FileService")
public interface FileService extends RemoteService{
	String getURL(String experiemntAccession,String tableAsAString);
}


