package uk.ac.ebi.fgpt.magecomet.client.view.window;

import java.util.LinkedHashMap;

import uk.ac.ebi.fgpt.magecomet.client.GuiMediator;
import uk.ac.ebi.fgpt.magecomet.client.model.GlobalConfigs;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.VStack;

public class IDF_FactorValue_ValidatorWindow extends Window{
	private final DynamicForm form = new DynamicForm();
	private LinkedHashMap<String, String> savedMappings = new LinkedHashMap<String, String>();
	private GuiMediator guiMediator;

	public IDF_FactorValue_ValidatorWindow(GuiMediator guiMediator){
		super();
		this.guiMediator = guiMediator;
		form.setColWidths(3);
		setTitle("Confirm Factor Values");
		setWidth("600");
		setHeight("300");
		
		//Buttons
		
		// Make a new button to confirm changes
		IButton save = new IButton("Save");
		save.setLeft(0);
		save.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				saveAction();
			}
		});

		// Make a new button to discard changes
		IButton cancel = new IButton("Cancel");
		cancel.setLeft(0);
		cancel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				discardAction();
			}
		});
		
		
		HTMLFlow flow = new HTMLFlow();
		flow.setContents("Please Confirm that the following factor value column names match " +
				"to the correct factor value type");
		
		HStack hStack = new HStack();
		hStack.addMember(save);
		hStack.addMember(cancel);
		hStack.setAlign(Alignment.RIGHT);
		hStack.setHeight(20);
		
		VStack vStack = new VStack();
		vStack.addMember(flow);
		vStack.addMember(form);
		vStack.setMargin(10);
		vStack.setMembersMargin(10);
		
		
		addItem(vStack);
		addItem(hStack);
		centerInPage();		
	}

	private void discardAction() {
		hide();
	}

	private void saveAction() {
		for(FormItem item:form.getFields()){
			savedMappings.put(item.getTitle(), item.getValue().toString());
		}
		guiMediator.setFactorValuesInIDF(savedMappings);
		hide();
	}

	public void updateFactorValues(LinkedHashMap<String, String> factorValues) {
		
		FormItem[] fields = new FormItem[factorValues.size()];
		
		int i =0;
		for(String key:factorValues.keySet()){
			ComboBoxItem newcomboBox = new ComboBoxItem();
			newcomboBox.setWrapTitle(false);
			newcomboBox.setTitle(factorValues.get(key));
			newcomboBox.setValueMap(GlobalConfigs.getCommonFactorValueTypes());
			if(savedMappings.containsKey(factorValues.get(key))){
				newcomboBox.setDefaultValue(savedMappings.get(factorValues.get(key)));
			}
			fields[i]=newcomboBox;
			i++;
		}
		form.setFields(fields);
		
	}

}
