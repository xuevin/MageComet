package uk.ac.ebi.fgpt.magecomet.client;

import gwtupload.client.IUploader;
import gwtupload.client.MultiUploader;
import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader.UploadedInfo;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.smartgwt.client.types.HeaderControls;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class magecomet implements EntryPoint {
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
	private final SectionStack sectionStack = new SectionStack();

	private final IDF_Section idfSection = new IDF_Section();
	private final SDRF_Section sdrfSection = new SDRF_Section();
	private final Tab editTab = new Tab("Edit");
	private final ErrorsTab errorTab = new ErrorsTab();
	private final TagCloud tagCloud = new TagCloud();
	private Window tagCloudWindow = new Window();
	private final Tab tagCloudTab1 = new Tab("Weight By Location");
	private final Tab tagCloudTab2 = new Tab("Weight By Errors");

	/**
	 * Declares the Widgets that will be used
	 */
	private SuggestBox EFOSuggestBox = new SuggestBox();

	public void onModuleLoad() {
		


		TabSet topTabSet = new TabSet();
		topTabSet.setTabBarPosition(Side.TOP);
		topTabSet.setTabBarAlign(Side.LEFT);
		topTabSet.setHeight100();
		topTabSet.setWidth100();
		
		
		sectionStack.setVisibilityMode(VisibilityMode.MULTIPLE);
		sectionStack.setSections(idfSection,sdrfSection);
		sectionStack.setHeight100();
		sectionStack.setWidth100();
		sectionStack.setMargin(0);
		sectionStack.setPadding(0);
		
				
		MultiUploader dataUploader = new MultiUploader();
		// Add a finish handler which will load the image once the upload
		// finishes
		dataUploader.addOnFinishUploadHandler(onFinishUploaderHandler);
		
		
		editTab.setPane(sectionStack);
		
		topTabSet.addTab(editTab);
		topTabSet.addTab(errorTab);

		
		//Tabs For Cloud
		// 1 - Where Did it Occur Tag
		
		// 2 - Weight 
		
		//TabSet for cloud
		TabSet tagCloudTabSet = new TabSet();
		tagCloudTabSet.setTabBarPosition(Side.TOP);
		tagCloudTabSet.setTabBarAlign(Side.LEFT);
		tagCloudTabSet.setHeight100();
		tagCloudTabSet.setWidth100();
		
		
		
		
		
		tagCloudTabSet.addTab(tagCloudTab1);
		tagCloudTabSet.addTab(tagCloudTab2);
		
		
//		tagCloud.setVisible(true);
		tagCloud.setWidth("100%");
		
		Canvas gwtCanvas = new Canvas();
		gwtCanvas.addChild(tagCloud);

		tagCloudTab1.setPane(gwtCanvas);
	
		tagCloudWindow.setHeaderControls(HeaderControls.HEADER_LABEL,HeaderControls.MINIMIZE_BUTTON);
		tagCloudWindow.setTitle("Tag Cloud");
		tagCloudWindow.setWidth("500");
		tagCloudWindow.setHeight("250");
		tagCloudWindow.addItem(tagCloudTabSet);
		tagCloudWindow.setCanDragResize(true);
		tagCloudWindow.moveTo(400, 0);
		tagCloudWindow.minimize();
		tagCloudWindow.show();
		
		//********DEBUBING***********************
		ClickHandler popup = new ClickHandler() {
			@Override
			public void onClick(
					//TODO Remove GWT elements and only use SMARTGWT
					com.google.gwt.event.dom.client.ClickEvent arg0) {
				new AutofillPopup("Blank");
				
			}
			
		};
		tagCloud.addWord("sarcoidoisis",popup,2);
		tagCloud.addWord("protocol",popup,2);
		tagCloud.addWord("Homo sapiens",popup,2);
		tagCloud.addWord("RNA",popup,2);
		//********DEBUBING***********************


		
		
		Canvas gwtUploadCanvas = new Canvas();
		HorizontalPanel uploadPanel = new HorizontalPanel();
		gwtUploadCanvas.addChild(uploadPanel);
		uploadPanel.add(dataUploader);
		uploadPanel.add(EFOSuggestBox);
		uploadPanel.add(new com.google.gwt.user.client.ui.Label("EFO Search"));
		uploadPanel.setHeight("60px");
		
	
		mainLayout.setHeight100();
		mainLayout.setWidth100();
		mainLayout.addMember(gwtUploadCanvas);
		mainLayout.addMember(topTabSet);
		mainLayout.addChild(tagCloudWindow);
		mainLayout.show();
		
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
				} else if (info.name.contains("idf")) {
					idfSection.handleJSONObject(jsonObject);
					fillTagCloud(jsonObject, 1);
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
				
				ClickHandler popup = new ClickHandler() {
					@Override
					public void onClick(
							//TODO Remove GWT elements and only use SMARTGWT
							com.google.gwt.event.dom.client.ClickEvent arg0) {
						new AutofillPopup(word);
						
					}
					
				};
				tagCloud.addWord(word,popup,weight);

			}
		}
	};
}
