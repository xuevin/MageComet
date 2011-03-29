package uk.ac.ebi.fgpt.magecomet.client;


import java.util.LinkedHashMap;

import uk.ac.ebi.fgpt.magecomet.client.tagcloud.EFOServiceAsync;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.client.widgets.form.validator.IsOneOfValidator;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.VStack;

public class AutofillPopup extends Window{


    private final StaticTextItem termSourceNum = new StaticTextItem();
    private final CheckboxItem newCharacteristicCheckbox = new CheckboxItem();  
    private final CheckboxItem newFactorValueCheckbox = new CheckboxItem();  
    private final CheckboxItem termSourceRefCheckbox = new CheckboxItem();  
    private final StaticTextItem termSourceRef = new StaticTextItem();
    private final CheckboxItem termSourceNumberCheckbox = new CheckboxItem();
    private final CheckboxItem addToAllRecordsCheckBox = new CheckboxItem();
    private final ComboBoxItem characteristicInput = new ComboBoxItem();
    private final ComboBoxItem factorValueInput = new ComboBoxItem();
	private final HTMLFlow efo_description = new HTMLFlow();
	private final ComboBoxItem sourceColumnCombobox = new ComboBoxItem();
	private final StaticTextItem sourceColumnInstructions = new StaticTextItem();
	private final CheckboxItem existingFactorValueCheckbox = new CheckboxItem();
	private final CheckboxItem existingCharacteristicCheckbox = new CheckboxItem();
	private final ComboBoxItem existingFactorValueInput = new ComboBoxItem();
    private final ComboBoxItem existingCharacteristicInput = new ComboBoxItem();
	private final DynamicForm form = new DynamicForm();

    private GuiMediator guiMediator;

	public AutofillPopup(final String efoTerm,final EFOServiceAsync efoServiceAsync,final GuiMediator guiMediator){
		super();
		this.guiMediator = guiMediator;
		setTitle("Term: " + efoTerm);
		setWidth(600);
		setHeight(500);
		setOverflow(Overflow.AUTO);
		centerInPage();
		setAlign(VerticalAlignment.TOP);
		show();
		
		
		final AsyncCallback<String> callback = new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
		        String details = caught.getMessage();
				 termSourceNum.setValue(details);

			}
			public void onSuccess(String EFOAccession){
				 termSourceNum.setValue(EFOAccession);
			}
		};
		final AsyncCallback<String> callback2 = new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
		        String details = caught.getMessage();
		        efo_description.setContents("Description:\n"+details);
			}
			public void onSuccess(String description){
				efo_description.setContents("Description:<br>"+description.replaceAll("\n", "<br><br>"));
			}
		};
        
		
		
		//*****************************
        // Form
        //*****************************
		form.setNumCols(4);
		form.setWidth(450);
		form.setAlign(Alignment.LEFT);
		
		
		
		efo_description.setContents("Description:");
		efo_description.setHeight(20);
		
        newCharacteristicCheckbox.setTitle("New Characteristic");
        characteristicInput.setWrapTitle(false);
        characteristicInput.setTitle("Column Title");
        characteristicInput.setValueMap(GlobalConfigs.getCommonCharacteristics());
        
        newFactorValueCheckbox.setTitle("New Factor Value");
        factorValueInput.setTitle("Column Title");
        factorValueInput.setWrapTitle(false);
        factorValueInput.setValueMap(GlobalConfigs.getCommonFactors());
        

        existingCharacteristicCheckbox.setTitle("Existing Characteristic Column");
        existingCharacteristicInput.setTitle("Existing Column");
        existingCharacteristicInput.setValueMap(guiMediator.getCharacteristicMap());
        existingCharacteristicInput.setValidators(new IsOneOfValidator());

        
        existingFactorValueCheckbox.setTitle("Existing Factor Value Column");
        existingFactorValueInput.setTitle("Existing Column");
        existingFactorValueInput.setValueMap(guiMediator.getFactorValuesMap());
        existingFactorValueInput.setValidators(new IsOneOfValidator());
        
        

        
        termSourceRefCheckbox.setTitle("Term Source REF");
       
        termSourceRef.setTitle("Ontology");
        termSourceRef.setValue("EFO");
     
        termSourceNumberCheckbox.setTitle("Term Source Number");
        
        termSourceNum.setTitle("Accession Number");
		termSourceNum.setWrapTitle(false);
		
        efoServiceAsync.getEfoAccessionIdByName(efoTerm, callback);
		efoServiceAsync.getEfoDescriptionByName(efoTerm, callback2);

        addToAllRecordsCheckBox.setTitle("Add Term as Value to All Records");
        addToAllRecordsCheckBox.setColSpan(4);
        addToAllRecordsCheckBox.setName("addToAllRecordsCheckBox");
        addToAllRecordsCheckBox.setRedrawOnChange(true);  
        addToAllRecordsCheckBox.setValue(false);
       
        sourceColumnInstructions.setShowTitle(false);
		sourceColumnInstructions.setColSpan(2);
		sourceColumnInstructions.setDefaultValue("Source column");
		

		sourceColumnCombobox.setTitle("Column");
		sourceColumnCombobox.setWrapTitle(false);
		sourceColumnCombobox.setValueMap(guiMediator.getColumnValueMap());
		sourceColumnCombobox.setValidators(new IsOneOfValidator());
		sourceColumnCombobox.setRequired(true);
        
        
        
        
        FormItemIfFunction ifNotChecked = new FormItemIfFunction() {
        	//If addAll is not checked, then this item is visible
            public boolean execute(FormItem item, Object value, DynamicForm form) {  
                return !(Boolean)form.getValue("addToAllRecordsCheckBox");  
            }  
        };
//        FormItemIfFunction ifChecked = new FormItemIfFunction() {
//        	//If addAll is not checked, then this item is visible
//            public boolean execute(FormItem item, Object value, DynamicForm form) {  
//                return (Boolean)form.getValue("addToAllRecordsCheckBox");  
//            }  
//        };
        
        newFactorValueCheckbox.setShowIfCondition(ifNotChecked);
        factorValueInput.setShowIfCondition(ifNotChecked);
        sourceColumnCombobox.setShowIfCondition(ifNotChecked);
        sourceColumnInstructions.setShowIfCondition(ifNotChecked);
        existingCharacteristicCheckbox.setShowIfCondition(ifNotChecked);
        existingCharacteristicInput.setShowIfCondition(ifNotChecked);
        existingFactorValueCheckbox.setShowIfCondition(ifNotChecked);
        existingFactorValueInput.setShowIfCondition(ifNotChecked);
        
       
		form.setFields(	addToAllRecordsCheckBox,	
						newCharacteristicCheckbox,
						characteristicInput,
						newFactorValueCheckbox,
						factorValueInput,
						existingCharacteristicCheckbox,
						existingCharacteristicInput,
						existingFactorValueCheckbox,
						existingFactorValueInput,
						termSourceRefCheckbox,
						termSourceRef,
						termSourceNumberCheckbox,
						termSourceNum,
						sourceColumnInstructions,
						sourceColumnCombobox);
		
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
				if(form.validate()){
					boolean termSourceRefChecked = termSourceRefCheckbox.getValueAsBoolean();
					boolean termSourceNumberChecked = termSourceNumberCheckbox.getValueAsBoolean();
					
					boolean newCharacteristicChecked = newCharacteristicCheckbox.getValueAsBoolean(); 
					boolean newFactorValueChecked = newFactorValueCheckbox.getValueAsBoolean();
					
					boolean existingCharacteristicChecked = existingCharacteristicCheckbox.getValueAsBoolean();
					boolean existingFactorValueChecked = existingFactorValueCheckbox.getValueAsBoolean();
					
					String characteristicString = characteristicInput.getValueAsString();
					String factorValueString = factorValueInput.getValueAsString();
					String existingCharacteristicInputColumnName = existingCharacteristicInput.getValueAsString();
					String existingFactorValueInputColumnName = existingFactorValueInput.getValueAsString();
					String sourceColumn = sourceColumnCombobox.getValueAsString();
				
					if(addToAllRecordsCheckBox.getValueAsBoolean()==true){
						if(newCharacteristicChecked){
							if(termSourceNumberChecked){
								guiMediator.addColumnToCharacteristicAndAddValueToAllRecords("Term Accession Number", termSourceNum.getDisplayValue());
							}
							if(termSourceRefChecked){
								guiMediator.addColumnToCharacteristicAndAddValueToAllRecords("Term Source REF", "EFO");
							}
							guiMediator.addColumnToCharacteristicAndAddValueToAllRecords(characteristicString, efoTerm);
						}
					}else{
						if(existingCharacteristicChecked && newCharacteristicChecked){
							System.out.println("TWO CHECKED");
							return;
						}
						if(existingFactorValueChecked && newFactorValueChecked){
							System.out.println("TWO CHECKED");
							return;
						}
						if(existingCharacteristicChecked){
							guiMediator.addAttributeToSelectedRecords(sourceColumn, existingCharacteristicInputColumnName, efoTerm);
						}
						if(existingFactorValueChecked){
							guiMediator.addAttributeToSelectedRecords(sourceColumn, existingFactorValueInputColumnName, efoTerm);
						}
						if(newCharacteristicChecked){
							String newColumnName = guiMediator.addCharacteristicToActiveGrid(characteristicString);
							guiMediator.addAttributeToSelectedRecords(sourceColumn, newColumnName, efoTerm);
						}
						if(newFactorValueChecked){
							String newColumnName = guiMediator.addFactorValueToActiveGrid(factorValueString);
							guiMediator.addAttributeToSelectedRecords(sourceColumn, newColumnName, efoTerm);

						}
					}
					guiMediator.refreshTable();
					hide();
				}
			}
		});
        
        //This save button will add the corresponding columns into the table
        Button cancelButton = new Button("Cancel");
        cancelButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				hide();
			}
		
		});
		
        
        //*****************************
        // Layout
        //*****************************
        buttonsStack.addMember(saveButton);
		buttonsStack.addMember(cancelButton);
		
		VStack vStack = new VStack();
		vStack.setPadding(10);
		vStack.setAlign(Alignment.LEFT);
		vStack.setAlign(VerticalAlignment.TOP);
		vStack.setHeight100();
		vStack.setMembersMargin(15);
		
		vStack.addMember(efo_description);
		vStack.addMember(form);
		
		addItem(vStack);
		addItem(buttonsStack);
	}

	public void updateColumns() {
		sourceColumnCombobox.setValueMap(guiMediator.getColumnValueMap());
		existingCharacteristicInput.setValueMap(guiMediator.getCharacteristicMap());
		existingFactorValueInput.setValueMap(guiMediator.getFactorValuesMap());
		
	}
	
	
}
