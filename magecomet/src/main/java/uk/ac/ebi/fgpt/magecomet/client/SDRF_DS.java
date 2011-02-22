package uk.ac.ebi.fgpt.magecomet.client;

import com.google.gwt.json.client.JSONArray;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.types.FieldType;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class SDRF_DS extends DataSource{
	private int numberOfColumns;
	private int numberOfRows;
	private DataSourceField[] arrayOfFields;
	private ListGridRecord[] arrayOfRecords;
	
	public SDRF_DS(JSONArray jsonArray,String id){
		setID(id);
		parseJSONArray(jsonArray);
		
	}
	private void parseJSONArray(JSONArray jsonArrayOfRows){
		//Number of rows is one less because row zero contains data about the field name.
		numberOfRows=jsonArrayOfRows.size();
		
		arrayOfRecords = new ListGridRecord[numberOfRows-1];
		
		//For each row in the JSON array, parse it, create a new record, and add it to the array of records.
		for(int i =0;i<numberOfRows;i++){
			JSONArray row= jsonArrayOfRows.get(i).isArray();
			//For the first row, make it into an array of fields
			if(i==0){
				numberOfColumns=row.size();
				// Plus one is to put the key in the front
				arrayOfFields= new DataSourceField[numberOfColumns+1];
				for(int j = 0;j<numberOfColumns;j++){
					arrayOfFields[j+1]= new DataSourceField(j+1+"",FieldType.TEXT,row.get(j).isString().stringValue());
				}
			}else{
				ListGridRecord newRecord = new ListGridRecord();
				for(int j=0;j<row.size();j++){
					newRecord.setAttribute((j+1)+"", row.get(j).isString().stringValue());
				}
				newRecord.setAttribute("0", i);
				arrayOfRecords[i-1]=newRecord;
			}
		}
		
		
		//Make a primary key
		DataSourceField key = new DataSourceField("0",FieldType.INTEGER,"Key");
		key.setPrimaryKey(true);
		arrayOfFields[0]=key;
		setFields(arrayOfFields);
		
		setTestData(arrayOfRecords);
		setClientOnly(true);
	}
}
