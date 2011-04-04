package uk.ac.ebi.fgpt.magecomet.client;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.smartgwt.client.core.DataClass;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.types.AutoFitWidthApproach;
import com.smartgwt.client.types.FieldType;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;


/**
 * This is a class used to modify a SDRF. All modifications to a SDRF should be called
 * through this class.
 * 
 * @author vincent@ebi.ac.uk
 *
 */
public class SDRF_Data {
	//These arrays must always be constantly updated.
	private ListGridRecord[] listOfAllRecords;
	private ListGridField[] listOfAllFields;
	private DataSource data;
	private int numColumnsBeforeModification;
	private int uniqueKeyCount;

	
	public SDRF_Data(JSONObject jsonObject){
		JSONArray jsonArray = jsonObject.get("sdrfArray").isArray();
		//OPT - Can do better
		listOfAllRecords = JSONToListGridRecord(jsonArray); //Might want to fix this
		listOfAllFields = JSONToListGridField(jsonArray);//Might want to fix this so that you don't do it 2x
		
		data = new DataSource("sdrf_ds");		
		data.setFields(JSONToDataSourceField(jsonArray)); // Need this to do filtering
		data.setTestData(listOfAllRecords);
		data.setClientOnly(true);
	}
	public void addAttributeToAllRecords(final String uniqueKey, final String value){
		for(ListGridRecord record:listOfAllRecords){
			record.setAttribute(uniqueKey, value);
		}
		updateDataSource();
	}
	/**
	 * Filters and finds all of the records that have the specified value. After, it adds the data
	 * into the destination column.
	 * 
	 * @param fromColumn
	 * @param destinationColumn
	 * @param value
	 */
	public void addValueToSelectedRecords(final String fromColumn, final String destinationColumn, final String value){
		for(ListGridRecord record:listOfAllRecords){
			if(record.getAttribute(fromColumn).toLowerCase().contains(value.toLowerCase())){
				record.setAttribute(destinationColumn, value);		
			}
		}
		updateDataSource();
	}
	public String addNewColumn_Characteristic_AndGetKey(String title){
		int uniqueKey = getNewUniqueKey();
		ListGridField newColumn = new ListGridField();
		newColumn.setName(uniqueKey+"");
		newColumn.setTitle("Characteristics["+title+"]");
		newColumn.setAutoFitWidthApproach(AutoFitWidthApproach.BOTH);
		newColumn.setAutoFitWidth(true);	
		
		ListGridField[] newFields = new ListGridField[listOfAllFields.length+1];
		
		//Find the right place to put this!
		int offset = 0;
		for(int i = 0;i<listOfAllFields.length;i++){
			newFields[i+offset]=listOfAllFields[i];
			if(listOfAllFields[i].getTitle().contains("Source Name")){
				offset=1;
				newFields[i+1]=newColumn;
			}
		}
		listOfAllFields=newFields;
		updateDataSource();
		return uniqueKey+"";
	}
	public String addNewColumn_FactorValue_AndGetKey(String title){
		int uniqueKey = getNewUniqueKey();
		ListGridField newColumn = new ListGridField();
		newColumn.setName(uniqueKey+"");
		newColumn.setTitle("Factor Value["+title+"]");
		newColumn.setAutoFitWidthApproach(AutoFitWidthApproach.BOTH);
		newColumn.setAutoFitWidth(true);	

		ListGridField[] newFields = new ListGridField[listOfAllFields.length+1];
		
		//Find the right place to put this!
		for(int i = 0;i<listOfAllFields.length;i++){
			newFields[i]=listOfAllFields[i];
		}
		newFields[listOfAllFields.length]=newColumn;
		
		listOfAllFields=newFields;
		updateDataSource();
		return uniqueKey+"";
	}
	public void updateColumnNames(ListGridField[] newArrayOfListGridFields) {
		listOfAllFields=newArrayOfListGridFields;
		updateDataSource();
	}
	/**
	 * Updates the DataSource to reflect the new columns added
	 * Because the data is edited via the listOfAllRecords, there is no worries about making 
	 * a new data source.  
	 * @param newListGridFields A new array of ListGridFields
	 */
	public void updateDataSource(ListGridField[] newListGridFields){
		listOfAllFields=newListGridFields;
		updateDataSource();
		
//		sdrfTable.setDataSource(data);
//		guiMediator.passDataToFilterTab(sdrfTable);
//		sdrfTable.setFields(newListGridFields);
	}
	public ListGridField[] getAllFields() {
		return listOfAllFields;
	}
	public String getNewColumnKey(){
		int uniqueKey = getNewUniqueKey();
		return uniqueKey+"";
	}
	public String getString(){
		
		String sdrfAsString ="";
		if(listOfAllFields.length!=0){
			
			//Print out all fields
			for(ListGridField column:listOfAllFields){
				if(!column.getTitle().equals("Key")){
					sdrfAsString+=(column.getTitle()+"\t");
				}
			}
			sdrfAsString=sdrfAsString.substring(0, sdrfAsString.length()-1);//Remove last tab and make it a new line
			sdrfAsString+="\n";
	
			for(DataClass record:data.getTestData()){
				for(ListGridField column:listOfAllFields){
					if(!column.getTitle().equals("Key")){
						
						if(record.getAttribute(column.getName())==null){
							sdrfAsString+="\t";
						}else{
							sdrfAsString+=record.getAttribute(column.getName())+"\t";
						}
					}
				}
				
				sdrfAsString=sdrfAsString.substring(0, sdrfAsString.length()-1);//Remove last tab and make it a new line
				sdrfAsString+="\n";
			}
			sdrfAsString=sdrfAsString.trim();//Remove last new line
			
			return sdrfAsString;
		}
		return "";
	}
//public String getString(){
//		
//		StringBuilder sdrfStringBuilder = new StringBuilder();
//		
//		if(listOfAllFields.length!=0){
//			
//			//Print out all fields
//			for(ListGridField column:listOfAllFields){
//				if(!column.getTitle().equals("Key")){
//					sdrfStringBuilder.append(column.getTitle());
//					sdrfStringBuilder.append("\t");
//				}
//			}
//			sdrfStringBuilder.deleteCharAt(sdrfStringBuilder.length()-1);
//			sdrfStringBuilder.append("\n");
//	
//			for (int j = 0; j < data.getTestData().length; j++) {
//				DataClass record = data.getTestData()[j];
//				for (int i = 0; i < listOfAllFields.length; i++) {
//					ListGridField column = listOfAllFields[i];
//					// skip the key
//					if (!column.getTitle().equals("Key")) {
//						if (record.getAttribute(column.getName()) == null) {
//							sdrfStringBuilder.append("\t");
//						} else {
//							sdrfStringBuilder.append(record.getAttribute(column
//									.getName()));
//							sdrfStringBuilder.append("\t");
//						}
//					}
//					
//				}
//				// delete last tab
//				sdrfStringBuilder.deleteCharAt(sdrfStringBuilder.length() - 1);
//				sdrfStringBuilder.append("\n");
//			}
//			sdrfStringBuilder.deleteCharAt(sdrfStringBuilder.length() - 1);
//			return sdrfStringBuilder.toString();
//		}
//		return "";
//	}
	private void updateDataSource() {
		//Create a new datasource based on the list of records and the fields specified.
		DataSourceField[] fields = new DataSourceField[listOfAllFields.length];
		for(int i =1;i<listOfAllFields.length;i++){
			fields[i]=new DataSourceField(listOfAllFields[i].getName(),
					FieldType.TEXT,listOfAllFields[i].getTitle());
		}
		//Make a primary key
		DataSourceField key = new DataSourceField("key",FieldType.INTEGER ,"Key");
		key.setPrimaryKey(true);
		fields[0]=key;
		
		data = new DataSource();
		data.setFields(fields); // Need this to do filtering (Limited to filtering of input data)
		data.setTestData(listOfAllRecords);
		data.setClientOnly(true);		
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
		uniqueKeyCount=numColumnsBeforeModification;
		
		//Make a primary key
		ListGridField key = new ListGridField("key","Key");
		key.setAutoFitWidthApproach(AutoFitWidthApproach.BOTH);
		key.setAutoFitWidth(true);
		arrayOfFields[0]=key;
		return arrayOfFields; 
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
	@Deprecated
	public DataSource getDataSource() {
		return data;
	}
	@Deprecated
	public ListGridRecord[] getAllRecords() {
		return listOfAllRecords;
	}
	public void setValueForSelectedRecords(ListGridRecord[] listOfRecords,
			String uniqueKey, String value) {
		//This updates based on what is filtered
		for(ListGridRecord record:listOfRecords){
			record.setAttribute(uniqueKey, value);
		}
		updateDataSource();
	}
	
}
