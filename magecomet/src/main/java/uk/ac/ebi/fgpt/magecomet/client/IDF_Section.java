package uk.ac.ebi.fgpt.magecomet.client;

import java.util.ArrayList;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.TransferImgButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;

public class IDF_Section extends SectionStackSection{
	
	private ArrayList<ListGridRecord> rows;
	private ListGrid idfGrid;
	private final HTMLFlow textBox = new HTMLFlow("Experiment Description");
	private GuiMediator guiMediator;

	public IDF_Section(GuiMediator guiMediator){
		super("IDF");
		
		this.guiMediator = guiMediator;
		this.guiMediator.registerIDFSection(this);
		
		HStack hStack = new HStack();
		
		idfGrid = new ListGrid();
		idfGrid.setCanEdit(true);
		idfGrid.setEditEvent(ListGridEditEvent.DOUBLECLICK);
		idfGrid.setCanSort(false);
		idfGrid.setWidth("60%");
		idfGrid.setShowAllRecords(true);
		
		
		rows = new ArrayList<ListGridRecord>();
		
		
		
		
		textBox.setWidth("40%");
		textBox.setOverflow(Overflow.AUTO);
		textBox.setMargin(5);
		
		hStack.addMember(idfGrid);
		hStack.addMember(textBox);
		
		addItem(hStack);
		
		IButton editFieldsButton = new IButton("Edit Fields");
		editFieldsButton.setLeft(0);
		editFieldsButton.addClickHandler(new ClickHandler() {
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
				
			}
		});
	}
	public void handleJSONObject(JSONObject jsonObject){
		JSONArray jsonArray = jsonObject.get("idfArray").isArray();
		ListGridRecord[] listOfRows = new ListGridRecord[jsonArray.size()];
		
		//Get the data from the rows and make each a record
		String textBoxContents = "";
		for(int row=0; row <jsonArray.size();row ++){
			
			JSONArray columnArray = jsonArray.get(row).isArray();
			ListGridRecord tempRow = new ListGridRecord();
			for(int column=0;column<columnArray.size();column++){
				tempRow.setAttribute(column+"", columnArray.get(column).isString().stringValue());
			}
			listOfRows[row]=tempRow;
			
			
			//Find the column with experiment description
			if(columnArray.get(0).isString().stringValue().equals("Experiment Description")){
				textBoxContents=(columnArray.get(1).isString().stringValue());
			}
			//Find the column with Protocol descriptions
			if(columnArray.get(0).isString().stringValue().equals("Protocol Description")){
				int i =1;
				while(i<columnArray.size()){
					textBoxContents+="<br>=============================<br>";
					textBoxContents+=(columnArray.get(i).isString().stringValue());
					i++;
				}
			}
		}
		textBox.setContents(textBoxContents);
		
		
		//Iterate through JSON Array and find the one with the most number of columns
		int longestRow = 0;
		for(int i =0;i<jsonArray.size();i++){
			if(longestRow<jsonArray.get(i).isArray().size()){
				longestRow = jsonArray.get(i).isArray().size();
			}
		}
		
		ListGridField[] listOfFields = new ListGridField[longestRow];
		//Make the columns
		for(int i = 1; i<longestRow;i++){
			listOfFields[i] = new ListGridField((""+i),"");
			listOfFields[i].setEditorType(new TextAreaItem());  
		}
		listOfFields[0] = new ListGridField("0","Field"); 
		listOfFields[0].setWidth(240);
		
		idfGrid.setFields(listOfFields);
		idfGrid.setData(listOfRows);
	}
	public String getString(){
		String isfAsString ="";
		if(idfGrid.getFields().length!=0){
			
//			//Print out all fields
			ListGridField[] listOfFields = idfGrid.getAllFields();
//			for(ListGridField column:listOfFields){
//				if(!column.getTitle().equals("Key")){
//					isfAsString+=column.getTitle()+"\t";
//				}
//				isfAsString.replaceAll("\\s+$", "");//Remove last tab
//			}
//			isfAsString+="\n";

//			for(DataClass record:idfGrid.getDataSource().getTestData()){
			for(ListGridRecord record:idfGrid.getRecords()){
				for(ListGridField column:listOfFields){
//					if(!column.getTitle().equals("Key")){
						if(record.getAttribute(column.getName())==null){
							isfAsString+="\t";
						}else{
							isfAsString+=record.getAttribute(column.getName())+"\t";
						}
//					}
				}
				isfAsString=isfAsString.trim();//Remove last tab and make it a new line
				isfAsString+="\n";
			}
			isfAsString=isfAsString.trim();//Remove last new line
			
			return isfAsString;
		}
		return "";
		
	}
}
