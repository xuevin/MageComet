package uk.ac.ebi.fgpt.magecomet.client;

import java.util.LinkedHashMap;

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
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.VStack;

/**
 * @author chrnovx
 *
 */
public class SDRF_Section_ColumnEditor extends Window{
	//Saved states of column editor
	private RecordList savedActiveRecords;
	private RecordList savedScratcRecords;
	private final ListGrid scratchGrid = new ListGrid();
	private final ListGrid activeGrid = new ListGrid();
	private ListGrid sdrfTable;
	private int uniqueKeyCount;
	private ComboBoxItem columnChooserCombobox;

	
	/**
	 * This creates a new Window which has the controls to edit the Columns in the SDRF
	 * 
	 * @param sdrfTable The ListGrid SDRF table that is being modified
	 * @param columnChooserCombobox  The ComboBoxItem that is used to filter the data
	 * @param originalKeyCount The original number of columns in the SDRF (used to generate unique keys)
	 */
	public SDRF_Section_ColumnEditor(final ListGrid sdrfTable, final ComboBoxItem columnChooserCombobox, int originalKeyCount) {
		super();
		this.sdrfTable=sdrfTable;
		this.uniqueKeyCount=originalKeyCount;
		this.columnChooserCombobox=columnChooserCombobox; // Needs combo box item because it is updated when the user clicks save
		
		setTitle("Customize SDRF Columns");
		
		//Scratch Grid
		scratchGrid.setVisible(true);
		scratchGrid.setWidth("45%");
		scratchGrid.setHeight("95%");
		scratchGrid.setLayoutAlign(VerticalAlignment.CENTER);
		scratchGrid.setCanAcceptDrop(true);
		scratchGrid.setCanAcceptDroppedRecords(true);
		scratchGrid.setCanDragRecordsOut(true);
		scratchGrid.setCanReorderRecords(true);  
		scratchGrid.setCanEdit(true);
	
		
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
		ListGridField scratchKey= new ListGridField("key","Key");
		ListGridField scratchField = new ListGridField("title","Scratch Columns");
		scratchKey.setHidden(true);
		scratchGrid.setFields(scratchKey,scratchField);
		
		
		ListGridField activeKey= new ListGridField("key","Key");
		ListGridField activeField = new ListGridField("title","Visible Column");
		activeKey.setHidden(true);
		activeGrid.setFields(activeKey,activeField);
		
		//Get the fields in the table and make them records. This allows 
		//users to reorder them vertically instead of horizontally with lots of scrolling
		activeGrid.setData(convertFieldsToRecords());
		
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
				scratchGrid.addData(newColumn);
				
			}
		});
		
		//Button To Transfer 
		TransferImgButton leftArrow = new TransferImgButton(TransferImgButton.LEFT);
		leftArrow.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				scratchGrid.transferSelectedData(activeGrid);
			}
		});
		TransferImgButton rightArrow = new TransferImgButton(TransferImgButton.RIGHT);
		rightArrow.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				activeGrid.transferSelectedData(scratchGrid);


			}
		});
				
		
		
		//Make a new button to confirm changes
		IButton save = new IButton("Save");  
    	save.setLeft(0);  
    	save.addClickHandler(new ClickHandler() {  
    		public void onClick(ClickEvent event) {  
    			Record[] activeFieldArray = activeGrid.getRecords();
    			ListGridField[] newActiveColumns = new ListGridField[activeFieldArray.length+1];
    			newActiveColumns[0]=new ListGridField("key","Key");
    			for(int i =0;i<activeFieldArray.length;i++){
    				newActiveColumns[i+1]=new ListGridField(	activeFieldArray[i].getAttribute("key"),
    														activeFieldArray[i].getAttribute("title"));
    				newActiveColumns[i+1].setAutoFitWidthApproach(AutoFitWidthApproach.BOTH);
    				newActiveColumns[i+1].setAutoFitWidth(true);
    			}
    			sdrfTable.setFields(newActiveColumns);
    			    			
    			//Save Column States
    			savedScratcRecords=scratchGrid.getDataAsRecordList();
    			savedActiveRecords=activeGrid.getDataAsRecordList();
    			
    			updateColumnsInComboBox();
    			//TODO
    			//Change column in the field
    			hide();
    		}
    	});
    	
    	
    	//Make a new button to discard changes
		IButton cancel = new IButton("Cancel");  
		cancel.setLeft(0);  
    	cancel.addClickHandler(new ClickHandler() {  
    		public void onClick(ClickEvent event) {
    			//discard changes... restore the original
    			
    			activeGrid.setData(savedActiveRecords);
    			scratchGrid.setData(savedScratcRecords);
    			//DON'T USE
    			//activeFields.setData(convertFieldsToRecords());
    			//Probably want to create a new popup instead
    			hide();
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
		hStack.addMember(scratchGrid);
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
	/**
	 * Updates the combo box. Should be called every time the table changes
	 */
	private void updateColumnsInComboBox() {
		LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();  
		for(ListGridField field:sdrfTable.getAllFields()){
			valueMap.put(field.getName(), field.getTitle());
		}
		if(columnChooserCombobox!=null){
			columnChooserCombobox.setValueMap(valueMap);	
		}
	}
}
