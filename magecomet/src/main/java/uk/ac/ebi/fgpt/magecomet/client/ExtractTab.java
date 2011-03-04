package uk.ac.ebi.fgpt.magecomet.client;

import java.util.LinkedHashMap;

import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
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
	private GuiMediator guiMediator;

	private ListGridRecord[] listOfAllRecords;

	public ExtractTab(GuiMediator guiMediator) {
		this.guiMediator=guiMediator;
		this.guiMediator.registerExtractTab(this);
		
		setTitle("Extract");
		
		vstack.setHeight(40);
		
		hstack.setHeight(40);
		
		Label directions = new Label();
		directions.setHeight(15);
		directions.setContents("Input the characters surrounding the the value that will be extracted into a new column.");
		

		//*****************************
		// Form
		//*****************************
		
		form.setNumCols(6);
		columnComboBoxItem.setTitle("From");
		
        TextItem leftInput = new TextItem();
        leftInput.setTitle("Left");
		TextItem rightInput = new TextItem();
		rightInput.setTitle("Right");
		
		submit.setTitle("Extract");
		
	
		
		//*****************************
		// Layout
		//*****************************
		form.setItems(columnComboBoxItem,leftInput,rightInput);

		
		hstack.addMember(form);
		hstack.addMember(submit);
		
		vstack.addMember(directions);
		vstack.addMember(hstack);
		
		setPane(vstack);
	}
	public void updateColumnsInComboBox(LinkedHashMap<String, String> valueMap) {
		if(columnComboBoxItem!=null){
			columnComboBoxItem.setValueMap(valueMap);	
		}
	}
	public void setRecords(final ListGridRecord[] listGridRecords) {
		//I HOpe this does not get called more than once.
		listOfAllRecords = listGridRecords;
		submit.addClickHandler(new ClickHandler() {
			
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				
			}
		});
	}

}
