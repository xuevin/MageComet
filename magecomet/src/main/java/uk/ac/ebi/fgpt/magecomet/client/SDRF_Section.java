 package uk.ac.ebi.fgpt.magecomet.client;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.smartgwt.client.core.DataClass;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.types.AutoFitWidthApproach;
import com.smartgwt.client.types.FieldType;
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

public class SDRF_Section extends SectionStackSection implements MageTabFile{
	private final ListGrid sdrfTable = new ListGrid();;
	private final TabSet automaticFunctionEditor = new TabSet();
	private final IButton editColumnsButton = new IButton("Edit Columns");  

	/*
	 * Used to modify all of the records even after filtering
	 * If you plan to add/remove anything to the grid, you must edit this list
	 */
	private ListGridRecord[] listOfAllRecords;
	
	private SDRF_Section_ColumnEditor columnEditorWindow;
	
	private int numColumnsBeforeModification;
	
	private GuiMediator guiMediator;
	
	public SDRF_Section(final GuiMediator guiMediator){
		super("SDRF");
		//Mediator Actions
		this.guiMediator = guiMediator;
		this.guiMediator.registerSDRFSection(this);
		
		numColumnsBeforeModification=0;
		
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
				if(columnEditorWindow==null){
					//If there are no fields, it does not continue,
					//Must load data first
					if(sdrfTable.getAllFields().length==0){
						return;
					}else{
						//Create New Window
						columnEditorWindow = new SDRF_Section_ColumnEditor(sdrfTable,numColumnsBeforeModification,guiMediator);
						columnEditorWindow.setWidth(600);
						columnEditorWindow.setHeight(400);
						columnEditorWindow.centerInPage();
						columnEditorWindow.show();
					}
				}else{
					columnEditorWindow.updateColumns();
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
	public void handleJSONObject(JSONObject jsonObject) {
		JSONArray jsonArray = jsonObject.get("sdrfArray").isArray();
		listOfAllRecords = JSONToListGridRecord(jsonArray);
		
		//Populate Datasource
		DataSource data = new DataSource("sdrf_ds");		
		data.setFields(JSONToDataSourceField(jsonArray)); // Need this to do filtering (Limited to filtering of input data)
		data.setTestData(listOfAllRecords);
		data.setClientOnly(true);
		
		//Set order of the fields and load data
		sdrfTable.setDataSource(data);
		sdrfTable.setFields(JSONToListGridField(jsonArray));
		sdrfTable.fetchData();
		
		//Pass data to FilterTab
		guiMediator.passDataToFilterTab(sdrfTable);
		
		//Pass all fields to Extract Tab
		guiMediator.passAllRecordsToExtractTab(listOfAllRecords,sdrfTable);
		
    	//Create New Column Editor Window
		columnEditorWindow = new SDRF_Section_ColumnEditor(sdrfTable,numColumnsBeforeModification,guiMediator);
		columnEditorWindow.setWidth(600);
		columnEditorWindow.setHeight(400);
		columnEditorWindow.centerInPage();
    	
	}
	
	public String getString(){
		String sdrfAsString ="";
		if(sdrfTable.getFields().length!=0){
			
			//Print out all fields
			ListGridField[] listOfFields = sdrfTable.getAllFields();
			for(ListGridField column:listOfFields){
				if(!column.getTitle().equals("Key")){
					sdrfAsString+=column.getTitle()+"\t";
				}
				sdrfAsString.replaceAll("\\s+$", "");//Remove last tab
			}
			sdrfAsString+="\n";

			for(DataClass record:sdrfTable.getDataSource().getTestData()){
				for(ListGridField column:listOfFields){
					if(!column.getTitle().equals("Key")){
						
						if(record.getAttribute(column.getName())==null){
							sdrfAsString+="\t";
						}else{
							sdrfAsString+=record.getAttribute(column.getName())+"\t";
						}
					}
				}
				sdrfAsString=sdrfAsString.trim();//Remove last tab and make it a new line
				sdrfAsString+="\n";
			}
			sdrfAsString=sdrfAsString.trim();//Remove last new line
			
			return sdrfAsString;
		}
		return "";
		
	}
	public void addAttributeToAllRecords(final String uniqueKey, final String value){
		for(ListGridRecord record:listOfAllRecords){
			record.setAttribute(uniqueKey, value);
			sdrfTable.updateData(record);
		}
		sdrfTable.saveAllEdits();	
	}
	public void addAttributeToSelectedRecords(final String fromColumn, final String destinationColumn, final String efoTerm){
		for(ListGridRecord record:listOfAllRecords){
			if(record.getAttribute(fromColumn).contains(efoTerm)){
				record.setAttribute(destinationColumn, efoTerm);	
			}
			sdrfTable.updateData(record);
		}
		sdrfTable.saveAllEdits();
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
			//Should the column be hidden? If true hide, else set it to auto width
			if(GlobalConfigs.shouldExclude(arrayOfFields[i+1].getAttribute("title"))){
				arrayOfFields[i+1].setHidden(true);
			}else{
				arrayOfFields[i+1].setAutoFitWidthApproach(AutoFitWidthApproach.BOTH);
				arrayOfFields[i+1].setAutoFitWidth(true);
			}
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
	 * Updates the DataSource to reflect the new columns added
	 * Because the data is edited via the listOfAllRecords, there is no worries about making 
	 * a new data source.  
	 * @param newListGridFields A new array of ListGridFields
	 */
	public void updateDataSource(ListGridField[] newListGridFields){
		//Create a new datasource based on the list of records and the fields specified.
		DataSource data = new DataSource("sdrf_ds");
		DataSourceField[] fields = new DataSourceField[newListGridFields.length];
		for(int i =1;i<newListGridFields.length;i++){
			fields[i]=new DataSourceField(newListGridFields[i].getName(),
					FieldType.TEXT,newListGridFields[i].getTitle());
		}
		//Make a primary key
		DataSourceField key = new DataSourceField("key",FieldType.INTEGER ,"Key");
		key.setPrimaryKey(true);
		fields[0]=key;
		
		data.setFields(fields); // Need this to do filtering (Limited to filtering of input data)
		data.setTestData(listOfAllRecords);
		data.setClientOnly(true);
		
		
		sdrfTable.setDataSource(data);
		guiMediator.passDataToFilterTab(sdrfTable);
		sdrfTable.setFields(newListGridFields);
	}
	public void refreshTable() {
		sdrfTable.fetchData();	
	}
}
