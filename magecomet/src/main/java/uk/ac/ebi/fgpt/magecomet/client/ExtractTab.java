package uk.ac.ebi.fgpt.magecomet.client;

import java.util.LinkedHashMap;

import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.KeyPressEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyPressHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.VStack;
import com.smartgwt.client.widgets.tab.Tab;

public class ExtractTab extends Tab{
	private final DynamicForm form = new DynamicForm();
	private final HStack hstack = new HStack();
	private final VStack vstack = new VStack();
	private final ComboBoxItem columnComboBoxItem = new ComboBoxItem("column");
	private final IButton submit = new IButton();
	private final ComboBoxItem newColumn = new ComboBoxItem();
    private final TextItem leftInput = new TextItem();
	private final TextItem rightInput = new TextItem();
	private final HTMLFlow sampleOutput = new HTMLFlow();
	private final ComboBoxItem destinationComboBoxItem = new ComboBoxItem("type");
	private GuiMediator guiMediator;


	public ExtractTab(GuiMediator guiMediator) {
		this.guiMediator=guiMediator;
		this.guiMediator.registerExtractTab(this);
		
		setTitle("Extract");
		
		vstack.setHeight(40);
		hstack.setHeight(16);
		hstack.setDefaultLayoutAlign(VerticalAlignment.CENTER);

		HTMLFlow directions = new HTMLFlow();
		directions.setHeight(20);
		directions.setContents("Input the characters surrounding the the value that will be extracted into a new column.<br>" +
				"To indicate the beginning of the row use \"^\" and to indicate the end of a row use \"$\"");
		

		//*****************************
		// Form
		//*****************************
		
		form.setNumCols(10);
		
		columnComboBoxItem.setTitle("From");
		columnComboBoxItem.setRequired(true);
		
        leftInput.setTitle("Left");
        leftInput.setRequired(true);
		rightInput.setTitle("Right");
		rightInput.setRequired(true);
	
		sampleOutput.setHeight(10);
		sampleOutput.setContents("Sample Extract: ");
		
		newColumn.setTitle("New Column");
		newColumn.setWrapTitle(false);
		newColumn.setRequired(true);
		newColumn.setShowPickerIcon(false);

		
		destinationComboBoxItem.setTitle("Type");
		LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
		valueMap.put("clipboard", "Clipboard");
		valueMap.put("characteristic", "Characteristic");
		valueMap.put("factorvalue", "Factor Value");
		valueMap.put("both", "Both");	        
		destinationComboBoxItem.setValueMap(valueMap);
		destinationComboBoxItem.addChangeHandler(new ChangeHandler() {  
	            public void onChange(ChangeEvent event) {  
	                String selectedItem = destinationComboBoxItem.getDisplayValue();
	                
	                if(selectedItem.equals("characteristic")){
	                	newColumn.setValueMap(GlobalConfigs.getCommonCharacteristics());	
	                }else if(selectedItem.equals("factorvalue")){
	                	newColumn.setValueMap(GlobalConfigs.getCommonFactors());	
	                }else{
	                	newColumn.setValueMap(GlobalConfigs.getCommonFactors());
	                }
	            }  
	        });  
		
		
	
		submit.setTitle("Extract");
		
	
		
		//*****************************
		// Layout
		//*****************************
		form.setItems(columnComboBoxItem,leftInput,rightInput,destinationComboBoxItem,newColumn);

		
		hstack.addMember(form);
		hstack.addMember(submit);
		

		vstack.addMember(directions);
		vstack.addMember(hstack);
		vstack.setMembersMargin(10);
		vstack.addMember(sampleOutput);
		setPane(vstack);
	}
	public void updateColumnsInComboBox(LinkedHashMap<String, String> valueMap) {
		if(columnComboBoxItem!=null){
			columnComboBoxItem.setValueMap(valueMap);	
		}
	}
	public void setRecords(final ListGridRecord[] listGridRecords,final ListGrid sdrfTable) {
		ChangedHandler inputChanged = new ChangedHandler() {
			public void onChanged(ChangedEvent event) {
				updateSampleOutput(listGridRecords);
			}
		};		
		ClickHandler addToColumnEditor = new ClickHandler() {
			public void onClick(ClickEvent event) {
				submitForm(sdrfTable,listGridRecords);
			}
		};
		newColumn.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				if(event.getKeyName().equals("Enter")){
					submitForm(sdrfTable,listGridRecords);
				}
			}
		});
		leftInput.addChangedHandler(inputChanged);
		rightInput.addChangedHandler(inputChanged);
		submit.addClickHandler(addToColumnEditor);
	}
	private void submitForm(final ListGrid sdrfTable, final ListGridRecord[] listOfAllRecords){
		if(form.validate()){
			//TODO add extra validation to make sure that column names are spelled correctly
			if(destinationComboBoxItem.getValue().equals("clipboard")){
				extractToColumnName(sdrfTable,listOfAllRecords,
						guiMediator.addColumnToClipboard(newColumn.getValueAsString()));
			}else if (destinationComboBoxItem.getValue().equals("factorvalue")){
				extractToColumnName(sdrfTable,listOfAllRecords,
						guiMediator.addFactorValueToActiveGrid(newColumn.getValueAsString()));
			}else if (destinationComboBoxItem.getValue().equals("characteristic")){
				extractToColumnName(sdrfTable,listOfAllRecords,
						guiMediator.addCharacteristicToActiveGrid(newColumn.getValueAsString()));
			}else if (destinationComboBoxItem.getValue().equals("both")){
				extractToColumnName(sdrfTable,listOfAllRecords,
						guiMediator.addFactorValueToActiveGrid(newColumn.getValueAsString()));
				extractToColumnName(sdrfTable,listOfAllRecords,
						guiMediator.addCharacteristicToActiveGrid(newColumn.getValueAsString()));
			}
			//Reset everything back to blanks to indicate that the extract was successful
			leftInput.clearValue();
			rightInput.clearValue();
			newColumn.clearValue();
			sampleOutput.setContents("Sample Extract:");
			guiMediator.refreshTable();
		}
	}
	private void extractToColumnName(final ListGrid sdrfTable, final ListGridRecord[] listOfAllRecords,String newColumnName){
		//For each record, add the attribute extracted
		for(ListGridRecord record:listOfAllRecords){
			
			//If ComboBoxItem is null, don't do anything;
			if(columnComboBoxItem.getValueAsString()==null ||
					record.getAttributeAsString(columnComboBoxItem.getValueAsString())==null){
				return;
			}
			String textInColumn = 
				record.getAttributeAsString(columnComboBoxItem.getValueAsString());
				record.setAttribute(newColumnName, extract(textInColumn));
			sdrfTable.updateData(record);
		}
		sdrfTable.saveAllEdits();
	}
	private void updateSampleOutput(ListGridRecord[] listOfAllRecords){
		String output="";
		//How many samples do you want to see
		int i = 10; 
		for(ListGridRecord record:listOfAllRecords){
			if(i<0){
				break;
			}
			//If ComboBoxItem is null, don't do anything;
			if(columnComboBoxItem.getValueAsString()==null ||
					record.getAttributeAsString(columnComboBoxItem.getValueAsString())==null){
				return;
			}
			String textInColumn = 
				record.getAttributeAsString(columnComboBoxItem.getValueAsString());
			
			output+="["+extract(textInColumn)+"] ";
			i--;
		}
		sampleOutput.setContents("Sample Extract: "+output);
	}
	private String extract(String input){
		String left;
		String right;
		if(leftInput.getValueAsString()!=null){
			left=translateEscapeCharacters(leftInput.getValueAsString());
		}else{
			left="";
		}
		if(rightInput.getValueAsString()!=null){
			right=translateEscapeCharacters(rightInput.getValueAsString());
		}else{
			right="";
		}
		
		//RegExp pattern = RegExp.compile("(\\b"+left+"\\b)(.*?)(\\b"+right+"\\b)");
		RegExp pattern = RegExp.compile("("+left+")(.*?)("+right+")");
		
		String textInColumn = input;
		
		
		MatchResult matcher = pattern.exec(textInColumn);
		if(pattern.test(textInColumn)){
			return matcher.getGroup(2).trim();
		}
		return "";
	}
	private String translateEscapeCharacters(String input){
		return input.replaceAll("\\.", "\\\\.").replaceAll("\\(","\\\\(").replaceAll("\\)", "\\\\)");
	}
	
	
	

}
