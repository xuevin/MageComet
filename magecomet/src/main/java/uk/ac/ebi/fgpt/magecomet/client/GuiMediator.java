package uk.ac.ebi.fgpt.magecomet.client;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
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
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;

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
	private LinkedHashSet<MageTabState> history = new LinkedHashSet<MageTabState>();

	private Logger logger = Logger.getLogger(getClass().toString());

	public GuiMediator() {
	}

	public String addCharacteristicColumnAndGetKey(String fieldAttribute) {
		return sdrfData
				.addNewColumn_Characteristic_AndGetKey("Characteristics["
						+ fieldAttribute + "]");
	}

	/**
	 * Adds a characteristic column, directly after sourceName and adds the
	 * specified value to all records
	 * 
	 * @param fieldTitle
	 *            the name of the field. WILL NOT BE SURROUNDED
	 * @param value
	 *            the value to be filled in for all records
	 */
	public void addColumnToCharacteristicAndAddValueToAllRecords(
			String fieldTitle, String value) {
		String uniqueKey = sdrfData
				.addNewColumn_Characteristic_AndGetKey(fieldTitle);
		sdrfData.addAttributeToAllRecords(uniqueKey, value);
	}

	@Deprecated
	public void addColumnToClipboardAndAddValueToAllRecords(String fieldTitle,
			String value) {
		String uniqueKey = sdrfSectionColumnEditor
				.addNewColumnToClipboardAndGetKey(fieldTitle);
		sdrfData.addAttributeToAllRecords(uniqueKey, value);
		sdrfSectionColumnEditor.show();
	}

	public String addColumnToClipboardAndGetKey(String title) {
		return sdrfSectionColumnEditor.addNewColumnToClipboardAndGetKey(title);
	}

	public String addFactorValueColumnAndGetKey(String fieldAttribute) {
		return sdrfData.addNewColumn_FactorValue_AndGetKey("Factor Value["
				+ fieldAttribute + "]");
	}

	public boolean dataHasBeenLoaded() {
		return (sdrfData != null);
	}

	public void filterReplaceRefresh(AdvancedCriteria advancedCriteria,
			final String uniqueKey, final String value) {
		logger.log(Level.INFO, "Filter And Replace Was Called");
		// filter
		sdrfSection.setFilterCritera(advancedCriteria);
		// replace
		sdrfData.setValueForSelectedRecords(sdrfSection.getListOfRecords(),
				uniqueKey, value);
		// refresh
		refreshTable();
	}

	public void filterTable(AdvancedCriteria advancedCriteria) {
		logger.log(Level.INFO, "Filter Was Called");

		sdrfSection.filterTable(advancedCriteria);
	}

	public ListGridField[] getAllSDRFFields() {
		return sdrfData.getAllFields();
	}

	// *****************************************
	// End Registration
	// *****************************************

	public LinkedHashMap<String, String> getCharacteristicMap() {
		LinkedHashMap<String, String> characteristicValuesMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> columnValueMap = getColumnValueMap();
		for (String key : columnValueMap.keySet()) {
			String value = columnValueMap.get(key);
			if (value.contains("Characteristics")) {
				characteristicValuesMap.put(key, value);
			}
		}
		return characteristicValuesMap;
	}

	public LinkedHashMap<String, String> getColumnValueMap() {
		LinkedHashMap<String, String> columnValueMap = new LinkedHashMap<String, String>();
		for (ListGridField field : sdrfData.getAllFields()) {
			columnValueMap.put(field.getName(), field.getTitle());
		}
		return columnValueMap;
	}

	public String getCurrentIDFTitle() {
		return currentIDF;
	}

	public String getCurrentSDRFTitle() {
		return currentSDRF;
	}

	public LinkedHashMap<String, String> getFactorValuesMap() {
		LinkedHashMap<String, String> factorValuesMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> columnValueMap = getColumnValueMap();

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
		return sdrfData.getNewColumnKey();
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

		sdrfData = new SDRF_Data(object);
		sdrfSection.setData(sdrfData.getDataSource(), sdrfData.getAllFields());
		filterTab.setData(sdrfData.getDataSource());
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

	@Deprecated
	public void passSDRFTableToExtractTab(ListGrid sdrfTable) {
		extractTab.setRecords(sdrfData.getAllRecords(), sdrfTable);
	}

	public void refreshTable() {
		logger.log(Level.INFO, "A Call has been made to refresh the table");
		sdrfSection.refreshTable(sdrfData.getDataSource(), sdrfData
				.getAllFields());
		filterTab.setData(sdrfData.getDataSource());
		updateColumnsInComboBoxes();
		logger.log(Level.INFO, "Table has been refreshed");
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

	public void registerSDRFSectionColumnEditor(
			SDRF_Section_ColumnEditor sdrfSectionColumnEditor) {
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

	public void setFactorValuesInIDF(
			LinkedHashMap<String, String> factorNameToType) {
		idfData.setFactorValues(factorNameToType);
		idfSection.setData(idfData.getAllFields(), idfData.getAllRecords());
	}

	// *****************************************
	// End Registration
	// *****************************************

	public void showIDFFactorValue_ValidatorWindow() {
		idfFactorValueWindow.updateFactorValues(getFactorValuesMap());
		idfFactorValueWindow.show();
	}

	/**
	 * Updates the columns. Should be called every time the table changes
	 */
	private void updateColumnsInComboBoxes() {
		// Update Data source in table
		LinkedHashMap<String, String> columnValueMap = new LinkedHashMap<String, String>();
		for (ListGridField field : sdrfData.getAllFields()) {
			columnValueMap.put(field.getName(), field.getTitle());
		}
		if (filterTab != null) {
			filterTab.updateColumnsInComboBox(columnValueMap);
		}
		if (extractTab != null) {
			extractTab.updateColumnsInComboBox(columnValueMap);
		}
	}

	public void updateSDRFColumnNames(ListGridField[] newArrayOfListGridFields) {
		sdrfData.updateColumnNames(newArrayOfListGridFields);
		sdrfData.updateDataSource(newArrayOfListGridFields);
	}

	public String addUnitColumnAndGetKey(String fieldAttribute) {
		return sdrfData.addNewColumn_Characteristic_AndGetKey("Unit["
				+ fieldAttribute + "]");
	}

	public void saveState() {
		logger.log(Level.INFO, "A call was made to save the state");
	}

	public void loadState() {
		logger.log(Level.INFO, "A call was made to load the previous state");
	}
}
