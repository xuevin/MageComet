package uk.ac.ebi.fgpt.magecomet.client.validationservice;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ValidationServiceAsync {

	void validate(String currentIDFTitle, String idfAsString,
			String currentSDRFTitle, String sdrfAsString,
			AsyncCallback<String> validationServiceCallback);

}
