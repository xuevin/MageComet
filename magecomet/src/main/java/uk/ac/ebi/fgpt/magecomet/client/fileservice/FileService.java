package uk.ac.ebi.fgpt.magecomet.client.fileservice;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("FileService")
public interface FileService extends RemoteService{
	String writeFile(String experiemntAccession,String tableAsAString);
}


