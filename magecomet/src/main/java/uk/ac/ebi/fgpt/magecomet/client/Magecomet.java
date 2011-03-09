package uk.ac.ebi.fgpt.magecomet.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.HeaderControls;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.types.TabBarControls;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.BaseWidget;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

import gwtupload.client.IUploader;
import gwtupload.client.MultiUploader;
import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader.UploadedInfo;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */

public class Magecomet implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	// private static final String SERVER_ERROR = "An error occurred while "
	// + "attempting to contact the server. Please check your network "
	// + "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */
	// private final GreetingServiceAsync greetingService =
	// GWT.create(GreetingService.class);

	/**
	 * This is the entry point method.
	 */

	/**
	 * Declare the panels that will be used
	 */
	private final VLayout mainLayout = new VLayout();
	
	private final GuiMediator guiMediator = new GuiMediator();
	
	//Section for Stacks
	private final SectionStack sectionStack = new SectionStack();
	private final IDF_Section idfSection = new IDF_Section(guiMediator);
	private final SDRF_Section sdrfSection = new SDRF_Section(guiMediator);
//	private final HStack saveStack = new HStack();
	private final Button saveSDRFButton = new Button("Export SDRF");
	private final TabSet topTabSet = new TabSet();
	private final Tab editTab = new Tab("Edit");
	private final ErrorsTab errorTab = new ErrorsTab();
	private String currentSDRF ="";
	private String currentIDF="";


	/**
	 * Declares the Widgets that will be used
	 */
	private SuggestBox EFOSuggestBox = new SuggestBox();
	
	private FileServiceAsync fileService = GWT.create(FileService.class);

//	private HTMLFlow header;
	
	public void onModuleLoad() {
		
		//Set the callback object for downloading files
		final AsyncCallback<String> callback = new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
		        String details = caught.getMessage();
			}
			public void onSuccess(String url){
				 String fileDownloadURL = GWT.getHostPageBaseURL() +
				 "magecomet/DownloadServlet" 
					 + "?fileURL=" + URL.encode(url); 
				 
				 Frame fileDownloadFrame = new Frame(fileDownloadURL); 
				 fileDownloadFrame.setSize("0px", "0px"); 
				 fileDownloadFrame.setVisible(false); 
				 RootPanel panel = RootPanel.get("__gwt_downloadFrame"); 

				 while (panel.getWidgetCount() > 0) 
					 panel.remove(0); 
				 panel.add(fileDownloadFrame); 
			}
		};

        //*****************************
        // Layout
        //*****************************		
		
		
		
//		header= new HTMLFlow();
//		header.setContents("Magecomet");
//		header.setHeight("30");
//		header.setStyleName("header");
		
		
		
		sectionStack.setVisibilityMode(VisibilityMode.MULTIPLE);
		sectionStack.setSections(idfSection,sdrfSection);
		sectionStack.setHeight100();
		sectionStack.setWidth100();
		sectionStack.setMargin(0);
		sectionStack.setPadding(0);
			
		MultiUploader dataUploader = new MultiUploader();
		dataUploader.addOnFinishUploadHandler(onFinishUploaderHandler);
		
		editTab.setPane(sectionStack);
		editTab.setIcon("[SKIN]actions/edit.png");
		
		topTabSet.setTabBarPosition(Side.TOP);
		topTabSet.setTabBarAlign(Side.LEFT);
		topTabSet.setHeight100();
		topTabSet.setWidth100();
		topTabSet.addTab(editTab);
		topTabSet.addTab(errorTab);
		topTabSet.setTabBarControls(TabBarControls.TAB_SCROLLER, TabBarControls.TAB_PICKER, saveSDRFButton);

		Canvas gwtUploadCanvas = new Canvas();
		HorizontalPanel uploadPanel = new HorizontalPanel();
		gwtUploadCanvas.addChild(uploadPanel);
		uploadPanel.add(dataUploader);
		uploadPanel.add(EFOSuggestBox);
		uploadPanel.add(new com.google.gwt.user.client.ui.Label("EFO Search"));
		uploadPanel.setHeight("60px");
		
		
		//===================
		
		saveSDRFButton.setIcon("[SKIN]actions/download.png");
		saveSDRFButton.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			
			public void onClick(ClickEvent event) {
				if(!sdrfSection.getSDRFAsString().equals("")){
					fileService.writeFile(currentSDRF, sdrfSection.getSDRFAsString(), callback);	
				}
				
			}
		});
		//====================
		
		TagCloudWindow tagCloudWindow = new TagCloudWindow(guiMediator);
		tagCloudWindow.show();
		tagCloudWindow.moveTo(600, 140);
//		tagCloudWindow.moveAbove(canvas)
		
		
		mainLayout.setHeight100();
		mainLayout.setWidth("98%");
//		mainLayout.addMember(header);
		mainLayout.addMember(gwtUploadCanvas);
		mainLayout.addMember(topTabSet);
		
		
		mainLayout.setHtmlElement(DOM.getElementById("webapp"));
		mainLayout.show();

//		RootPanel.get("webapp").add(mainLayout);
		
	}

	// Fill in the corresponding sections
	private IUploader.OnFinishUploaderHandler onFinishUploaderHandler = new IUploader.OnFinishUploaderHandler() {
		public void onFinish(IUploader uploader) {
			if (uploader.getStatus() == Status.SUCCESS) {
				
				UploadedInfo info = uploader.getServerInfo();
				
				System.out.println("File name " + info.name);
				
				if(info.name==null){
					System.out.println("Problem...");
					//FIXME This is a known problem that occurs in development 
				}

				// Here is the string returned in your servlet
				JSONObject jsonObject = JSONParser.parseStrict(info.message)
						.isObject();
				
				// Parse the response according to the name of the file
				if (info.name.contains("sdrf")) {
					sdrfSection.handleJSONObject(jsonObject);
					fillTagCloud(jsonObject, 2);
					currentSDRF = info.name;
				} else if (info.name.contains("idf")) {
					idfSection.handleJSONObject(jsonObject);
					fillTagCloud(jsonObject, 1);
					currentIDF = info.name;
				} else {
					// Do Nothing
				}
				
				updateErrors(jsonObject);
			}
		}
		private void updateErrors(JSONObject jsonObject){
			JSONArray errors = jsonObject.get("error").isArray();
			errorTab.handelJSONArrayOfErrors(errors);
		}
		private void fillTagCloud(JSONObject jsonObject, int weight) {
			JSONArray tagWords = jsonObject.get("whatizit").isArray();
			
			for (int i = 0; i < tagWords.size(); i++) {
				final String word = tagWords.get(i).isString().stringValue();
				
				
				guiMediator.addWordToTagCloud(word,weight);
			}
		}
	};
}