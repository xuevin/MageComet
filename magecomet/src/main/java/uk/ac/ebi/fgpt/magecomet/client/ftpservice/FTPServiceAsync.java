package uk.ac.ebi.fgpt.magecomet.client.ftpservice;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface FTPServiceAsync {
	void getExperimentJSON(String experimentAccession,AsyncCallback<String> callback);
}
