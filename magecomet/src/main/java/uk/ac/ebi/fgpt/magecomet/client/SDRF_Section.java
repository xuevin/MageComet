 package uk.ac.ebi.fgpt.magecomet.client;

import com.smartgwt.client.data.AdvancedCriteria;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.types.TabBarControls;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.tab.TabSet;

public class SDRF_Section extends SectionStackSection{
	private final ListGrid sdrfTable = new ListGrid();
	private final TabSet automaticFunctionEditor = new TabSet();
	private final IButton editColumnsButton = new IButton("Edit Columns");  

	/*
	 * Used to modify all of the records even after filtering
	 * If you plan to add/remove anything to the grid, you must edit this list
	 */
	
	private SDRF_Section_ColumnEditor columnEditorWindow;
	
	
	private GuiMediator guiMediator;
	
	public SDRF_Section(final GuiMediator guiMediator){
		super("SDRF");
		//Mediator Actions
		this.guiMediator = guiMediator;
		this.guiMediator.registerSDRFSection(this);
		
		columnEditorWindow = new SDRF_Section_ColumnEditor(guiMediator);
		
		sdrfTable.setCanEdit(true);
	 	sdrfTable.setEditEvent(ListGridEditEvent.DOUBLECLICK);  
        sdrfTable.setEditByCell(true); 
        sdrfTable.setCanReorderRecords(true);    
        sdrfTable.setCellHeight(22); 
//      sdrfTable.setDragDataAction(DragDataAction.MOVE);  
        sdrfTable.setShowRollOver(false);
        sdrfTable.setPadding(0);
        sdrfTable.setMargin(0);
        sdrfTable.setSelectionType(SelectionStyle.MULTIPLE);
        sdrfTable.setShowBackgroundComponent(false);
        sdrfTable.setCanReorderFields(true);
        sdrfTable.setDrawAheadRatio((float)2);
        
        
        editColumnsButton.setWidth(120);
		editColumnsButton.setIcon("[SKIN]DatabaseBrowser/column.png");
        editColumnsButton.setTop(250);  
        editColumnsButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(guiMediator.dataHasBeenLoaded()){
					columnEditorWindow.updateColumnsInColumnEditor();
					columnEditorWindow.centerInPage();
					columnEditorWindow.show();
				}
			}
		});
        
        FilterTab filterAndReplaceTab = new FilterTab(guiMediator);
        ExtractTab createNewColumnsTab = new ExtractTab(guiMediator);
        
        automaticFunctionEditor.setWidth100();
        automaticFunctionEditor.setHeight(60);
        automaticFunctionEditor.setTabBarControls(TabBarControls.TAB_PICKER);
        automaticFunctionEditor.setPaneContainerOverflow(Overflow.VISIBLE);
        automaticFunctionEditor.setOverflow(Overflow.VISIBLE);
        automaticFunctionEditor.setTabs(filterAndReplaceTab,createNewColumnsTab);
        automaticFunctionEditor.setTabBarControls(TabBarControls.TAB_SCROLLER, TabBarControls.TAB_PICKER,editColumnsButton);
        automaticFunctionEditor.setTabBarAlign(Side.LEFT);
        automaticFunctionEditor.setTabBarPosition(Side.TOP);
        
        addItem(automaticFunctionEditor);
        addItem(sdrfTable);
	}
	public void setData(DataSource data, ListGridField[] listOfFields, ListGridRecord[] listOfAllRecords){
		sdrfTable.setDataSource(data);
		sdrfTable.setFields(listOfFields);
		sdrfTable.fetchData();

		
		
		//Pass data to FilterTab
		guiMediator.passDataSourceToFilterTab(data);
		//Pass all fields to Extract Tab
		guiMediator.passAllRecordsToExtractTab(listOfAllRecords,sdrfTable);
	}
	public void refreshTable(DataSource data, ListGridField[] listOfFields) {
		
		sdrfTable.setDataSource(data);
		sdrfTable.setFields(listOfFields);
		sdrfTable.fetchData();
		guiMediator.updateColumnsInComboBoxes(listOfFields);
		guiMediator.passDataSourceToFilterTab(data);
	}
	public void filterTable(AdvancedCriteria filterCritria){
		sdrfTable.filterData(filterCritria);
	}
	public ListGridRecord[] getListOfRecords(){
		return sdrfTable.getRecords();
	}
}
