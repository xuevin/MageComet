package uk.ac.ebi.fgpt.magecomet.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import uk.ac.ebi.fgpt.magecomet.client.service.fileservice.FileService;
import uk.ac.ebi.fgpt.magecomet.client.service.fileservice.FileServiceAsync;
import uk.ac.ebi.fgpt.magecomet.client.service.fileservice.FileServiceCallback;
import uk.ac.ebi.fgpt.magecomet.client.service.searchservice.SearchOracle;
import uk.ac.ebi.fgpt.magecomet.client.service.validationservice.ValidationService;
import uk.ac.ebi.fgpt.magecomet.client.service.validationservice.ValidationServiceAsync;
import uk.ac.ebi.fgpt.magecomet.client.service.validationservice.ValidationServiceCallback;
import uk.ac.ebi.fgpt.magecomet.client.view.canvas.SuggestCanvas;
import uk.ac.ebi.fgpt.magecomet.client.view.tab.EditTab;
import uk.ac.ebi.fgpt.magecomet.client.view.tab.ErrorsTab;
import uk.ac.ebi.fgpt.magecomet.client.view.tab.LoadTab;
import uk.ac.ebi.fgpt.magecomet.client.view.window.TagCloudWindow;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.types.TabBarControls;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.tab.TabSet;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */

public class Magecomet implements EntryPoint {
  /**
   * This is the entry point method.
   */
  
  private final GuiMediator guiMediator = new GuiMediator();
  private final Button exportSDRFButton = new Button("Export SDRF");
  private final Button exportIDFButton = new Button("Export IDF");
  private final TabSet topTabSet = new TabSet();
  private final EditTab editTab = new EditTab(guiMediator);
  private final ErrorsTab errorTab = new ErrorsTab(guiMediator);
  private final LoadTab loadTab = new LoadTab(guiMediator);
  private final SearchOracle searchOracle = new SearchOracle();
  private final SuggestCanvas suggestCanvasItem = new SuggestCanvas("suggestBox", "suggestBox", searchOracle);
  private final Button revalidateButton = new Button("Revalidate");
  private final Button confirmFactorValues = new Button("Confirm Factor Values");
  private Logger logger = Logger.getLogger(getClass().toString());
  
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
    topTabSet.setTabBarControls(TabBarControls.TAB_SCROLLER, TabBarControls.TAB_PICKER, suggestCanvasItem,
      confirmFactorValues, exportIDFButton, exportSDRFButton, revalidateButton);
    RootPanel.get("contentsarea").add(topTabSet);
    
    /*
     * Buttons
     */
    // Confirm Factor Values
    confirmFactorValues.setWidth(150);
    confirmFactorValues.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        guiMediator.showIDFFactorValue_ValidatorWindow();
      }
    });
    // Export SDRF
    exportSDRFButton.setIcon("[SKIN]actions/download.png");
    exportSDRFButton.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        if (!guiMediator.getCurrentSDRFTitle().equals("null")) {
          logger.log(Level.INFO, "Export SDRF Button was pressed");
          fileService.writeFile(guiMediator.getCurrentSDRFTitle(), guiMediator.getSDRFAsString(),
            new FileServiceCallback(guiMediator.getCurrentSDRFTitle()));
        }
        
      }
    });
    // Export IDF
    exportIDFButton.setIcon("[SKIN]actions/download.png");
    exportIDFButton.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        if (!guiMediator.getCurrentIDFTitle().equals("null")) {
          logger.log(Level.INFO, "Export IDF Button was pressed");
          fileService.writeFile(guiMediator.getCurrentIDFTitle(), guiMediator.getIDFAsString(),
            new FileServiceCallback(guiMediator.getCurrentIDFTitle()));
        }
      }
    });
    // Revalidate Button
    revalidateButton.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        if (!guiMediator.getCurrentSDRFTitle().equals("null")
            && !guiMediator.getCurrentIDFTitle().equals("null")) {
          validationService.validate(guiMediator.getCurrentIDFTitle(), guiMediator.getIDFAsString(),
            guiMediator.getCurrentSDRFTitle(), guiMediator.getSDRFAsString(), new ValidationServiceCallback(
                guiMediator));
        }
        
      }
    });
    
    // *****************************
    // Layout
    // *****************************
    
    TagCloudWindow tagCloudWindow = new TagCloudWindow(guiMediator);
    tagCloudWindow.show();
    tagCloudWindow.moveTo(250, 85);
    // tagCloudWindow.moveAbove(canvas)
    
    // mainLayout.setHtmlElement(DOM.getElementById("webapp"));
    // mainLayout.show();
    
    // topTabSet.setHtmlElement(DOM.getElementById("webapp"));
    
    Window.addWindowClosingHandler(new Window.ClosingHandler() {
      public void onWindowClosing(Window.ClosingEvent closingEvent) {
        closingEvent.setMessage("Do you really want to leave the page?");
      }
    });
  }
}