 package uk.ac.ebi.fgpt.magecomet.client;


import java.util.LinkedHashMap;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.AutoFitWidthApproach;
import com.smartgwt.client.types.DragDataAction;
import com.smartgwt.client.types.FieldType;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.TransferImgButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FilterBuilder;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VStack;

public class SDRF_Section extends SectionStackSection{
	private ListGrid sdrfTable;
	private FilterBuilder filterBuilder; 
	private HStack filterStack;
	private ComboBoxItem columnChooserCombobox;
	private TextItem cellValueTextItem;
	private Window columnWindow;
	private int uniqueKeyCount;
	
	public SDRF_Section(){
		super("SDRF");
		uniqueKeyCount=0;
		
		sdrfTable = new ListGrid();			
		sdrfTable.setCanEdit(true);
	 	sdrfTable.setEditEvent(ListGridEditEvent.CLICK);  
        sdrfTable.setEditByCell(true); 
        sdrfTable.setCanReorderRecords(true);     
        sdrfTable.setDragDataAction(DragDataAction.MOVE);  
        sdrfTable.setShowRollOver(false);
        sdrfTable.setPadding(0);
        sdrfTable.setMargin(0);
        sdrfTable.setSelectionType(SelectionStyle.MULTIPLE);
        sdrfTable.setShowBackgroundComponent(false);
        sdrfTable.setCanReorderFields(false);
		
        
        filterStack = new HStack();
        filterStack.setHeight(40);
		
		
		IButton editColumns = new IButton("Edit Columns");  
        editColumns.setTop(250);  
        editColumns.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(columnWindow==null){
					createNewColumnEditorWindow();	
				}else{
					columnWindow.show();
				}
			}
		});
        
        
        //For adding columns
        //sdrfTable.startEditingNew(); 
        
		IButton button2 = new IButton("Add New Column");  
        button2.setTop(250);  
        button2.addClickHandler(new ClickHandler() {  
            public void onClick(ClickEvent event) {
            	ListGridField[] temp= new ListGridField[sdrfTable.getAllFields().length+1];
            	for(int i =0;i<sdrfTable.getAllFields().length;i++){
            		temp[i]=sdrfTable.getAllFields()[i];
            	}
            	temp[temp.length-1]=(new ListGridField((""+temp.length+1),"Title"));
            	//TODO Ask user what title it will be called. 
            	// Also, find a way to simplify where to place the column
            	
            	sdrfTable.setFields(temp);

            }  
        });  
        
        IButton nameButton = new IButton("\"Name\"");  
    	nameButton.setLeft(0);  
    	nameButton.addClickHandler(new ClickHandler() {  
        public void onClick(ClickEvent event) {  
        	sdrfTable.setFieldTitle("Hello", "Name");  
        	}  
    	});
    

		
    	addItem(editColumns);
		addItem(sdrfTable);
		addItem(filterStack);
		
        //addItem(nameButton); 
		//addItem(button);
		addItem(button2);
		//addItem(orderOfColumns);
		
		
	}
	public void handleJSONObject(JSONObject jsonObject) {
		
		JSONArray jsonArray = jsonObject.get("sdrfArray").isArray();
		
		//Populate Datasource
		DataSource data = new DataSource("sdrf_ds");		
		data.setFields(JSONToDataSourceField(jsonArray)); // Need this to do filtering (Limited to filtering of input data)
		data.setTestData(JSONToListGridRecord(jsonArray));
		data.setClientOnly(true);
		
		
		//Set order of the fields and load data
		sdrfTable.setDataSource(data);
		sdrfTable.setFields(JSONToListGridField(jsonArray));
		sdrfTable.fetchData();
		
		//Build a filter
    	filterBuilder = new FilterBuilder();
		filterBuilder.setDataSource(sdrfTable.getDataSource());
		IButton filterButton = new IButton("Filter");
		filterButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				sdrfTable.filterData(filterBuilder.getCriteria());
			}
		});
		filterStack.addMember(filterBuilder);
		filterStack.addMember(filterButton);
		
		
		DynamicForm form = new DynamicForm();
		form.setNumCols(4);
		columnChooserCombobox = new ComboBoxItem("Column");  
		columnChooserCombobox.setTitle("Column");  
		updateColumnsInComboBox();
		
		cellValueTextItem = new TextItem();
		cellValueTextItem.setTitle("Value");
		form.setItems(columnChooserCombobox,cellValueTextItem);
		
		
		
		
    	IButton orderOfColumns = new IButton("Set Value In Visible Columns");  
    	orderOfColumns.setLeft(0);  
    	orderOfColumns.addClickHandler(new ClickHandler() {  
    		public void onClick(ClickEvent event) {  
	//        	for(ListGridField field:sdrfTable.getAllFields()){
	//        		System.out.print(field.getTitle()+"\t");
	//        	}
    			//This updates based on what is visible
    			for(ListGridRecord record:sdrfTable.getRecords()){
    				String columnName = columnChooserCombobox.getValue().toString();
    				record.setAttribute(columnName, cellValueTextItem.getValue().toString());
    				sdrfTable.updateData(record);
    			}
    			sdrfTable.saveAllEdits();
    	
    			
//    			sdrfTable.getRecord(0).setAttribute("1", "Happy");
//    			sdrfTable.saveAllEdits();
//    			sdrfTable.updateData(sdrfTable.getRecord(0));
    		}
    	});
    	
    	
    	filterStack.addMember(form);
    	filterStack.addMember(orderOfColumns);
	}
	private void createNewColumnEditorWindow() {
		if(sdrfTable.getAllFields().length==0){
			return;
		}
		//Create New Window
		columnWindow = new Window();
		columnWindow.setTitle("Customize SDRF Columns");
		columnWindow.setWidth(600);
		columnWindow.setHeight(400);
		columnWindow.centerInPage();
		columnWindow.show();
		
		//Available Fields that the user can select from
		final ListGrid curratorGrid = new ListGrid();
		curratorGrid.setVisible(true);
		curratorGrid.setWidth("45%");
		curratorGrid.setHeight("95%");
		curratorGrid.setLayoutAlign(VerticalAlignment.CENTER);
		curratorGrid.setCanAcceptDrop(true);
		curratorGrid.setCanAcceptDroppedRecords(true);
		curratorGrid.setCanDragRecordsOut(true);
		curratorGrid.setCanReorderRecords(true);  
	
		
		//Fields that are visible to the user
		final ListGrid activeFields = new ListGrid();
		activeFields.setVisible(true);
		activeFields.setWidth("45%");
		activeFields.setHeight("95%");
		activeFields.setLayoutAlign(VerticalAlignment.CENTER);
		activeFields.setCanAcceptDrop(true);
		activeFields.setCanAcceptDroppedRecords(true);
		activeFields.setCanDragRecordsOut(true);
		activeFields.setCanReorderRecords(true);  
		activeFields.setCanSort(true);
		
		
		ListGridField curatorAddedKey= new ListGridField("key","Key");
		ListGridField curratorAddedField = new ListGridField("title","New Columns");
		curatorAddedKey.setHidden(true);
		curratorGrid.setFields(curatorAddedKey,curratorAddedField);
		
		
		ListGridField visibleKey= new ListGridField("key","Key");
		ListGridField visibleField = new ListGridField("title","Visible Column");
		visibleKey.setHidden(true);
		activeFields.setFields(visibleKey,visibleField);
		
		//Get the fields in the table and make them records. This allows 
		//users to reorder them vertically instead of horizontally with lots of scrolling
		activeFields.setData(convertFieldsToRecords());
		
		
		IButton addButton = new IButton();
		addButton.setWidth(24);
		addButton.setHeight(24);
		addButton.setShowRollOver(true);
		addButton.setIcon("[SKIN]actions/add.png");
		
		
		//Columns can only be shifted through this GUI.
		
		addButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				
				int uniqueKey = getNewUniqueKey();
				ListGridRecord newColumn = new ListGridRecord();
				newColumn.setAttribute("key",uniqueKey);
				newColumn.setAttribute("title", "New Column"+uniqueKey);
				curratorGrid.addData(newColumn);
				
			}
		});
		
		
		TransferImgButton leftArrow = new TransferImgButton(TransferImgButton.LEFT);
		leftArrow.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				activeFields.transferSelectedData(curratorGrid);
			}
		});
		
		TransferImgButton rightArrow = new TransferImgButton(TransferImgButton.RIGHT);
		rightArrow.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				curratorGrid.transferSelectedData(activeFields);
			}
		});
				
		
		
		VStack arrows = new VStack();
		arrows.setWidth(20);
		arrows.setAlign(Alignment.CENTER);
		arrows.addMember(addButton);
		arrows.addMember(leftArrow);
		arrows.addMember(rightArrow);
		
		
		//Make a new button to confirm changes
		IButton save = new IButton("Save");  
    	save.setLeft(0);  
    	save.addClickHandler(new ClickHandler() {  
    		public void onClick(ClickEvent event) {  
    			Record[] activeFieldArray = activeFields.getRecords();
    			ListGridField[] newActiveColumns = new ListGridField[activeFieldArray.length+1];
    			newActiveColumns[0]=new ListGridField("key","Key");
    			for(int i =0;i<activeFieldArray.length;i++){
    				newActiveColumns[i+1]=new ListGridField(	activeFieldArray[i].getAttribute("key"),
    														activeFieldArray[i].getAttribute("title"));
    				newActiveColumns[i+1].setAutoFitWidthApproach(AutoFitWidthApproach.BOTH);
    				newActiveColumns[i+1].setAutoFitWidth(true);
    			}
    			sdrfTable.setFields(newActiveColumns);
    			
    			updateColumnsInComboBox();
    			//TODO
    			//Change column in the field
    			columnWindow.hide();
    		}
    	});
    	
    	//Make a new button to discard changes
		IButton cancel = new IButton("Cancel");  
		cancel.setLeft(0);  
    	cancel.addClickHandler(new ClickHandler() {  
    		public void onClick(ClickEvent event) {
    			//discard changes... restore the original
    			
    			//DON'T USE
    			//activeFields.setData(convertFieldsToRecords());
    			
    			//Probably want to create a new popup instead
    			columnWindow.destroy();
    			columnWindow=null;
    		}
    	});
		
		HStack actionButtons = new HStack();
		actionButtons.setAlign(Alignment.RIGHT);
		actionButtons.setHeight(20);
		actionButtons.addMember(save);
		actionButtons.addMember(cancel);
		
		
		HStack hStack = new HStack();
		hStack.setAlign(Alignment.CENTER);
		hStack.setAlign(VerticalAlignment.CENTER);
		hStack.addMember(curratorGrid);
		hStack.addMember(arrows);
		hStack.addMember(activeFields);
		
		
		columnWindow.addItem(hStack);
		columnWindow.addItem(actionButtons);		
	}
	
	/**
	 * Updates the combo box. Should be called everytime the table changes
	 */
	private void updateColumnsInComboBox() {
		LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();  
		for(ListGridField field:sdrfTable.getAllFields()){
			valueMap.put(field.getName(), field.getTitle());
		}
		columnChooserCombobox.setValueMap(valueMap);

	}
	
	/**
	 * Gets an array of Fields which is retrieved from the sdrfTable.
	 * The order of the array determines the order of the columns.
	 * 
	 * For each listGridRecord, there is a key and title attribute.
	 * The key attribute uniquely identifies the column and is used to retrieve
	 * the data
	 * 
	 * The title attribute is the name of the column, such as "Term Source REF"
	 * 
	 * <br>[0] (KEY=1)...(TITLE= Term Source)
	 * <br>[1] (KEY=20)...(TITLE= Comment)
	 * <br>[2] (Key=3)...(Title= Sample)
	 *  
	 * @return an array of ListGridRecords, which describes the order of the columns
	 */
	private ListGridRecord[] convertFieldsToRecords(){
		ListGridRecord[] columnsArray= new ListGridRecord[sdrfTable.getAllFields().length-1];
		int i =0;
		for(ListGridField field:sdrfTable.getAllFields()){
			if(i!=0){
				columnsArray[i-1]=new ListGridRecord();
				columnsArray[i-1].setAttribute("title", field.getTitle());
				columnsArray[i-1].setAttribute("key", field.getName());

			}
			i++;	
		}
		return columnsArray;
	}
	
	/**
	 * Converts a JSON array into an array of ListGridRecords.
	 * 
	 * Each attribute is retrieved through it's unique key assigned.
	 * Fields also have the unique key and that is how the relationship is preserved
	 * 
	 * @param jsonArrayOfRows
	 * @return an array of ListGridRecords are returned. This is used to populate the data
	 * source but it does not determine the order of the columns
	 */
	private ListGridRecord[] JSONToListGridRecord(JSONArray jsonArrayOfRows){
		//Number of rows is one less because row zero contains data about the field name.
		int numberOfRows=jsonArrayOfRows.size();
		
		ListGridRecord[] arrayOfRecords = new ListGridRecord[numberOfRows-1];
		
		//------------------------------
		//	key	1	2	3	...
		//	0	1	2	3	...
		//	1	1	2	3	...
		//------------------------------
		//For each row in the JSON array, parse it, create a new record, and add it to the array of records.
		for(int i =1;i<numberOfRows;i++){
			//The First Row is not a record, it is a field
			JSONArray row= jsonArrayOfRows.get(i).isArray();

			ListGridRecord newRecord = new ListGridRecord();
			for(int j=0;j<row.size();j++){
				newRecord.setAttribute((j+1)+"", row.get(j).isString().stringValue());
			}
			newRecord.setAttribute("key", i);
			arrayOfRecords[i-1]=newRecord;
		}
		return arrayOfRecords;
	}
	/**
	 * @param jsonArray
	 * @return an array of DataSourceFields are returned. This is used to populate the
	 * data source and is not used to determine the order of the columns
	 */
	private DataSourceField[] JSONToDataSourceField(JSONArray jsonArray) {
		JSONArray firstRow = jsonArray.get(0).isArray();
		// Plus one is to put the key in the front
		DataSourceField[] arrayOfFields = new DataSourceField[firstRow.size()+1];
		
		for(int i=0;i<firstRow.size();i++){
			arrayOfFields[i+1]= new DataSourceField(i+1+"",FieldType.TEXT,firstRow.get(i).isString().stringValue());
		}
		
		//Make a primary key
		DataSourceField key = new DataSourceField("key",FieldType.INTEGER ,"Key");
		key.setPrimaryKey(true);
		arrayOfFields[0]=key;
		return arrayOfFields; 
	}
	/**
	 * @param array
	 * @return an array of ListGridField's are returned. 
	 * This array is used to determine the order of the columns. 
	 * 
	 * <br>[ArrayIndex]uniquekey...(name)
	 * <br>[0]key...( Key)
	 * <br>[1]1...(Term Source)
	 * <br>[2]26...(Comment)
	 * <br>[3]2...(Sample Name)
	 * <br>etc.
	 */
	private ListGridField[] JSONToListGridField(JSONArray array){
		JSONArray firstRow = array.get(0).isArray();
		// Plus one is to put the key in the front
		ListGridField[] arrayOfFields = new ListGridField[firstRow.size()+1];
		
		for(int i=0;i<firstRow.size();i++){
			arrayOfFields[i+1]= new ListGridField(i+1+"",firstRow.get(i).isString().stringValue());
			arrayOfFields[i+1].setAutoFitWidthApproach(AutoFitWidthApproach.BOTH);
			arrayOfFields[i+1].setAutoFitWidth(true);
			
		}
		//At first, the unique key count is instantiated to the number of fields present
		uniqueKeyCount=firstRow.size();
		
		//Make a primary key
		ListGridField key = new ListGridField("key","Key");
		key.setAutoFitWidthApproach(AutoFitWidthApproach.BOTH);
		key.setAutoFitWidth(true);
		arrayOfFields[0]=key;
		return arrayOfFields; 
	}
	private int getNewUniqueKey(){
		uniqueKeyCount++;
		return uniqueKeyCount;
	}
}
