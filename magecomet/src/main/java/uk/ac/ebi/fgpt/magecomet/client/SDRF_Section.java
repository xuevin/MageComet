 package uk.ac.ebi.fgpt.magecomet.client;


import java.util.LinkedHashMap;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.types.AutoFitWidthApproach;
import com.smartgwt.client.types.DragDataAction;
import com.smartgwt.client.types.FieldType;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.IButton;
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

public class SDRF_Section extends SectionStackSection{
	private final ListGrid sdrfTable = new ListGrid();;
	private final FilterBuilder filterBuilder  = new FilterBuilder(); 
	private final ComboBoxItem columnChooserCombobox = new ComboBoxItem("Column");
	private final HStack filterStack = new HStack();
	private TextItem cellValueTextItem;
	private SDRF_Section_ColumnEditor columnWindow;
	private int numColumnsBeforeModification;
	

	
	
	public SDRF_Section(){
		super("SDRF");
		numColumnsBeforeModification=0;
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
        sdrfTable.setCanReorderFields(true);
		
        
        filterStack.setHeight(40);
		
		
		IButton editColumns = new IButton("Edit Columns");  
        editColumns.setTop(250);  
        editColumns.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(columnWindow==null){
					//If there are no fields, it does not continue,
					//Must load data first
					if(sdrfTable.getAllFields().length==0){
						return;
					}else{
						columnWindow = new SDRF_Section_ColumnEditor(sdrfTable,columnChooserCombobox,numColumnsBeforeModification);
						//Create New Window
						columnWindow.setWidth(600);
						columnWindow.setHeight(400);
						columnWindow.centerInPage();
						columnWindow.show();
					}
				}else{
					columnWindow.updateColumns();
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
		numColumnsBeforeModification=firstRow.size();
		
		//Make a primary key
		ListGridField key = new ListGridField("key","Key");
		key.setAutoFitWidthApproach(AutoFitWidthApproach.BOTH);
		key.setAutoFitWidth(true);
		arrayOfFields[0]=key;
		return arrayOfFields; 
	}
	/**
	 * Updates the combo box. Should be called everytime the table changes
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
