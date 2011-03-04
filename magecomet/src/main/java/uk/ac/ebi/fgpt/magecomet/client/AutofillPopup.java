package uk.ac.ebi.fgpt.magecomet.client;


import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.VStack;

public class AutofillPopup extends Window{

	public AutofillPopup(final String efoTerm,final GuiMediator guiMediator){
		super();
		setTitle(efoTerm);
		setWidth(400);
		setHeight(240);
		centerInPage();
		setAlign(VerticalAlignment.TOP);
		show();
		
		
        //*****************************
        // Form
        //*****************************
		DynamicForm form = new DynamicForm();
		form.setNumCols(4);
		
		Label efo_description = new Label();
		efo_description.setContents(efoTerm+"Description");
		efo_description.setHeight(20);
		
        final CheckboxItem characteristicCheckbox = new CheckboxItem();  
        characteristicCheckbox.setTitle("Characteristic");
        TextItem characteristicInput = new TextItem();
        characteristicInput.setTitle("Column Name");
        characteristicInput.setHint("ie. Tissue");
        
        final CheckboxItem factorValueCheckbox = new CheckboxItem();  
        factorValueCheckbox.setTitle("Factor Value");
        
        TextItem factorValueInput = new TextItem();
        factorValueInput.setTitle("Column Name");
        factorValueInput.setHint("ie. Tissue");

        
        final CheckboxItem termSourceRefCheckbox = new CheckboxItem();  
        termSourceRefCheckbox.setTitle("Term Source REF");
        termSourceRefCheckbox.setColSpan(4);
        
        final CheckboxItem termSourceNumberCheckbox = new CheckboxItem();
        termSourceNumberCheckbox.setTitle("Term Source Number");
        termSourceNumberCheckbox.setColSpan(4);
        
        final CheckboxItem addToAllRecordsCheckBox = new CheckboxItem();
        addToAllRecordsCheckBox.setTitle("Add Term as Value to All Records");
        addToAllRecordsCheckBox.setColSpan(4);
        
        
        
        
		
        form.setWidth(250);
        form.setAlign(Alignment.LEFT);
		form.setFields(characteristicCheckbox,
						characteristicInput,
						factorValueCheckbox,
						factorValueInput,
						termSourceRefCheckbox,
						termSourceNumberCheckbox,
						addToAllRecordsCheckBox);
		
		HStack buttonsStack = new HStack();
		buttonsStack.setAlign(Alignment.RIGHT);
		buttonsStack.setAlign(VerticalAlignment.BOTTOM);
		
		
		
		
        //*****************************
        // Buttons
        //*****************************
        //This button will save the changes
		Button saveButton = new Button("Save");
        saveButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				boolean characteristic = characteristicCheckbox.getValueAsBoolean(); 
				boolean factorValue = factorValueCheckbox.getValueAsBoolean();
				boolean termSourceRef = termSourceRefCheckbox.getValueAsBoolean();
				boolean termSourceNumber = termSourceNumberCheckbox.getValueAsBoolean();
				
				
				
				if(addToAllRecordsCheckBox.getValueAsBoolean()==true){
					guiMediator.addColumnToScratchAndAddValueToAllRecords("Comments", efoTerm);	
				}
				destroy();
			}
		});
        
        //This save button will add the corresponding columns into the table
        Button cancelButton = new Button("Cancel");
        cancelButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				destroy();
				
			}
		
		});
		
        
        //*****************************
        // Layout
        //*****************************
		buttonsStack.addMember(cancelButton);
		buttonsStack.addMember(saveButton);
		
		VStack vStack = new VStack();
		vStack.setAlign(Alignment.LEFT);
		vStack.setAlign(VerticalAlignment.TOP);
		vStack.setHeight100();
		
		vStack.addMember(efo_description);
		vStack.addMember(form);
		
		addMember(vStack);
		addMember(buttonsStack);
	}
	
}
