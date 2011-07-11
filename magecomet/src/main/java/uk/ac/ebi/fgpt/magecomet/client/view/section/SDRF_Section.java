package uk.ac.ebi.fgpt.magecomet.client.view.section;

import java.util.logging.Logger;

import uk.ac.ebi.fgpt.magecomet.client.GuiMediator;
import uk.ac.ebi.fgpt.magecomet.client.view.tab.ExtractTab;
import uk.ac.ebi.fgpt.magecomet.client.view.tab.FilterTab;
import uk.ac.ebi.fgpt.magecomet.client.view.window.SDRF_Section_ColumnEditor;

import com.smartgwt.client.data.AdvancedCriteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.types.FetchMode;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.types.TabBarControls;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.CellSavedEvent;
import com.smartgwt.client.widgets.grid.events.CellSavedHandler;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.tab.TabSet;

public class SDRF_Section extends SectionStackSection {
  private final ListGrid sdrfTable = new ListGrid();
  private final TabSet automaticFunctionEditor = new TabSet();
  private final IButton editColumnsButton = new IButton("Edit Columns");
  private final IButton undoButton = new IButton("Undo");
  private final IButton redoButton = new IButton("Redo");
  /*
   * Used to modify all of the records even after filtering If you plan to add/remove anything to the grid,
   * you must edit this list
   */

  private SDRF_Section_ColumnEditor columnEditorWindow;
  
  private GuiMediator guiMediator;
  
  private Logger logger = Logger.getLogger(getClass().toString());
  
  public SDRF_Section(final GuiMediator guiMediator) {
    super("SDRF");
    
    // Mediator Actions
    this.guiMediator = guiMediator;
    this.guiMediator.registerSDRFSection(this);
    columnEditorWindow = new SDRF_Section_ColumnEditor(guiMediator);
    
    sdrfTable.setCanEdit(true);
    sdrfTable.setEditEvent(ListGridEditEvent.DOUBLECLICK);
    sdrfTable.setEditByCell(true);
    sdrfTable.setCanReorderRecords(true);
    sdrfTable.setCellHeight(22);
    // sdrfTable.setDragDataAction(DragDataAction.MOVE);
    sdrfTable.setShowRollOver(false);
    sdrfTable.setPadding(0);
    sdrfTable.setMargin(0);
    sdrfTable.setSelectionType(SelectionStyle.MULTIPLE);
    sdrfTable.setShowBackgroundComponent(false);
    sdrfTable.setCanReorderFields(false);
    // sdrfTable.setShowAllRecords(true); //Do not enable this as it will make the interface slow
    // sdrfTable.setShowAllColumns(true); //Do not enable this as it will make the interface slow
    sdrfTable.setAutoFetchData(false);
    sdrfTable.setDataFetchMode(FetchMode.BASIC);
    sdrfTable.addCellSavedHandler(new CellSavedHandler() {
      @Override
      public void onCellSaved(CellSavedEvent event) {
        String newValue = "";
        if (event.getNewValue() != null) {
          newValue = event.getNewValue().toString();
        }
        
        guiMediator.setCell(event.getRecord().getAttribute("key"), sdrfTable.getField(event.getColNum())
            .getName(), newValue);
        guiMediator.saveState();
        System.out.println(guiMediator.getSDRFAsString());
      }
    });
    
    guiMediator.getStateCounter().setContents("State: " + 0);
    guiMediator.getStateCounter().setVisibility(Visibility.VISIBLE);
    
    editColumnsButton.setWidth(120);
    editColumnsButton.setIcon("[SKIN]DatabaseBrowser/column.png");
    editColumnsButton.setTop(250);
    editColumnsButton.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        if (guiMediator.dataHasBeenLoaded()) {
          columnEditorWindow.updateColumnsInColumnEditor();
          columnEditorWindow.centerInPage();
          columnEditorWindow.show();
        }
      }
    });
    
    undoButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        guiMediator.loadPreviousState();
        
      }
    });
    
    redoButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        guiMediator.redo();
      }
    });
    
    FilterTab filterAndReplaceTab = new FilterTab(guiMediator);
    
    ExtractTab createNewColumnsTab = new ExtractTab(guiMediator);
    
    automaticFunctionEditor.setWidth100();
    automaticFunctionEditor.setHeight(60);
    automaticFunctionEditor.setTabBarControls(TabBarControls.TAB_PICKER);
    automaticFunctionEditor.setPaneContainerOverflow(Overflow.VISIBLE);
    automaticFunctionEditor.setOverflow(Overflow.VISIBLE);
    automaticFunctionEditor.setTabs(filterAndReplaceTab, createNewColumnsTab);
    automaticFunctionEditor.setTabBarControls(TabBarControls.TAB_SCROLLER, TabBarControls.TAB_PICKER,
      guiMediator.getStateCounter(), undoButton, redoButton, editColumnsButton);
    automaticFunctionEditor.setTabBarAlign(Side.LEFT);
    automaticFunctionEditor.setTabBarPosition(Side.TOP);
    
    addItem(automaticFunctionEditor);
    addItem(sdrfTable);
  }
  
  public void setData(DataSource data, ListGridField[] listOfFields) {
    sdrfTable.setDataSource(data);
    sdrfTable.setFields(listOfFields);
    sdrfTable.fetchData();
  }
  
  public void filterTable(AdvancedCriteria filterCritria, DSCallback callback) {
    // sdrfTable.setCriteria(filterCritria);
    sdrfTable.invalidateCache();
    sdrfTable.filterData(filterCritria, callback);
  }
  
  public void filterTable(AdvancedCriteria advancedCriteria) {
    sdrfTable.filterData(advancedCriteria);
  }
  
  public String[] getRecordKeys() {
    RecordList recordlist = sdrfTable.getResultSet();
    
    int length = recordlist.getLength();
    String[] listOfKeys = new String[length];
    for (int i = 0; i < length; i++) {
      listOfKeys[i] = recordlist.get(i).getAttribute("key");
    }
    return listOfKeys;
  }
}
