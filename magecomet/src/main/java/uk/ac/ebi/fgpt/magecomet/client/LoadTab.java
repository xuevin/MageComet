package uk.ac.ebi.fgpt.magecomet.client;


import uk.ac.ebi.fgpt.magecomet.client.ftpservice.FTPException;
import uk.ac.ebi.fgpt.magecomet.client.ftpservice.FTPService;
import uk.ac.ebi.fgpt.magecomet.client.ftpservice.FTPServiceAsync;
import gwtupload.client.IUploader;
import gwtupload.client.MultiUploader;
import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader.UploadedInfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.KeyPressEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyPressHandler;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.VStack;
import com.smartgwt.client.widgets.tab.Tab;

public class LoadTab extends Tab{
	private GuiMediator guiMediator;
	private final TextItem accessionInput = new TextItem("accession","Experiment Accession");
	private FTPServiceAsync ftpServiceAsync = GWT.create(FTPService.class);
	private Img loadImage = new Img("[SKIN]loadingSmall.gif");
	public LoadTab(GuiMediator guiMediator){
		super("Load");
		this.guiMediator=guiMediator;
		this.guiMediator.registerLoadTab(this);
		
		
		VStack vstack = new VStack();
		accessionInput.setWrapTitle(false);
		accessionInput.setWidth(200);
//		accessionInput.setCharacterCasing(CharacterCasing.UPPER);
//		accessionInput.setMask(">");

		
		DynamicForm form = new DynamicForm();
		form.setItems(accessionInput);
		IButton submit = new IButton();
		submit.setTitle("Submit");
		submit.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				submitAction();
			}
		});
		accessionInput.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {

				if (event.getKeyName().equals("Enter")) {
					submitAction();
				}
			}
		});

		
		loadImage.setSize(16);
		HStack hStack = new HStack();
		hStack.setHeight(15);
		hStack.addMember(form);
		hStack.addMember(submit);
		hStack.addMember(loadImage);
		hStack.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		loadImage.setVisibility(Visibility.HIDDEN);
		
		
		
		/*
		 * GWT Components 
		 */
		
		
		MultiUploader dataUploader = new MultiUploader();
		dataUploader.addOnFinishUploadHandler(onFinishUploaderHandler);
		
		HorizontalPanel uploadPanel = new HorizontalPanel();
		uploadPanel.add(dataUploader);
		uploadPanel.setHeight("70px");
		
		Canvas gwtUploadCanvas = new Canvas();
		gwtUploadCanvas.setStyleName("gwt-SuggestBoxCanvas");
		gwtUploadCanvas.addChild(uploadPanel);
		
		
		HTMLFlow header= new HTMLFlow();
		header.setContents("MageComet");
		header.setHeight("30");
		header.setStyleName("header");
		
		
		vstack.addMember(header);
		vstack.addMember(gwtUploadCanvas);
		vstack.addMember(hStack);
		
		/*
		 * SmartGWT Components
		 */
		setPane(vstack);
		setIcon("[SKIN]TreeGrid/folder_open.png");
		
	}
	private void submitAction() {
		loadImage.setVisibility(Visibility.VISIBLE);
		ftpServiceAsync.getExperimentJSON(accessionInput.getDisplayValue().toUpperCase(), new AsyncCallback<String>() {
			
			public void onSuccess(String arg0) {
				JSONObject jsonObject = JSONParser.parseStrict(arg0).isObject();
				guiMediator.passDataToSDRFSection(jsonObject);
				guiMediator.passDataToIDFSection(jsonObject);
				guiMediator.passDataToErrorsTab(jsonObject);
				guiMediator.passDataToTagCloud(jsonObject);
				guiMediator.setCurrentIDFTitle((accessionInput.getDisplayValue()).toUpperCase()+".idf.txt");
				guiMediator.setCurrentSDRFTitle((accessionInput.getDisplayValue()).toUpperCase()+"sdrf.txt");
				loadImage.setVisibility(Visibility.HIDDEN);
			}
			
			public void onFailure(Throwable arg0) {
				loadImage.setVisibility(Visibility.HIDDEN);
				if(arg0 instanceof FTPException){
					System.out.println(((FTPException) arg0).getAccession());
				}
			}
		});
		
		
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
					guiMediator.passDataToSDRFSection(jsonObject);
					guiMediator.passDataToTagCloud(jsonObject);
					guiMediator.setCurrentSDRFTitle(info.name);
				} else if (info.name.contains("idf")) {
					guiMediator.passDataToIDFSection(jsonObject);
					guiMediator.passDataToTagCloud(jsonObject);
					guiMediator.setCurrentIDFTitle(info.name);
				} else {
					// Do Nothing
				}
				guiMediator.passDataToErrorsTab(jsonObject);
			}
		}
	};
	
}
