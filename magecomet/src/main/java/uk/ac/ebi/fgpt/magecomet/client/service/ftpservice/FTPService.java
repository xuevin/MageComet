package uk.ac.ebi.fgpt.magecomet.client.service.ftpservice;


import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("FTPService")
public interface FTPService extends RemoteService{
	String getExperimentJSON(String experimentAccession) throws FTPException;
}


