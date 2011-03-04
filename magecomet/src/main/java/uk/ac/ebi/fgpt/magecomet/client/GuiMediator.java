package uk.ac.ebi.fgpt.magecomet.client;


import java.util.LinkedHashMap;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class GuiMediator {
	private TagCloudWindow tagCloudWindow;
	private SDRF_Section sdrfSection;
	private IDF_Section idfSection;
	private SDRF_Section_ColumnEditor sdrfSectionColumnEditor;
	private FilterTab filterTab;
	private ExtractTab extractTab;
	

	public GuiMediator(){
	}
	public void registerTagCloud(TagCloudWindow tagCloud){
		this.tagCloudWindow=tagCloud;	
	}
	public void registerSDRFSection(SDRF_Section sdrfSection){
		this.sdrfSection=sdrfSection;
	}
	public void registerSDRFSectionColumnEditor(
			SDRF_Section_ColumnEditor sdrfSectionColumnEditor) {
		this.sdrfSectionColumnEditor=sdrfSectionColumnEditor;
	}
	public void registerIDFSection(IDF_Section idfSection){
		this.idfSection=idfSection;	
	}
	public void registerFilterTab(FilterTab filterTab) {
		this.filterTab = filterTab;
	}
	public void registerExtractTab(ExtractTab extractTab){
		this.extractTab=extractTab;
	}
	public void addWordToTagCloud(final String word, int weight) {
		//Very ugly way of referring to itself..
		//TODO Maybe this click handeler doesn't belong here.
		final GuiMediator self = this;
		ClickAction action = new ClickAction() {
			public void execute(){
				new AutofillPopup(word,self);				
			}
			
		};
		if(tagCloudWindow!=null){
			tagCloudWindow.addWord(word,action,weight);
		}
	}
	/**
	 * Updates the combo box. Should be called every time the table changes
	 */
	public void updateColumnsInComboBox(ListGridField[] listGridFields) {
		LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();  
		for(ListGridField field:listGridFields){
			valueMap.put(field.getName(), field.getTitle());
		}
		if(filterTab!=null){
			filterTab.updateColumnsInComboBox(valueMap);	
		}
		if(extractTab!=null){
			extractTab.updateColumnsInComboBox(valueMap);	
		}
	}
	public void addColumnToScratchAndAddValueToAllRecords(String title,String value){
		String uniqueKey = sdrfSectionColumnEditor.addNewColumnAndGetKey(title);
		sdrfSection.addAttributeToAllRecords(uniqueKey,value);
		sdrfSectionColumnEditor.show();
	}
	public void passDataToFilterTab(ListGrid sdrfTable) {
		filterTab.setData(sdrfTable);
	}
	public void passAllRecordsToExtractTab(ListGridRecord[] listGridRecords){
		extractTab.setRecords(listGridRecords);
	}

}
