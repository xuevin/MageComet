package uk.ac.ebi.fgpt.magecomet.client;

import java.util.LinkedHashMap;

import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
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
	private final TextItem newColumn = new TextItem();
    private final TextItem leftInput = new TextItem();
	private final TextItem rightInput = new TextItem();
	private final HTMLFlow sampleOutput = new HTMLFlow();
	private GuiMediator guiMediator;

	private ListGridRecord[] listOfAllRecords;

	public ExtractTab(GuiMediator guiMediator) {
		this.guiMediator=guiMediator;
		this.guiMediator.registerExtractTab(this);
		
		
		setTitle("Extract");
		
		vstack.setHeight(40);
		
		hstack.setHeight(40);
		
		HTMLFlow directions = new HTMLFlow();
		directions.setHeight(15);
		directions.setContents("Input the characters surrounding the the value that will be extracted into a new column.<br>" +
				"To indicate the beginning of the row use \"^\" and to indicate the end of a row use \"$\"");
		

		//*****************************
		// Form
		//*****************************
		
		form.setNumCols(8);
		columnComboBoxItem.setTitle("From");
		
        leftInput.setTitle("Left");
		rightInput.setTitle("Right");
	
		sampleOutput.setHeight(75);
		sampleOutput.setContents("Sample Extract:<br>");
		
	
		
		
		newColumn.setTitle("New Column:");

		
		submit.setTitle("Extract");
		
	
		
		//*****************************
		// Layout
		//*****************************
		form.setItems(columnComboBoxItem,leftInput,rightInput,newColumn);

		
		hstack.addMember(form);
		hstack.addMember(submit);
		
		vstack.addMember(directions);
		vstack.addMember(hstack);
		vstack.addMember(sampleOutput);
		
		setPane(vstack);
	}
	public void updateColumnsInComboBox(LinkedHashMap<String, String> valueMap) {
		if(columnComboBoxItem!=null){
			columnComboBoxItem.setValueMap(valueMap);	
		}
	}
	public ChangedHandler inputChanged = new ChangedHandler() {
		
		public void onChanged(ChangedEvent event) {
			String output="";
			//How many samples do you want to see
			int i = 5; 
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
				
				output+=extract(textInColumn)+"<br>";
				i--;
			}
			sampleOutput.setContents("Sample Extract:<br>"+output);
						
		}
	};
	public void setRecords(final ListGridRecord[] listGridRecords) {
		listOfAllRecords = listGridRecords;
		
		leftInput.addChangedHandler(inputChanged);
		rightInput.addChangedHandler(inputChanged);
		
		submit.addClickHandler(new ClickHandler() {
			
			public void onClick(ClickEvent event) {
				//Add a new column
				String newColumnName = guiMediator.addColumnToScratch(newColumn.getValueAsString());
				
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
				}
			}
		});
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
