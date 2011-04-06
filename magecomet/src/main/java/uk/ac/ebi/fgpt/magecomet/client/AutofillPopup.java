package uk.ac.ebi.fgpt.magecomet.client;



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
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.VStack;

public class AutofillPopup extends Window{


	private final HTMLFlow efo_description = new HTMLFlow();
    private final StaticTextItem termSourceNum = new StaticTextItem();
    private final StaticTextItem termSourceRef = new StaticTextItem();
    private final CheckboxItem newCharacteristicCheckbox = new CheckboxItem();  
    private final CheckboxItem termSourceRefCheckbox = new CheckboxItem();  
    private final CheckboxItem termSourceNumberCheckbox = new CheckboxItem();
    private final CheckboxItem addToAllRecordsCheckBox = new CheckboxItem();
	private final ComboBoxItem characteristicInput = new ComboBoxItem();
	private final DynamicForm form = new DynamicForm();
	private final VStack vStack = new VStack();

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
        addToAllRecordsCheckBox.setValue(true);
      
       
		form.setFields(	addToAllRecordsCheckBox,
						newCharacteristicCheckbox,
						characteristicInput,
						termSourceRefCheckbox,
						termSourceRef,
						termSourceNumberCheckbox,
						termSourceNum
						);
		
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
					submit(efoTerm);
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

	private void submit(String efoTerm) {

		//Convenience Booleans
		boolean termSourceRefChecked = termSourceRefCheckbox.getValueAsBoolean();
		boolean termSourceNumberChecked = termSourceNumberCheckbox.getValueAsBoolean();
		boolean newCharacteristicChecked = newCharacteristicCheckbox.getValueAsBoolean(); 
		
		//Convenience Values
		String characteristicString = characteristicInput.getValueAsString();	
		/*
		 * If you are adding a value to all records, it must be a characteristic
		 */
		if(addToAllRecordsCheckBox.getValueAsBoolean()==true){
			if(termSourceNumberChecked){
				guiMediator.addColumnToCharacteristicAndAddValueToAllRecords("Term Accession Number", termSourceNum.getDisplayValue());
			}
			if(termSourceRefChecked){
				guiMediator.addColumnToCharacteristicAndAddValueToAllRecords("Term Source REF", "EFO");
			}
			if(newCharacteristicChecked){
				guiMediator.addColumnToCharacteristicAndAddValueToAllRecords("Characteristics["+characteristicString+"]", efoTerm);
			}
		}
		guiMediator.refreshTable();
		hide();
		
	}
	
}
