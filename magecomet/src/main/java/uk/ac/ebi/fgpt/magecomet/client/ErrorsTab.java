package uk.ac.ebi.fgpt.magecomet.client;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.types.FieldType;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.tab.Tab;

public class ErrorsTab extends Tab{

	GuiMediator guiMediator;
	private final ListGrid errorsListGrid = new ListGrid();;
	public ErrorsTab(GuiMediator guiMediator){
		super("Errors");
		this.guiMediator=guiMediator;
		this.guiMediator.registerErrorsTab(this);
		setIcon("[SKIN]actions/exclamation.png");
		
		ListGridField codeField = new ListGridField("code","Code");
		ListGridField typeField = new ListGridField("type","Type");
		ListGridField messageField = new ListGridField("message","Message");
		ListGridField lineField = new ListGridField("line","Line");
		ListGridField columnField = new ListGridField("column","Column");
		errorsListGrid.setFields(codeField,typeField,messageField,lineField,columnField);
		
		setPane(errorsListGrid);
		
	}
	public void handelJSONArrayOfErrors(JSONObject object){
		JSONArray errors = object.get("error").isArray();
		if(errors==null){
			return;
		}

		int size = errors.size();
		ListGridRecord[] records= new ListGridRecord[size];
		
		for(int i=0;i<size;i++){
			ListGridRecord newRecord = new ListGridRecord();
			JSONObject errorItem = errors.get(i).isObject();
			
			newRecord.setAttribute("code",errorItem.get("code").isString().stringValue());
			newRecord.setAttribute("type",errorItem.get("type").isString().stringValue());
			newRecord.setAttribute("message",errorItem.get("message").isString().stringValue());
			newRecord.setAttribute("line",errorItem.get("line").isString().stringValue());
			newRecord.setAttribute("column",errorItem.get("column").isString().stringValue());
			records[i]=newRecord;
		}
		
		DataSource data = new DataSource();
		DataSourceField codeField = new DataSourceField("code",FieldType.TEXT,"Code");
		DataSourceField typeField = new DataSourceField("type",FieldType.TEXT,"Type");
		DataSourceField messageField = new DataSourceField("message",FieldType.TEXT,"Message");
		DataSourceField lineField = new DataSourceField("line",FieldType.TEXT,"Line");
		DataSourceField columnField = new DataSourceField("column",FieldType.TEXT,"Column");
		data.setFields(codeField,typeField,messageField,lineField,columnField);
		
		
		data.setTestData(records);
		data.setClientOnly(true);
		
		
		errorsListGrid.setDataSource(data);
		errorsListGrid.fetchData();
	}
}
