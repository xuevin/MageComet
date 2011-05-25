package uk.ac.ebi.fgpt.magecomet.client.service.validationservice;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("ValidationService")
public interface ValidationService extends RemoteService{
	String validate(String currentIDFTitle, String idfAsString,
			String currentSDRFTitle, String sdrfAsString);
}


