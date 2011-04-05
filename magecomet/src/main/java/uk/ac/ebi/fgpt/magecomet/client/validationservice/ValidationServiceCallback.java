package uk.ac.ebi.fgpt.magecomet.client.validationservice;

import uk.ac.ebi.fgpt.magecomet.client.GuiMediator;

import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ValidationServiceCallback implements AsyncCallback<String> {
	private GuiMediator guiMediator;
	public ValidationServiceCallback(GuiMediator guiMediator) {
		this.guiMediator=guiMediator;
	}

	public void onFailure(Throwable caught) {
		System.out.println(caught);
	}

	public void onSuccess(String jsonArrayAsString){
		System.out.println("Errors Successfully Reparsed");
		guiMediator.passArrayToErrorsTab(JSONParser.parseStrict(jsonArrayAsString).isArray());
	}
}
