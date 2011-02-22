package uk.ac.ebi.fgpt.magecomet.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridEditEvent;
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
import com.smartgwt.client.widgets.layout.SectionStackSection;

public class IDF_Section extends SectionStackSection{
	
	private ArrayList<ListGridRecord> rows;
	private ArrayList<String> listOfAvailableFields;
	private ListGrid idfGrid;

	public IDF_Section(){
		super("IDF");
		idfGrid = new ListGrid();
		idfGrid.setCanEdit(true);
		idfGrid.setEditEvent(ListGridEditEvent.CLICK);
		idfGrid.setCanSort(false);
		rows = new ArrayList<ListGridRecord>();
		listOfAvailableFields = new ArrayList<String>();
		addItem(idfGrid);
		
		IButton editFieldsButton = new IButton("Edit Fields");
		editFieldsButton.setLeft(0);
		editFieldsButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Window editRowWindow = new Window();
				editRowWindow.setTitle("Customize IDF Fields");
				editRowWindow.setWidth(600);
				editRowWindow.setHeight(400);
				editRowWindow.centerInPage();
				editRowWindow.show();
				
				
				
				
				//Available Fields that the user can select from
				final ListGrid availableFields = new ListGrid();
				availableFields.setVisible(true);
				availableFields.setWidth("45%");
				availableFields.setHeight("95%");
				availableFields.setLayoutAlign(VerticalAlignment.CENTER);
				//Fields that are visible to the user
				final ListGrid visibleFields = new ListGrid();
				visibleFields.setVisible(true);
				visibleFields.setWidth("45%");
				visibleFields.setHeight("95%");
				visibleFields.setLayoutAlign(VerticalAlignment.CENTER);
				
				
				TransferImgButton arrowImg = new TransferImgButton(TransferImgButton.RIGHT);
				arrowImg.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						availableFields.transferSelectedData(visibleFields);
					}
				});
				
				
				HStack hStack = new HStack();
				hStack.setAlign(Alignment.CENTER);
				hStack.setAlign(VerticalAlignment.CENTER);
				hStack.addMember(availableFields);
				hStack.addMember(arrowImg);
				hStack.addMember(visibleFields);
				
				
				editRowWindow.addItem(hStack);
				
				// TODO Auto-generated method stub
				
			}
		});
		
		addItem(editFieldsButton);
		
		//TODO make a HLayout for this
		

//		IButton nameButton = new IButton("Click to Print");
//		nameButton.setLeft(0);
//		nameButton.addClickHandler(new ClickHandler() {
//			public void onClick(ClickEvent event) {
//				getIDF();
//			}
//		});
		
		//addItem(nameButton); 
	}
	public void handleJSONObject(JSONObject jsonObject){
		JSONArray jsonArray = jsonObject.get("idfArray").isArray();
		ListGridRecord[] listOfRows = new ListGridRecord[jsonArray.size()];
		
		
		for(int row=0; row <jsonArray.size();row ++){
			
			JSONArray columnArray = jsonArray.get(row).isArray();
			ListGridRecord tempRow = new ListGridRecord();
			for(int column=0;column<columnArray.size();column++){
				tempRow.setAttribute(column+"", columnArray.get(column).isString().stringValue());
			}
			listOfRows[row]=tempRow;
		}
		
		//Iterate through JSON Array and find the one with the most number of columns
		int longestRow = 0;
		for(int i =0;i<jsonArray.size();i++){
			if(longestRow<jsonArray.get(i).isArray().size()){
				longestRow = jsonArray.get(i).isArray().size();
			}
		}
		ListGridField[] listOfFields = new ListGridField[longestRow];
		
		for(int i = 1; i<longestRow;i++){
			listOfFields[i] = new ListGridField((""+i),"");
		}
		listOfFields[0] = new ListGridField("0","Field"); 
		idfGrid.setFields(listOfFields);
		idfGrid.setData(listOfRows);
	}
//	public void handleJSONObject(JSONObject jsonObject){
//		IDF_Client idf = new IDF_Client(jsonObject);
//		Map<String,String> singleMaps = idf.getAllSingleMaps();
//		
//		//Iterate through the list of single valued keys and make a row (which is a listGridRecord)
//		for(String fieldName:singleMaps.keySet()){
//			ListGridRecord newRecord = new ListGridRecord();
//			newRecord.setAttribute("0", fieldName);
//			newRecord.setAttribute("1",singleMaps.get(fieldName));
//			rows.add(newRecord);
//		}
//		
//		Map<String,List<String>> multiMaps = idf.getAllMultiMaps();
//		
//		//Iterate through the list of multi valued keys and make a row (which is a listGridRecord)
//		for(String fieldName:multiMaps.keySet()){
//			ListGridRecord newRecord = new ListGridRecord();
//			newRecord.setAttribute("0", fieldName);
//			for(int i =0;i<multiMaps.get(fieldName).size();i++){
//				newRecord.setAttribute(((i+1)+""),multiMaps.get(fieldName).get(i));	
//			}
//			rows.add(newRecord);
//		}
//
//		
//		updateGrid();
////		idf.getAllMultiMaps();
//
//	}
	private void updateGrid() {
		//Get greatest number of columns
		int longestRow = 0;
		for(ListGridRecord record:rows){
			if(longestRow<record.getAttributes().length){
				longestRow=record.getAttributes().length;
			}
		}
		longestRow = longestRow-2;
		
		//Add columns
		ListGridField[] columnsArray = new ListGridField[longestRow]; 
		
		ListGridField fieldColumn = new ListGridField("0","Field");
		fieldColumn.setCanEdit(false);
		columnsArray[0]=fieldColumn;
		
		
		for(int i=1; i < longestRow; i++){
			columnsArray[i]=new ListGridField((""+i),(""+i));
		}
	
		idfGrid.setFields(columnsArray);
	
		//Very round about way to get the data into an array of ListGridRecord
		ListGridRecord[] arrayOfData = new ListGridRecord[rows.size()];
		for(int i =0;i<rows.size();i++){
			arrayOfData[i]=rows.get(i);
		}
		idfGrid.setData(arrayOfData);
		
	}
	public JSONObject getIDF(){
		//TODO -- Broken
		RecordList recordList = idfGrid.getDataAsRecordList();
		Record[] recordArray = recordList.toArray();
		for(int row =0;row<recordArray.length;row++){
			String[] rowAttribs = recordArray[row].getAttributes();
			for(int column =0;column<rowAttribs.length;column++){
				System.out.print(recordArray[row].getAttribute(column+"") + "\t");
			}
			System.out.println();
		}
		
		return null;
		
	}

}
