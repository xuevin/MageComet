package uk.ac.ebi.fgpt.magecomet.client;


import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.VStack;

public class AutofillPopup extends Window{

	private EFOServiceAsync efoServiceAsync = GWT.create(EFOService.class);

    private final StaticTextItem termSourceNum = new StaticTextItem();
    private final CheckboxItem characteristicCheckbox = new CheckboxItem();  
    private final CheckboxItem factorValueCheckbox = new CheckboxItem();  
    private final CheckboxItem termSourceRefCheckbox = new CheckboxItem();  
    private final StaticTextItem termSourceRef = new StaticTextItem();
    private final CheckboxItem termSourceNumberCheckbox = new CheckboxItem();
    private final CheckboxItem addToAllRecordsCheckBox = new CheckboxItem();
    private final TextItem characteristicInput = new TextItem();
    private final TextItem factorValueInput = new TextItem();
	private final HTMLFlow efo_description = new HTMLFlow();

	public AutofillPopup(final String efoTerm,final GuiMediator guiMediator){
		super();
		setTitle(efoTerm);
		setWidth(500);
		setHeight(350);
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
		DynamicForm form = new DynamicForm();
		form.setNumCols(4);
		
		efo_description.setContents("Description:");
		efo_description.setHeight(20);
		
        characteristicCheckbox.setTitle("Characteristic");
        characteristicInput.setTitle("Column Name");
        characteristicInput.setHint("ie. Tissue");
        
        factorValueCheckbox.setTitle("Factor Value");
        
        
        factorValueInput.setTitle("Column Name");
        factorValueInput.setHint("ie. Tissue");

        
        termSourceRefCheckbox.setTitle("Term Source REF");
       
        termSourceRef.setTitle("Ontology");
        termSourceRef.setValue("EFO");
     
        termSourceNumberCheckbox.setTitle("Term Source Number");
        
        termSourceNum.setTitle("Accession Number");
		efoServiceAsync.getEfoAccessionIdByName(efoTerm, callback);
		efoServiceAsync.getEfoDescriptionByName(efoTerm, callback2);

        addToAllRecordsCheckBox.setTitle("Add Term as Value to All Records");
        addToAllRecordsCheckBox.setColSpan(4);
        
        
        
        
		
        form.setWidth(250);
        form.setAlign(Alignment.LEFT);
		form.setFields(characteristicCheckbox,
						characteristicInput,
						factorValueCheckbox,
						factorValueInput,
						termSourceRefCheckbox,
						termSourceRef,
						termSourceNumberCheckbox,
						termSourceNum,
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
				boolean characteristicChecked = characteristicCheckbox.getValueAsBoolean(); 
				boolean factorValueChecked = factorValueCheckbox.getValueAsBoolean();
				boolean termSourceRefChecked = termSourceRefCheckbox.getValueAsBoolean();
				boolean termSourceNumberChecked = termSourceNumberCheckbox.getValueAsBoolean();
				String characteristicString = characteristicInput.getValueAsString();
				String factorValueString = factorValueInput.getValueAsString();
			
				if(addToAllRecordsCheckBox.getValueAsBoolean()==true){
					if(characteristicChecked){
						guiMediator.addColumnToScratchAndAddValueToAllRecords("Characteristics["+characteristicString+"]", efoTerm);
						if(termSourceNumberChecked){
							guiMediator.addColumnToScratchAndAddValueToAllRecords("Term Accession Number", termSourceNum.getDisplayValue());
						}
						if(termSourceRefChecked){
							guiMediator.addColumnToScratchAndAddValueToAllRecords("Term Source REF", "EFO");
						}
					}
					if(factorValueChecked){
						guiMediator.addColumnToScratchAndAddValueToAllRecords("Factor Value["+factorValueString+"]", efoTerm);
						if(termSourceNumberChecked){
							guiMediator.addColumnToScratchAndAddValueToAllRecords("Term Accession Number", termSourceNum.getDisplayValue());
						}
						if(termSourceRefChecked){
							guiMediator.addColumnToScratchAndAddValueToAllRecords("Term Source REF", "EFO");
						}
					}
				}else{
					
					//Do some filtering here
					//Filter on all columns that have the value mentioned
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
        buttonsStack.addMember(saveButton);
		buttonsStack.addMember(cancelButton);
		
		VStack vStack = new VStack();
		vStack.setAlign(Alignment.LEFT);
		vStack.setAlign(VerticalAlignment.TOP);
		vStack.setHeight100();
		vStack.setMembersMargin(15);
		
		vStack.addMember(efo_description);
		vStack.addMember(form);
		
		addItem(vStack);
		addItem(buttonsStack);
	}
	
}
