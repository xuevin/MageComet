package uk.ac.ebi.fgpt.magecomet.client.view.tab;

import gwtupload.client.IUploader;
import gwtupload.client.MultiUploader;
import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader.OnChangeUploaderHandler;
import gwtupload.client.IUploader.UploadedInfo;

import java.util.logging.Level;
import java.util.logging.Logger;

import uk.ac.ebi.fgpt.magecomet.client.GuiMediator;
import uk.ac.ebi.fgpt.magecomet.client.service.ftpservice.FTPException;
import uk.ac.ebi.fgpt.magecomet.client.service.ftpservice.FTPService;
import uk.ac.ebi.fgpt.magecomet.client.service.ftpservice.FTPServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.VerticalPanel;
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

/**
 * The tab that describes how to load files
 * 
 * @author Vincent Xue
 * 
 */
public class LoadTab extends Tab {
  private GuiMediator guiMediator;
  
  private final HTMLFlow erroReport = new HTMLFlow();
  private final TextItem accessionInput = new TextItem("accession", "Experiment Accession");
  private FTPServiceAsync ftpServiceAsync = GWT.create(FTPService.class);
  private final Img loadImage = new Img("[SKIN]loadingSmall.gif");
  private final VStack vstack = new VStack();
  private Logger logger = Logger.getLogger(getClass().toString());
  
  public LoadTab(GuiMediator guiMediator) {
    super("Load");
    this.guiMediator = guiMediator;
    this.guiMediator.registerLoadTab(this);
    
    accessionInput.setWrapTitle(false);
    accessionInput.setWidth(200);
    // accessionInput.setCharacterCasing(CharacterCasing.UPPER);
    // accessionInput.setMask(">");
    
    DynamicForm form = new DynamicForm();
    form.setItems(accessionInput);
    IButton submit = new IButton("Submit");
    submit.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        submitAction();
      }
    });
    IButton reset = new IButton("Reset and Start New Experiment");
    reset.setWidth(250);
    reset.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        reload();
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
    hStack.addMember(erroReport);
    hStack.setDefaultLayoutAlign(VerticalAlignment.CENTER);
    loadImage.setVisibility(Visibility.HIDDEN);
    
    /*
     * GWT Components
     */

    MultiUploader dataUploader = new MultiUploader();
    dataUploader.setMaximumFiles(2);
    dataUploader.addOnFinishUploadHandler(onFinishUploaderHandler);
    dataUploader.addOnChangeUploadHandler(onChangeUploadHandler);
    
    InlineHTML instructions = new InlineHTML("To being, please load the IDF and SDRF or load by accession");
    VerticalPanel uploadPanel = new VerticalPanel();
    uploadPanel.add(instructions);
    uploadPanel.add(dataUploader);
    uploadPanel.setHeight("70px");
    uploadPanel.setWidth("300px");
    
    Canvas gwtUploadCanvas = new Canvas();
    gwtUploadCanvas.setStyleName("gwt-SuggestBoxCanvas");
    gwtUploadCanvas.addChild(uploadPanel);
    
    // HTMLFlow header = new HTMLFlow();
    // header.setContents("MageComet");
    // header.setHeight("30");
    // header.setStyleName("header");
    
    Image header = new Image("images/magecomet.svg");
    header.setPixelSize(200, 75);
    
    vstack.addMember(header);
    vstack.addMember(gwtUploadCanvas);
    vstack.addMember(hStack);
    vstack.addMember(reset);
    
    /*
     * SmartGWT Components
     */
    setPane(vstack);
    setIcon("[SKIN]TreeGrid/folder_open.png");
    
  }
  
  private native void reload() /*-{ 
                               $wnd.location.reload(); 
                               }-*/;
  
  private void submitAction() {
    loadImage.setVisibility(Visibility.VISIBLE);
    ftpServiceAsync.getExperimentJSON(accessionInput.getDisplayValue().toUpperCase(),
      new AsyncCallback<String>() {
        
        public void onSuccess(String arg0) {
          logger.log(Level.INFO, "JSON Received");
          JSONObject jsonObject = JSONParser.parseStrict(arg0).isObject();
          guiMediator.resetTagCloud();
          guiMediator.loadSDRFData(jsonObject);
          guiMediator.loadIDFData(jsonObject);
          guiMediator.passDataToErrorsTab(jsonObject);
          guiMediator.passDataToTagCloud(jsonObject);
          guiMediator.setCurrentIDFTitle((accessionInput.getDisplayValue()).toUpperCase() + ".idf.txt");
          guiMediator.setCurrentSDRFTitle((accessionInput.getDisplayValue()).toUpperCase() + ".sdrf.txt");
          loadImage.setVisibility(Visibility.HIDDEN);
          
          erroReport.setContents("Success!");
        }
        
        public void onFailure(Throwable arg0) {
          loadImage.setVisibility(Visibility.HIDDEN);
          if (arg0 instanceof FTPException) {
            erroReport.setContents("Fail! " + arg0);
          }
        }
      });
    
  }
  
  private IUploader.OnChangeUploaderHandler onChangeUploadHandler = new OnChangeUploaderHandler() {
    
    @Override
    public void onChange(IUploader uploader) {
      uploader.submit();
    }
  };
  
  // Fill in the corresponding sections
  private IUploader.OnFinishUploaderHandler onFinishUploaderHandler = new IUploader.OnFinishUploaderHandler() {
    public void onFinish(IUploader uploader) {
      if (uploader.getStatus() == Status.SUCCESS) {
        
        UploadedInfo info = uploader.getServerInfo();
        
        System.out.println("File name " + info.name);
        
        if (info.name == null) {
          System.out.println("Problem with uploader");
        }
        
        // Here is the string returned in your servlet
        JSONObject jsonObject = JSONParser.parseStrict(info.message).isObject();
        
        logger.log(Level.INFO, "JSON Received");
        // Parse the response according to the name of the file
        if (info.name.contains("sdrf")) {
          guiMediator.loadSDRFData(jsonObject);
          guiMediator.passDataToTagCloud(jsonObject);
          guiMediator.setCurrentSDRFTitle(info.name);
        } else if (info.name.contains("idf")) {
          guiMediator.loadIDFData(jsonObject);
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
