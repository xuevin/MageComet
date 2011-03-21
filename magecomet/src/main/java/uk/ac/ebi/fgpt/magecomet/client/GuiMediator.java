package uk.ac.ebi.fgpt.magecomet.client;


import java.util.LinkedHashMap;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class GuiMediator{
	private TagCloudWindow tagCloudWindow;
	private SDRF_Section sdrfSection;
	private IDF_Section idfSection;
	private SDRF_Section_ColumnEditor sdrfSectionColumnEditor;
	private FilterTab filterTab;
	private ExtractTab extractTab;
	private LoadTab loadTab;
	private ErrorsTab errorsTab;
	private String currentIDF;
	private String currentSDRF;
	private EditTab editTab;
	

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
	public void registerLoadTab(LoadTab loadTab) {
		this.loadTab = loadTab;
	}
	public void registerErrorsTab(ErrorsTab errorsTab){
		this.errorsTab=errorsTab;
	}
	public void registerEditTab(EditTab editTab) {
		this.editTab = editTab;
	}
	
	
	/**
	 * Updates the columns. Should be called every time the table changes
	 */
	public void updateColumnsInComboBoxes(ListGridField[] listGridFields) {
		//Update Data source in table
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
	/**
	 * Updates the DataSource in the SDRFSection. Also updates columns in the combo boxes
	 * Also calls updateColumnsInComboBoxes
	 * @param listGridFields a list of ListGridFields (which represents the title of a column)
	 */
	public void updateDataSource(ListGridField[] listGridFields){
		sdrfSection.updateDataSource(listGridFields);
	}
	public void addColumnToClipboardAndAddValueToAllRecords(String title,String value){
		String uniqueKey = sdrfSectionColumnEditor.addNewColumnToClipboardAndGetKey(title);
		sdrfSection.addAttributeToAllRecords(uniqueKey,value);
		sdrfSectionColumnEditor.show();
	}
	public String addFactorValueToActiveGrid(String title){
		return sdrfSectionColumnEditor.addNewFactorValueColumnAndGetKey(title);
	}
	public String addCharacteristicToActiveGrid(String title){
		return sdrfSectionColumnEditor.addNewCharacteristicColumnAndGetKey(title);
	}
	public String addColumnToClipboard(String title){
		return sdrfSectionColumnEditor.addNewColumnToClipboardAndGetKey(title);
	}
	public void passDataToFilterTab(ListGrid sdrfTable) {
		filterTab.setData(sdrfTable);
	}
	public void passAllRecordsToExtractTab(ListGridRecord[] listGridRecords,ListGrid sdrfTable){
		extractTab.setRecords(listGridRecords,sdrfTable);
	}
	public void refreshTable(){
		sdrfSection.refreshTable();
	}
	public void passDataToSDRFSection(JSONObject object){
		sdrfSection.handleJSONObject(object);
	}
	public void passDataToIDFSection(JSONObject object){
		idfSection.handleJSONObject(object);
	}
	public void passDataToErrorsTab(JSONObject object){
		errorsTab.handelJSONArrayOfErrors(object);
	}
	public void setCurrentIDF(String currentIDF) {
		this.currentIDF = currentIDF;
	}
	public String getCurrentIDF() {
		return currentIDF;
	}
	public void setCurrentSDRF(String currentSDRF) {
		this.currentSDRF = currentSDRF;
	}
	public String getCurrentSDRF() {
		return currentSDRF;
	}
	public String getSDRFAsString() {
		return sdrfSection.getString();
	}
	public String getIDFAsString() {
		return idfSection.getString();
	}
	public void passDataToTagCloud(JSONObject jsonObject) {
//		System.out.println("I dont get it");
		
		if(jsonObject.get("whatizitIDF")!=null){
//			System.out.println("NOt Null");
			JSONArray tagWords = jsonObject.get("whatizitIDF").isArray() ;
			for (int i = 0; i < tagWords.size(); i++) {
				final String word = tagWords.get(i).isString().stringValue();
				tagCloudWindow.addWord(word,1);
			}
		}
//		System.out.println("I dont get it?");
		
		if((jsonObject.get("whatizitSDRF")!=null)){
			JSONArray tagWords=jsonObject.get("whatizitSDRF").isArray();
//			System.out.println("Still not null");

			for (int i = 0; i < tagWords.size(); i++) {
				final String word = tagWords.get(i).isString().stringValue();
				tagCloudWindow.addWord(word,2);
			}
		}
//		System.out.println("It never finishes...");
	}
//	public void addWordToTagCloud(final String word, int weight) {
//		if(tagCloudWindow!=null){
//			tagCloudWindow.addWord(word,weight);
//		}//TODO make this a special Tab
//	}


	


}
