package uk.ac.ebi.fgpt.magecomet.client;


import java.util.LinkedHashMap;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.smartgwt.client.data.AdvancedCriteria;
import com.smartgwt.client.data.DataSource;
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
	private EditTab editTab;
	private LinkedHashMap<String, String> columnValueMap; 
	private String currentIDF;
	private String currentSDRF;
	private SDRF_Data sdrfData;
	

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
	//*****************************************
	// End Registration
	//*****************************************
	
	/**
	 * Updates the columns. Should be called every time the table changes
	 */
	public void updateColumnsInComboBoxes(ListGridField[] listGridFields) {
		//Update Data source in table
		columnValueMap = new LinkedHashMap<String, String>();  
		for(ListGridField field:listGridFields){
			columnValueMap.put(field.getName(), field.getTitle());
		}
		if(filterTab!=null){
			filterTab.updateColumnsInComboBox(columnValueMap);
		}
		if(extractTab!=null){
			extractTab.updateColumnsInComboBox(columnValueMap);	
		}
	}
	public void addColumnToClipboardAndAddValueToAllRecords(String title,String value){
		String uniqueKey = sdrfSectionColumnEditor.addNewColumnToClipboardAndGetKey(title);
		sdrfData.addAttributeToAllRecords(uniqueKey,value);
		sdrfSectionColumnEditor.show();
	}
	/**
	 * Adds a characteristic column, directly after sourceName and adds the specified value to all records
	 * @param title the name of the field
	 * @param value the value to be filled in for all records
	 */
	public void addColumnToCharacteristicAndAddValueToAllRecords(String title,String value){
		String uniqueKey = sdrfData.addNewColumn_Characteristic_AndGetKey(title);
		sdrfData.addAttributeToAllRecords(uniqueKey,value);
	}
	public String addFactorValueColumnAndGetKey(String title){
		return sdrfData.addNewColumn_FactorValue_AndGetKey(title);
	}
	public String addCharacteristicColumnAndGetKey(String title){
		return sdrfData.addNewColumn_Characteristic_AndGetKey(title);
	}
	public String getNewColumnKey(){
		return sdrfData.getNewColumnKey();
	}
	public void filterAndReplace(AdvancedCriteria advancedCriteria, String uniqueKey, String value){
		sdrfSection.filterTable(advancedCriteria);
		sdrfData.setValueForSelectedRecords(sdrfSection.getListOfRecords(),uniqueKey,value);
		
	}
	public void filterTable(AdvancedCriteria advancedCriteria){
		sdrfSection.filterTable(advancedCriteria);
	}
	@Deprecated
	public void passAllRecordsToExtractTab(ListGridRecord[] listGridRecords,ListGrid sdrfTable){
		extractTab.setRecords(listGridRecords,sdrfTable);
	}
	public void passDataSourceToFilterTab(final DataSource data) {
		filterTab.setData(data);
	}
	public void loadSDRFData(JSONObject object){
		sdrfData= new SDRF_Data(object);
		sdrfSection.setData(sdrfData.getDataSource(),sdrfData.getAllFields(),sdrfData.getAllRecords());
		updateColumnsInComboBoxes(sdrfData.getAllFields());

	}
	public void passDataToIDFSection(JSONObject object){
		idfSection.handleJSONObject(object);
	}
	public void passDataToErrorsTab(JSONObject object){
		System.out.println("JSONObject was passed to Errors Tab");
		errorsTab.handelJSONObject(object);
	}
	public void updateSDRFColumnNames(ListGridField[] newArrayOfListGridFields){
		sdrfData.updateColumnNames(newArrayOfListGridFields);
		sdrfData.updateDataSource(newArrayOfListGridFields);
	}
	public ListGridField[] getAllFields(){
		return sdrfData.getAllFields();
	}
	public void passDataToTagCloud(JSONObject jsonObject) {
			if(jsonObject.get("whatizitIDF")!=null){
				JSONArray tagWords = jsonObject.get("whatizitIDF").isArray() ;
				for (int i = 0; i < tagWords.size(); i++) {
					final String word = tagWords.get(i).isString().stringValue();
					tagCloudWindow.addWord(word,1);
				}
			}
			if((jsonObject.get("whatizitSDRF")!=null)){
				JSONArray tagWords=jsonObject.get("whatizitSDRF").isArray();
				for (int i = 0; i < tagWords.size(); i++) {
					final String word = tagWords.get(i).isString().stringValue();
					tagCloudWindow.addWord(word,2);
				}
			}
			tagCloudWindow.refreshTagClouds();
		}
	public void setCurrentIDFTitle(String currentIDF) {
		this.currentIDF = currentIDF;
	}
	public String getCurrentIDFTitle() {
		return currentIDF;
	}
	public void setCurrentSDRFTitle(String currentSDRF) {
		this.currentSDRF = currentSDRF;
	}
	public String getCurrentSDRFTitle() {
		return currentSDRF;
	}
	public String getSDRFAsString() {
		return sdrfData.getString();
	}
	public String getIDFAsString() {
		return idfSection.getString();
	}
	public LinkedHashMap<String,String> getColumnValueMap(){
		if(columnValueMap==null){
			System.err.println("COLUMN VALUE MAP IS NULL!");
			return new LinkedHashMap<String, String>();
		}
		return columnValueMap;
	}
	public LinkedHashMap<String,String> getFactorValuesMap(){
		if(columnValueMap==null){
			System.err.println("COLUMN VALUE MAP IS NULL!");
			return new LinkedHashMap<String, String>();
		}
		LinkedHashMap<String,String> factorValuesMap = new LinkedHashMap<String, String>();
		for(String key:columnValueMap.keySet()){
			String value = columnValueMap.get(key);
			if(value.contains("Factor Value")){
				factorValuesMap.put(key, value);	
			}
		}
		return factorValuesMap;
	}
	public LinkedHashMap<String,String> getCharacteristicMap(){
		if(columnValueMap==null){
			System.err.println("COLUMN VALUE MAP IS NULL!");
			return new LinkedHashMap<String, String>();
		}
		LinkedHashMap<String,String> characteristicValuesMap = new LinkedHashMap<String, String>();
		for(String key:columnValueMap.keySet()){
			String value = columnValueMap.get(key);
			if(value.contains("Characteristics")){
				characteristicValuesMap.put(key, value);	
			}
		}
		return characteristicValuesMap;
	}
	public void refreshTable(){
		sdrfSection.refreshTable(sdrfData.getDataSource(),sdrfData.getAllFields());
	}
	public void addValueToSelectedRecords(String fromColumn, String destinationColumn, String value){
		sdrfData.addValueToSelectedRecords(fromColumn, destinationColumn, value);
	}
	public String addColumnToClipboardAndGetKey(String title) {
		return sdrfSectionColumnEditor.addNewColumnToClipboardAndGetKey(title);
	}
	public boolean dataHasBeenLoaded(){
		return (sdrfData!=null);
	}
	public void passArrayToErrorsTab(JSONArray array) {
		errorsTab.handleJSONArrayOfErrors(array);
	}
}
