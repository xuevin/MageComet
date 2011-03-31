package uk.ac.ebi.fgpt.magecomet.client;

import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.AutoFitWidthApproach;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.TransferImgButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.VStack;

/**
 * @author vincent@ebi.ac.uk
 *
 */
public class SDRF_Section_ColumnEditor extends Window{
	//Saved states of column editor
	private RecordList savedActiveRecords;
	private RecordList savedClipboardRecords;
	private final ListGrid clipboardGrid = new ListGrid();
	private final ListGrid activeGrid = new ListGrid();
	private ListGrid sdrfTable;
	private int uniqueKeyCount;
	private GuiMediator guiMediator;

	
	/**
	 * 
	 * This is a window which has the function to edit the SDRF table.
	 * 
	 * @param sdrfTable the SDRF table to be modified
	 * @param originalKeyCount the number of fields that were originally in the SDRF
	 * @param guiMediator the mediator, which allows for this window to update other windows.
	 */
	public SDRF_Section_ColumnEditor(final ListGrid sdrfTable, final int originalKeyCount,final GuiMediator guiMediator) {
		super();
		this.sdrfTable=sdrfTable;
		this.uniqueKeyCount=originalKeyCount;
		this.guiMediator = guiMediator;
		this.guiMediator.registerSDRFSectionColumnEditor(this);
		
		
		setTitle("Customize SDRF Columns");
		
		//Clipboard
		clipboardGrid.setVisible(true);
		clipboardGrid.setWidth("45%");
		clipboardGrid.setHeight("95%");
		clipboardGrid.setLayoutAlign(VerticalAlignment.CENTER);
		clipboardGrid.setCanAcceptDrop(true);
		clipboardGrid.setCanAcceptDroppedRecords(true);
		clipboardGrid.setCanDragRecordsOut(true);
		clipboardGrid.setCanReorderRecords(true);  
		clipboardGrid.setCanEdit(true);
		
		//Active Grid
		activeGrid.setVisible(true);
		activeGrid.setWidth("45%");
		activeGrid.setHeight("95%");
		activeGrid.setLayoutAlign(VerticalAlignment.CENTER);
		activeGrid.setCanAcceptDrop(true);
		activeGrid.setCanAcceptDroppedRecords(true);
		activeGrid.setCanDragRecordsOut(true);
		activeGrid.setCanReorderRecords(true);  
		activeGrid.setCanSort(true);
		activeGrid.setCanEdit(true);
		
		//Make keys for each grid
		ListGridField clipboardKey= new ListGridField("key","Key");
		ListGridField clipboardField = new ListGridField("title","Clipboard");
		clipboardKey.setHidden(true);
		clipboardGrid.setFields(clipboardKey,clipboardField);
		
		ListGridField activeKey= new ListGridField("key","Key");
		ListGridField activeField = new ListGridField("title","Visible Column");
		activeKey.setHidden(true);
		activeGrid.setFields(activeKey,activeField);

		
		//Get the fields in the table and make them records. This allows 
		//users to reorder them vertically instead of horizontally with lots of scrolling
		activeGrid.setData(convertFieldsToRecords());
		
		//Save Column States
		savedClipboardRecords=clipboardGrid.getDataAsRecordList();
		savedActiveRecords=activeGrid.getDataAsRecordList();
		
		//******************
		// Buttons
		//******************
		
		// Button to add new columns		
		IButton addButton = new IButton();
		addButton.setWidth(24);
		addButton.setHeight(24);
		addButton.setShowRollOver(true);
		addButton.setIcon("[SKIN]actions/add.png");

		addButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				int uniqueKey = getNewUniqueKey();
				ListGridRecord newColumn = new ListGridRecord();
				newColumn.setAttribute("key",uniqueKey);
				newColumn.setAttribute("title", "New Column"+uniqueKey);
				clipboardGrid.addData(newColumn);
			}
		});
		
		//Button To Transfer 
		TransferImgButton leftArrow = new TransferImgButton(TransferImgButton.LEFT);
		leftArrow.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				clipboardGrid.transferSelectedData(activeGrid);
			}
		});
		TransferImgButton rightArrow = new TransferImgButton(TransferImgButton.RIGHT);
		rightArrow.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				activeGrid.transferSelectedData(clipboardGrid);
			}
		});
		
		//Make a new button to confirm changes
		IButton save = new IButton("Save");  
    	save.setLeft(0);  
    	save.addClickHandler(new ClickHandler() {  
    		public void onClick(ClickEvent event) {  
    			saveAction();
    			guiMediator.refreshTable();
    		}
    	});
    	
    	//Make a new button to discard changes
		IButton cancel = new IButton("Cancel");  
		cancel.setLeft(0);  
    	cancel.addClickHandler(new ClickHandler() {  
    		public void onClick(ClickEvent event) {
    			discardAction();
    		}
    	});

		
    	
    	//******************
    	// Layout
    	//******************
		VStack arrows = new VStack();
		arrows.setWidth(20);
		arrows.setAlign(Alignment.CENTER);
		arrows.addMember(addButton);
		arrows.addMember(leftArrow);
		arrows.addMember(rightArrow);
    			
    	// Simple HStack for save and cancel button
		HStack actionButtons = new HStack();
		actionButtons.setAlign(Alignment.RIGHT);
		actionButtons.setHeight(20);
		actionButtons.addMember(save);
		actionButtons.addMember(cancel);
		
		// HStack for 2 tables and transfer buttons
		HStack hStack = new HStack();
		hStack.setAlign(Alignment.CENTER);
		hStack.setAlign(VerticalAlignment.CENTER);
		hStack.addMember(clipboardGrid);
		hStack.addMember(arrows);
		hStack.addMember(activeGrid);
		
		addItem(hStack);
		addItem(actionButtons);		
	}
	/**
	 * Updates the list of columns in the editor.
	 */
	public void updateColumns() {
		savedActiveRecords=convertFieldsToRecords();
		activeGrid.setData(savedActiveRecords);
	}
	public String addNewColumnToClipboardAndGetKey(String title){
		int uniqueKey = getNewUniqueKey();
		ListGridRecord newColumn = new ListGridRecord();
		newColumn.setAttribute("key",uniqueKey);
		newColumn.setAttribute("title", title);
		clipboardGrid.addData(newColumn);
		
		//Save
		savedClipboardRecords=clipboardGrid.getDataAsRecordList();
		return uniqueKey+"";
	}
	public String addNewCharacteristicColumnAndGetKey(String title){
		int uniqueKey = getNewUniqueKey();
		ListGridRecord newColumn = new ListGridRecord();
		newColumn.setAttribute("key",uniqueKey);
		newColumn.setAttribute("title", "Characteristics["+title+"]");
		
		
		//Find the right place to put this!
		for(int i = 0;i<savedActiveRecords.getLength();i++){
			if(savedActiveRecords.get(i).getAttributeAsString("title").contains("Source Name")){
				savedActiveRecords.addAt(newColumn, i+1);
				break;
			}
		}
	
		//Save
		saveAction();
		return uniqueKey+"";
	
	}
	public String addNewFactorValueColumnAndGetKey(String title){
		int uniqueKey = getNewUniqueKey();
		ListGridRecord newColumn = new ListGridRecord();
		newColumn.setAttribute("key",uniqueKey);
		newColumn.setAttribute("title", "Factor Value["+title+"]");
		activeGrid.addData(newColumn);
		
		//Save
		saveAction();
		return uniqueKey+"";
	}
	private void discardAction() {
		//discard changes... restore the original
		activeGrid.setData(savedActiveRecords);
		clipboardGrid.setData(savedClipboardRecords);
		hide();		
	}
	private void saveAction() {
		Record[] activeFieldArray = activeGrid.getRecords();
		
		//Set the grid to reflect the new columns
		ListGridField[] newActiveColumns = new ListGridField[activeFieldArray.length+1];
		newActiveColumns[0]=new ListGridField("key","Key");
		for(int i =0;i<activeFieldArray.length;i++){
			newActiveColumns[i+1]=new ListGridField(	activeFieldArray[i].getAttribute("key"),
														activeFieldArray[i].getAttribute("title"));
			if(GlobalConfigs.shouldExclude(activeFieldArray[i].getAttribute("title"))){
				newActiveColumns[i+1].setHidden(true);
			}else{
				//Only set autowidth for visible columns (Speed Up?)
				newActiveColumns[i+1].setAutoFitWidthApproach(AutoFitWidthApproach.BOTH);
				newActiveColumns[i+1].setAutoFitWidth(true);	
			}
		}
		sdrfTable.setFields(newActiveColumns);   
		
		//Save Column States
		savedClipboardRecords=clipboardGrid.getDataAsRecordList();
		savedActiveRecords=activeGrid.getDataAsRecordList();
		
		//Update other gui components with the change
		guiMediator.updateDataSource(newActiveColumns);
		hide();
		
	}
	/**
	 * Gets a unique key that represents a column
	 * 
	 * @return a new unique key that represents a column
	 */
	private int getNewUniqueKey(){
		uniqueKeyCount++;
		return uniqueKeyCount;
	}
	/**
	 * Gets an RecordList, which is basically an array
	 * of fields. This retrieves the list from the SDRF Table.
	 * The order of the records determines the order of the columns.
	 * 
	 * For each listGridRecord, there is a key and title attribute.
	 * The key attribute uniquely identifies the column and is used to retrieve
	 * the data
	 * 
	 * The title attribute is the name of the column, such as "Term Source REF"
	 * 
	 * <br>(KEY=1)...(TITLE= Term Source)
	 * <br>(KEY=20)...(TITLE= Comment)
	 * <br>(Key=3)...(Title= Sample)
	 *  
	 * @return an array of ListGridRecords, which describes the order of the columns
	 */
	private RecordList convertFieldsToRecords(){
		RecordList recordList = new RecordList();
//		ListGridRecord[] columnsArray= new ListGridRecord[sdrfTable.getAllFields().length-1];
		int i =0;
		for(ListGridField field:sdrfTable.getAllFields()){
			if(i!=0){
				ListGridRecord record = new ListGridRecord();
				record.setAttribute("title", field.getTitle());
				record.setAttribute("key", field.getName());
				recordList.add(record);
			}
			i++;	
		}
		return recordList;
	}
}
