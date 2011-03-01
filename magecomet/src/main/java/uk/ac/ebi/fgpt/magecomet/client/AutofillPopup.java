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

	public AutofillPopup(String title){
		super();
		setTitle(title);
		setWidth(400);
		setHeight(240);
		centerInPage();
		setAlign(VerticalAlignment.TOP);
		show();
		
		
		
		DynamicForm form = new DynamicForm();
		form.setNumCols(4);
		
		Label efo_name = new Label();
		efo_name.setContents(title);
		efo_name.setHeight(20);
		
        CheckboxItem characteristicCheckbox = new CheckboxItem();  
        characteristicCheckbox.setTitle("Characteristic");
        TextItem characteristicInput = new TextItem();
        characteristicInput.setTitle("Column Name");
        characteristicInput.setHint("ie. Tissue");
        
        CheckboxItem factorValueCheckbox = new CheckboxItem();  
        factorValueCheckbox.setTitle("Factor Value");
        
        TextItem factorValueInput = new TextItem();
        factorValueInput.setTitle("Column Name");
        factorValueInput.setHint("ie. Tissue");

        
        CheckboxItem termSourceRefCheckbox = new CheckboxItem();  
        termSourceRefCheckbox.setTitle("Term Source REF");
        termSourceRefCheckbox.setColSpan(4);
        
        CheckboxItem termSourceNumberCheckbox = new CheckboxItem();
        termSourceNumberCheckbox.setTitle("Term Source Number");
        termSourceNumberCheckbox.setColSpan(4);
        
        Button saveButton = new Button("Save");
        //This save button will add the corresponding columns into the table
        
        
        Button cancelButton = new Button("Cancel");
        cancelButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				destroy();
				
			}
			
		
		});
        
		
        form.setWidth(250);
        form.setAlign(Alignment.LEFT);
		form.setFields(characteristicCheckbox,
						characteristicInput,
						factorValueCheckbox,
						factorValueInput,
						termSourceRefCheckbox,
						termSourceNumberCheckbox);
		
		
		
		HStack buttonsStack = new HStack();
		buttonsStack.setAlign(Alignment.RIGHT);
		buttonsStack.setAlign(VerticalAlignment.BOTTOM);
		
		buttonsStack.addMember(cancelButton);
		buttonsStack.addMember(saveButton);
		
		VStack vStack = new VStack();
		vStack.setAlign(Alignment.LEFT);
		vStack.setAlign(VerticalAlignment.TOP);
		vStack.setHeight100();
		
		vStack.addMember(efo_name);
		vStack.addMember(form);
		
		
		addMember(vStack);
		addMember(buttonsStack);
	}
	
}
