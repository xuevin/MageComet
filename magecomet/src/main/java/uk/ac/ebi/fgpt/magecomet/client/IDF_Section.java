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
	
	private final ListGrid idfGrid = new ListGrid();
	private final HTMLFlow textBox = new HTMLFlow("Experiment Description");
	private GuiMediator guiMediator;

	public IDF_Section(GuiMediator guiMediator){
		super("IDF");
		
		this.guiMediator = guiMediator;
		this.guiMediator.registerIDFSection(this);
		
		HStack hStack = new HStack();
		
		idfGrid.setCanEdit(true);
		idfGrid.setEditEvent(ListGridEditEvent.DOUBLECLICK);
		idfGrid.setCanSort(false);
		idfGrid.setWidth("60%");
		idfGrid.setShowAllRecords(true);
		idfGrid.setShowBackgroundComponent(false);
		idfGrid.setShowRollOver(false);

		
		textBox.setWidth("40%");
		textBox.setOverflow(Overflow.AUTO);
		textBox.setMargin(5);
		
		hStack.addMember(idfGrid);
		hStack.addMember(textBox);
		
		addItem(hStack);
		
//		IButton editFieldsButton = new IButton("Edit Fields");
//		editFieldsButton.setLeft(0);
//		editFieldsButton.addClickHandler(new ClickHandler() {
//			public void onClick(ClickEvent event) {
//				Window editRowWindow = new Window();
//				editRowWindow.setTitle("Customize IDF Fields");
//				editRowWindow.setWidth(600);
//				editRowWindow.setHeight(400);
//				editRowWindow.centerInPage();
//				editRowWindow.show();
//				
//				
//				//Available Fields that the user can select from
//				final ListGrid availableFields = new ListGrid();
//				availableFields.setVisible(true);
//				availableFields.setWidth("45%");
//				availableFields.setHeight("95%");
//				availableFields.setLayoutAlign(VerticalAlignment.CENTER);
//				//Fields that are visible to the user
//				final ListGrid visibleFields = new ListGrid();
//				visibleFields.setVisible(true);
//				visibleFields.setWidth("45%");
//				visibleFields.setHeight("95%");
//				visibleFields.setLayoutAlign(VerticalAlignment.CENTER);
//				
//				
//				TransferImgButton arrowImg = new TransferImgButton(TransferImgButton.RIGHT);
//				arrowImg.addClickHandler(new ClickHandler() {
//					public void onClick(ClickEvent event) {
//						availableFields.transferSelectedData(visibleFields);
//					}
//				});
//				
//				
//				HStack hStack = new HStack();
//				hStack.setAlign(Alignment.CENTER);
//				hStack.setAlign(VerticalAlignment.CENTER);
//				hStack.addMember(availableFields);
//				hStack.addMember(arrowImg);
//				hStack.addMember(visibleFields);
//				
//				
//				editRowWindow.addItem(hStack);
//				
//			}
//		});
	}
	public void setDescription(String filteredOutData){
		textBox.setContents(filteredOutData);
	}
	public void setData(ListGridField[] allFields, ListGridRecord[] allRecords) {
		idfGrid.setRecords(allRecords);
		idfGrid.setFields(allFields);
	}
}
