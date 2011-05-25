package uk.ac.ebi.fgpt.magecomet.client.service.validationservice;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ValidationServiceAsync {

	void validate(String currentIDFTitle, String idfAsString,
			String currentSDRFTitle, String sdrfAsString,
			AsyncCallback<String> validationServiceCallback);

}
