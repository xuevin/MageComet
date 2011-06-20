package uk.ac.ebi.fgpt.magecomet.client.service.fileservice;


import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.RootPanel;

public class FileServiceCallback implements AsyncCallback<String> {
	private String fileName;
  private Logger logger = Logger.getLogger(getClass().toString());
	public FileServiceCallback(String fileName){
		this.fileName = fileName;
	}

	public void onFailure(Throwable caught) {
    logger.log(Level.WARNING,"File Service Callback Failed");

		String details = caught.getMessage();
	}

	public void onSuccess(String absolutePath) {
	  logger.log(Level.INFO,"File Service Callback Success");
		String fileDownloadURL = GWT.getHostPageBaseURL()
				+ "Magecomet/DownloadServlet" + "?fileURL=" + URL.encode(absolutePath) + "&fileName=" + fileName;
		Frame fileDownloadFrame = new Frame(fileDownloadURL);
		fileDownloadFrame.setSize("0px", "0px");
		fileDownloadFrame.setVisible(false);
		RootPanel panel = RootPanel.get("__gwt_downloadFrame");

		while (panel.getWidgetCount() > 0)
			panel.remove(0);
		panel.add(fileDownloadFrame);
	}
}
