package uk.ac.ebi.fgpt.magecomet.client.fileservice;


import uk.ac.ebi.fgpt.magecomet.client.GuiMediator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.RootPanel;

public class FileServiceCallback implements AsyncCallback<String> {
	private String fileName;
	public FileServiceCallback(String fileName){
		this.fileName = fileName;
	}

	public void onFailure(Throwable caught) {
		String details = caught.getMessage();
	}

	public void onSuccess(String absolutePath) {
		String fileDownloadURL = GWT.getHostPageBaseURL()
				+ "magecomet/DownloadServlet" + "?fileURL=" + URL.encode(absolutePath) + "&fileName=" + fileName;
		Frame fileDownloadFrame = new Frame(fileDownloadURL);
		fileDownloadFrame.setSize("0px", "0px");
		fileDownloadFrame.setVisible(false);
		RootPanel panel = RootPanel.get("__gwt_downloadFrame");

		while (panel.getWidgetCount() > 0)
			panel.remove(0);
		panel.add(fileDownloadFrame);
	}
}
