package uk.ac.ebi.fgpt.magecomet.client;

import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import uk.ac.ebi.fgpt.magecomet.client.gui.section.IDF_Section;
import uk.ac.ebi.fgpt.magecomet.client.gui.section.SDRF_Section;
import uk.ac.ebi.fgpt.magecomet.client.gui.tab.EditTab;
import uk.ac.ebi.fgpt.magecomet.client.gui.tab.ErrorsTab;
import uk.ac.ebi.fgpt.magecomet.client.gui.tab.ExtractTab;
import uk.ac.ebi.fgpt.magecomet.client.gui.tab.FilterTab;
import uk.ac.ebi.fgpt.magecomet.client.gui.tab.LoadTab;
import uk.ac.ebi.fgpt.magecomet.client.gui.window.IDF_FactorValue_ValidatorWindow;
import uk.ac.ebi.fgpt.magecomet.client.gui.window.SDRF_Section_ColumnEditor;
import uk.ac.ebi.fgpt.magecomet.client.gui.window.TagCloudWindow;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.smartgwt.client.data.AdvancedCriteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.widgets.HTMLFlow;

public class GuiMediator {
  private IDF_FactorValue_ValidatorWindow idfFactorValueWindow;
  private TagCloudWindow tagCloudWindow;
  private SDRF_Section sdrfSection;
  private IDF_Section idfSection;
  private SDRF_Section_ColumnEditor sdrfSectionColumnEditor;
  private FilterTab filterTab;
  private ExtractTab extractTab;
  private LoadTab loadTab;
  private ErrorsTab errorsTab;
  private EditTab editTab;
  private String currentIDF;
  private String currentSDRF;
  private SDRF_Data sdrfData;
  private IDF_Data idfData;
  private final HTMLFlow stateCounter = new HTMLFlow();
  
  private Logger logger = Logger.getLogger(getClass().toString());
  
  public GuiMediator() {}
  
  /**
   * Mthod that adds an empty column to the SDRF table right after 'Source Name'
   * 
   * @param fieldAttribute
   *          the name of the attribute that will be surrounded by the Characteristic brackets, ie
   *          'organism_part'
   * @return the unique column key is returned
   */
  public String addCharacteristicColumnAndGetKey(String fieldAttribute) {
    return sdrfData.addNewColumn_Characteristic_AndGetKey("Characteristics[" + fieldAttribute + "]");
  }
  
  /**
   * Method to add a column, directly after 'Source Name' and adds the specified value to all records in the
   * column
   * 
   * @param fieldTitle
   *          the name of the field which should already be surrounded by tags (ie
   *          'Characteristic[organism_part]')
   * @param value
   *          the value to be filled in for all records
   */
  public void addColumnToCharacteristicAndAddValueToAllRecords(String fieldTitle, String value) {
    String uniqueKey = sdrfData.addNewColumn_Characteristic_AndGetKey(fieldTitle);
    sdrfData.addAttributeToAllRecords(uniqueKey, value);
  }
  
  /**
   * Method used to add column to clipboard and get key
   * 
   * @param visibleName
   *          this is the string that will show up in the field
   * @return the unique column key will be returned
   */
  public String addColumnToClipboardAndGetKey(String visibleName) {
    return sdrfSectionColumnEditor.addNewColumnToClipboardAndGetKey(visibleName);
  }
  
  /**
   * Method to add a column, to the end of the table
   * 
   * @param fieldAttribute
   *          the name of the field that will be surrounded by the Factor Value brackets ie 'organism_part'
   */
  public String addFactorValueColumnAndGetKey(String fieldAttribute) {
    return sdrfData.addNewColumn_FactorValue_AndGetKey("Factor Value[" + fieldAttribute + "]");
  }
  
  /**
   * @return true if the sdrfData is not null
   */
  public boolean dataHasBeenLoaded() {
    return (sdrfData != null);
  }
  
  /**
   * This method is used to filter and replace values in the SDRF
   * 
   * @param advancedCriteria
   *          the criteria to filter on
   * @param uniqueKey
   *          the target column key that the values will fill in
   * @param value
   *          the value that will be filled all the records that match the critera
   */
  public void filterReplaceRefresh(AdvancedCriteria advancedCriteria,
                                   final String uniqueKey,
                                   final String value) {
    logger.log(Level.INFO, "Filter And Replace Was Called");
    // filter
    sdrfSection.filterTable(advancedCriteria, new DSCallback() {
      @Override
      public void execute(DSResponse response, Object rawData, DSRequest request) {
        // replace
        sdrfData.setValueForSelectedRecords(sdrfSection.getRecordKeys(), uniqueKey, value);
        // refresh
        saveState();
        refreshTable();
      }
    });
    
  }
  
  /**
   * This method updates the view to show all records that match the criteria.
   * 
   * @param advancedCriteria
   *          the criteria that will be filtered on
   */
  public void filterTable(AdvancedCriteria advancedCriteria) {
    logger.log(Level.INFO, "Filter Was Called");
    sdrfSection.filterTable(advancedCriteria);
  }
  
  public ColumnField[] getAllSDRFFields() {
    return sdrfData.getAllFields();
  }
  
  public LinkedHashMap<String,String> getCharacteristicMap() {
    LinkedHashMap<String,String> characteristicValuesMap = new LinkedHashMap<String,String>();
    LinkedHashMap<String,String> columnValueMap = getColumnValueMap();
    for (String key : columnValueMap.keySet()) {
      String value = columnValueMap.get(key);
      if (value.contains("Characteristics")) {
        characteristicValuesMap.put(key, value);
      }
    }
    return characteristicValuesMap;
  }
  
  public LinkedHashMap<String,String> getColumnValueMap() {
    LinkedHashMap<String,String> columnValueMap = new LinkedHashMap<String,String>();
    for (ColumnField field : sdrfData.getAllFields()) {
      columnValueMap.put(field.getUniqueName(), field.getVisibleName());
    }
    return columnValueMap;
  }
  
  public String getCurrentIDFTitle() {
    if (currentIDF == null) {
      return "null";
    }
    return currentIDF;
  }
  
  public String getCurrentSDRFTitle() {
    if (currentSDRF == null) {
      return "null";
    }
    return currentSDRF;
  }
  
  public LinkedHashMap<String,String> getFactorValuesMap() {
    LinkedHashMap<String,String> factorValuesMap = new LinkedHashMap<String,String>();
    LinkedHashMap<String,String> columnValueMap = getColumnValueMap();
    
    for (String key : columnValueMap.keySet()) {
      String value = columnValueMap.get(key);
      if (value.contains("Factor Value")) {
        factorValuesMap.put(key, value);
      }
    }
    return factorValuesMap;
  }
  
  public String getIDFAsString() {
    return idfData.getString();
  }
  
  public String getNewColumnKey() {
    return sdrfData.getNewUniqueKey();
  }
  
  public String getSDRFAsString() {
    return sdrfData.getString();
  }
  
  public void loadIDFData(JSONObject object) {
    logger.log(Level.INFO, "Loading IDF");
    
    idfData = new IDF_Data(object);
    idfSection.setData(idfData.getAllFields(), idfData.getAllRecords());
    idfSection.setDescription(idfData.getFilterData());
    
    logger.log(Level.INFO, "Loaded IDF");
  }
  
  public void loadSDRFData(JSONObject object) {
    logger.log(Level.INFO, "Loading SDRF");
    
    sdrfData = new SDRF_Data(JSON_Tools.get2DArray("sdrfArray", object));
    DataSource data = sdrfData.getNewDataSource();
    
    sdrfSection.refreshTable(data, sdrfData.getAllFieldsAsListGridField());
    
    extractTab.setRecords(sdrfData.getAllRecords());
    
    filterTab.setData(data);
    updateColumnsInComboBoxes();
    idfFactorValueWindow = new IDF_FactorValue_ValidatorWindow(this);
    idfFactorValueWindow.updateFactorValues(getFactorValuesMap());
    
    logger.log(Level.INFO, "Loaded SDRF");
  }
  
  public void passArrayToErrorsTab(JSONArray array) {
    errorsTab.handleJSONArrayOfErrors(array);
  }
  
  public void passDataToErrorsTab(JSONObject object) {
    logger.log(Level.INFO, "Loading Errors");
    errorsTab.handelJSONObject(object);
    logger.log(Level.INFO, "Loaded Errors");
  }
  
  public void passDataToTagCloud(JSONObject jsonObject) {
    logger.log(Level.INFO, "Load textmining results to the cloud");
    
    if (jsonObject.get("whatizitIDF") != null) {
      JSONArray tagWords = jsonObject.get("whatizitIDF").isArray();
      for (int i = 0; i < tagWords.size(); i++) {
        final String word = tagWords.get(i).isString().stringValue();
        tagCloudWindow.addWord(word, 1);
      }
    }
    if ((jsonObject.get("whatizitSDRF") != null)) {
      JSONArray tagWords = jsonObject.get("whatizitSDRF").isArray();
      for (int i = 0; i < tagWords.size(); i++) {
        final String word = tagWords.get(i).isString().stringValue();
        tagCloudWindow.addWord(word, 2);
      }
    }
    tagCloudWindow.refreshTagClouds();
    
    logger.log(Level.INFO, "Textmining results passed to the cloud");
  }
  
  public void registerEditTab(EditTab editTab) {
    this.editTab = editTab;
  }
  
  public void registerErrorsTab(ErrorsTab errorsTab) {
    this.errorsTab = errorsTab;
  }
  
  public void registerExtractTab(ExtractTab extractTab) {
    this.extractTab = extractTab;
  }
  
  public void registerFilterTab(FilterTab filterTab) {
    this.filterTab = filterTab;
  }
  
  public void registerIDFSection(IDF_Section idfSection) {
    this.idfSection = idfSection;
  }
  
  public void registerLoadTab(LoadTab loadTab) {
    this.loadTab = loadTab;
  }
  
  public void registerSDRFSection(SDRF_Section sdrfSection) {
    this.sdrfSection = sdrfSection;
  }
  
  public void registerSDRFSectionColumnEditor(SDRF_Section_ColumnEditor sdrfSectionColumnEditor) {
    this.sdrfSectionColumnEditor = sdrfSectionColumnEditor;
  }
  
  public void registerTagCloud(TagCloudWindow tagCloud) {
    this.tagCloudWindow = tagCloud;
  }
  
  public void setCurrentIDFTitle(String currentIDF) {
    this.currentIDF = currentIDF;
  }
  
  public void setCurrentSDRFTitle(String currentSDRF) {
    this.currentSDRF = currentSDRF;
  }
  
  public void setFactorValuesInIDF(LinkedHashMap<String,String> factorNameToType) {
    idfData.setFactorValues(factorNameToType);
    idfSection.setData(idfData.getAllFields(), idfData.getAllRecords());
  }
  
  public void showIDFFactorValue_ValidatorWindow() {
    idfFactorValueWindow.updateFactorValues(getFactorValuesMap());
    idfFactorValueWindow.show();
  }
  
  /**
   * Updates the columns. Should be called every time the table changes
   */
  private void updateColumnsInComboBoxes() {
    LinkedHashMap<String,String> columnValueMap = new LinkedHashMap<String,String>();
    for (ColumnField field : sdrfData.getAllFields()) {
      columnValueMap.put(field.getUniqueName(), field.getVisibleName());
    }
    if (filterTab != null) {
      filterTab.updateColumnsInComboBox(columnValueMap);
    }
    if (extractTab != null) {
      extractTab.updateColumnsInComboBox(columnValueMap);
    }
  }
  
  public void updateSDRFColumnNames(ColumnField[] newArrayOfListGridFields) {
    sdrfData.updateColumnNames(newArrayOfListGridFields);
  }
  
  public String addUnitColumnAndGetKey(String fieldAttribute) {
    return sdrfData.addNewColumn_Characteristic_AndGetKey("Unit[" + fieldAttribute + "]");
  }
  
  public void saveState() {
    sdrfData.saveState();
    filterTab.setData(sdrfData.getNewDataSource());
    extractTab.setRecords(sdrfData.getAllRecords());
    updateStateCounter();
    
  }
  
  public void refreshTable() {
    // The datasource is like a physical mirror(the one that reflects). In this case, it shows the
    // user what is in the SDRF_Data object.
    
    DataSource data = sdrfData.getNewDataSource();
    sdrfSection.refreshTable(data, sdrfData.getAllFieldsAsListGridField());
    
    filterTab.setData(data);
    extractTab.setRecords(sdrfData.getAllRecords());
    
    updateColumnsInComboBoxes();
    logger.log(Level.INFO, "Table Refreshed");
  }
  
  public void loadPreviousState() {
    sdrfData.undo();
    refreshTable();
    updateStateCounter();
    
  }
  
  public void redo() {
    sdrfData.redo();
    refreshTable();
    updateStateCounter();
  }
  
  public void updateStateCounter() {
    stateCounter.setContents("State: " + sdrfData.getState());
  }
  
  public HTMLFlow getStateCounter() {
    return stateCounter;
  }
  
  public void setCell(String recordKey, String fieldKey, String newValue) {
    sdrfData.setCell(recordKey, fieldKey, newValue);
  }
  
  public void resetTagCloud() {
    tagCloudWindow.reset();
    logger.info("TagCloud reset");
  }
}
