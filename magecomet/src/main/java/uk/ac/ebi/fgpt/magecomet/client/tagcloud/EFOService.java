package uk.ac.ebi.fgpt.magecomet.client.tagcloud;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("EFOService")
public interface EFOService extends RemoteService{
	String getEfoAccessionIdByName(String efoName);
	String getEfoDescriptionByName(String efoName);
}
