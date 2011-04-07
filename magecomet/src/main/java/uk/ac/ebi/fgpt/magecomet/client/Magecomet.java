package uk.ac.ebi.fgpt.magecomet.client;

import uk.ac.ebi.fgpt.magecomet.client.fileservice.FileService;
import uk.ac.ebi.fgpt.magecomet.client.fileservice.FileServiceAsync;
import uk.ac.ebi.fgpt.magecomet.client.fileservice.FileServiceCallback;
import uk.ac.ebi.fgpt.magecomet.client.searchservice.SearchOracle;
import uk.ac.ebi.fgpt.magecomet.client.validationservice.ValidationService;
import uk.ac.ebi.fgpt.magecomet.client.validationservice.ValidationServiceAsync;
import uk.ac.ebi.fgpt.magecomet.client.validationservice.ValidationServiceCallback;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.types.TabBarControls;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.tab.TabSet;

// Set - Replace Existing or add if it does not exist 
// Get - 
// Add - add 
// 


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */

public class Magecomet implements EntryPoint {
	/**
	 * This is the entry point method.
	 */

	/**
	 * Declare the panels that will be used
	 */
	private final GuiMediator guiMediator = new GuiMediator();

	private final Button exportSDRFButton = new Button("Export SDRF");
	private final Button exportIDFButton = new Button("Export IDF");
	private final TabSet topTabSet = new TabSet();
	private final EditTab editTab = new EditTab(guiMediator);
	private final ErrorsTab errorTab = new ErrorsTab(guiMediator);
	private final LoadTab loadTab = new LoadTab(guiMediator);
	private final SearchOracle searchOracle = new SearchOracle();
	private final SuggestCanvas suggestCanvasItem= new SuggestCanvas("suggestBox", "suggestBox", searchOracle);
	private final Button revalidateButton = new Button("Revalidate");
	private final Button confirmFactorValues = new Button("Confirm Factor Values");
	
	/**
	 * Declares the Variables that will be instantiated on module load / file load
	 */
	
	private FileServiceAsync fileService = GWT.create(FileService.class);
	private ValidationServiceAsync validationService = GWT.create(ValidationService.class);

	
	
	public void onModuleLoad() {
		/*
		 * SmartGWT components
		 */
		topTabSet.setTabBarPosition(Side.TOP);
		topTabSet.setTabBarAlign(Side.LEFT);
		topTabSet.setHeight100();
		topTabSet.setWidth100();
		topTabSet.addTab(loadTab);
		topTabSet.addTab(editTab);
		topTabSet.addTab(errorTab);
		topTabSet.setTabBarControls(TabBarControls.TAB_SCROLLER,
				TabBarControls.TAB_PICKER, suggestCanvasItem, confirmFactorValues, exportIDFButton,
				exportSDRFButton,revalidateButton);
//		topTabSet.moveBy(0, 140);
		topTabSet.show();

		

		/*
		 * Buttons
		 */
		confirmFactorValues.setWidth(150);
		confirmFactorValues.addClickHandler(new ClickHandler() {
			
			public void onClick(ClickEvent event) {
				guiMediator.showIDFFactorValue_ValidatorWindow();
			}
		});
		
		exportSDRFButton.setIcon("[SKIN]actions/download.png");
		exportSDRFButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(!guiMediator.getSDRFAsString().equals("")){
					fileService.writeFile(guiMediator.getCurrentSDRFTitle(), guiMediator.getSDRFAsString(), new FileServiceCallback());	
				}
				
			}
		});
		
		exportIDFButton.setIcon("[SKIN]actions/download.png");
		exportIDFButton.addClickHandler(new ClickHandler() {
			
			public void onClick(ClickEvent event) {
				
				if(!guiMediator.getIDFAsString().equals("")){
					fileService.writeFile(guiMediator.getCurrentIDFTitle(), guiMediator.getIDFAsString(), new FileServiceCallback());	
				}
				
			}
		});
		
		
		revalidateButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(!guiMediator.getSDRFAsString().equals("")){
					validationService.validate(	guiMediator.getCurrentIDFTitle(), 
												guiMediator.getIDFAsString(), 
												guiMediator.getCurrentSDRFTitle(),
												guiMediator.getSDRFAsString(),
												new ValidationServiceCallback(guiMediator));
				}
				
			}
		});
		
		//*****************************
        // Layout
        //*****************************		
		
		TagCloudWindow tagCloudWindow = new TagCloudWindow(guiMediator);
		tagCloudWindow.show();
		tagCloudWindow.moveTo(400,0);
//		tagCloudWindow.moveAbove(canvas)
		
//		mainLayout.setHtmlElement(DOM.getElementById("webapp"));
//		mainLayout.show();

//		RootPanel.get("search").add(uploadPanel);
//		topTabSet.setHtmlElement(DOM.getElementById("webapp"));
		
		Window.addWindowClosingHandler(new Window.ClosingHandler() {
			public void onWindowClosing(Window.ClosingEvent closingEvent) {
				closingEvent
						.setMessage("Do you really want to leave the page?");
			}
		});
	}
}