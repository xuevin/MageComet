package uk.ac.ebi.fgpt.magecomet.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.smartgwt.client.core.DataClass;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.types.AutoFitWidthApproach;
import com.smartgwt.client.types.FieldType;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * This is a class used to modify a SDRF. All modifications to a SDRF should be called through this class.
 * 
 * @author vincent@ebi.ac.uk
 * 
 */
public class SDRFData {
  /**
   * This array represents the visible order of the columns
   */
  private ListGridField[] listOfAllFields;
  /**
   * This is the Datasource object that represents the data stored in the SDRF data
   */
  private DataSource data;
  private int numColumnsBeforeModification;
  private int uniqueKeyCount;
  private ArrayList<DataClass[]> history;
  private int currentState;
  private DataClass[] mostCurrentData;
  
  private Logger logger = Logger.getLogger("SDRF_Data");
  
  public SDRFData(JSONObject jsonObject) {
    JSONArray jsonArray = jsonObject.get("sdrfArray").isArray();
    listOfAllFields = JSONToListGridField(jsonArray);
    
    history = new ArrayList<DataClass[]>();
    currentState = 0;
    
    data = new DataSource("sdrf_ds");
    data.setFields(JSONToDataSourceField(jsonArray)); // Need this to this for filtering
    
    mostCurrentData = JSONToDataClassArray(jsonArray);
    data.setTestData(mostCurrentData);
    data.setClientOnly(true);
    saveState();
  }
  @Deprecated
  public void addAttributeToAllRecords(final String uniqueKey, final String value) {
    for (DataClass record : mostCurrentData) {
      record.setAttribute(uniqueKey, value);
    }
    updateDataSource();
  }
  
  public String addNewColumn_Characteristic_AndGetKey(String fieldTitle) {
    int uniqueKey = getNewUniqueKey();
    ListGridField newColumn = new ListGridField();
    newColumn.setName(uniqueKey + "");
    newColumn.setTitle(fieldTitle);
    newColumn.setAutoFitWidthApproach(AutoFitWidthApproach.BOTH);
    newColumn.setAutoFitWidth(true);
    
    ListGridField[] newFields = new ListGridField[listOfAllFields.length + 1];
    
    // Find the right place to put this new field.
    int offset = 0;
    for (int i = 0; i < listOfAllFields.length; i++) {
      newFields[i + offset] = listOfAllFields[i];
      if (listOfAllFields[i].getTitle().contains("Source Name")) {
        offset = 1;
        newFields[i + 1] = newColumn;
      }
    }
    listOfAllFields = newFields;
    updateDataSource();
    return uniqueKey + "";
  }
  
  private void updateDataSource() {
    data = new DataSource(); //Needs to make a new datasource because you can't add fields once the DS is created.
    
    // Update the fields
    DataSourceField[] fields = new DataSourceField[listOfAllFields.length];
    for (int i = 1; i < listOfAllFields.length; i++) {
      fields[i] = new DataSourceField(listOfAllFields[i].getName(), FieldType.TEXT, listOfAllFields[i]
          .getTitle());
    }
    // Make a primary key
    DataSourceField key = new DataSourceField("key", FieldType.INTEGER, "Key");
    key.setPrimaryKey(true);
    fields[0] = key;
    data.setFields(fields);
    
    
    // Update the data
    data.setTestData(mostCurrentData);
    data.setClientOnly(true);
    saveState();
  }
  
  public String addNewColumn_FactorValue_AndGetKey(String fieldAttribute) {
    int uniqueKey = getNewUniqueKey();
    ListGridField newColumn = new ListGridField();
    newColumn.setName(uniqueKey + "");
    newColumn.setTitle(fieldAttribute);
    newColumn.setAutoFitWidthApproach(AutoFitWidthApproach.BOTH);
    newColumn.setAutoFitWidth(true);
    
    ListGridField[] newFields = new ListGridField[listOfAllFields.length + 1];
    
    // Find the right place to put this new field
    for (int i = 0; i < listOfAllFields.length; i++) {
      newFields[i] = listOfAllFields[i];
    }
    newFields[listOfAllFields.length] = newColumn;
    
    listOfAllFields = newFields;
    updateDataSource();
    return uniqueKey + "";
  }
  
  public void updateColumnNames(ListGridField[] newArrayOfListGridFields) {
    listOfAllFields = newArrayOfListGridFields;
    updateDataSource();
  }
  
  public void updateDataSource(ListGridField[] newListGridFields) {
    listOfAllFields = newListGridFields;
    updateDataSource();
  }
  @Deprecated
  public ListGridField[] getAllFields() {
    return listOfAllFields;
  }
  
  public String getNewColumnKey() {
    int uniqueKey = getNewUniqueKey();
    return uniqueKey + "";
  }
  
  public String getString() {
    
    String sdrfAsString = "";
    if (listOfAllFields.length != 0) {
      
      // Print out all fields
      for (ListGridField column : listOfAllFields) {
        if (!column.getTitle().equals("Key")) {
          sdrfAsString += (column.getTitle() + "\t");
        }
      }
      // Remove last tab and make it a new line
      sdrfAsString = sdrfAsString.substring(0, sdrfAsString.length() - 1);
      sdrfAsString += "\n";
      
      for (DataClass record : data.getTestData()) {
        for (ListGridField column : listOfAllFields) {
          if (!column.getTitle().equals("Key")) {
            
            if (record.getAttribute(column.getName()) == null) {
              sdrfAsString += "\t";
            } else {
              sdrfAsString += record.getAttribute(column.getName()) + "\t";
            }
          }
        }
        
        sdrfAsString = sdrfAsString.substring(0, sdrfAsString.length() - 1);// Remove last tab and make
        // it a new line
        sdrfAsString += "\n";
      }
      sdrfAsString = sdrfAsString.trim();// Remove last new line
      
      return sdrfAsString;
    }
    return "";
  }
  
  /**
   * Converts a JSON array into an array of ListGridRecords.
   * 
   * Each attribute is retrieved through it's unique key assigned. Fields also have the unique key and that is
   * how the relationship is preserved
   * 
   * @param jsonArrayOfRows
   * @return an array of ListGridRecords are returned. This is used to populate the data source but it does
   *         not determine the order of the columns
   */
  private DataClass[] JSONToDataClassArray(JSONArray jsonArrayOfRows) {
    // Number of rows is one less because row zero contains data about the
    // field name.
    int numberOfRows = jsonArrayOfRows.size();
    
    DataClass[] arrayOfRecords = new DataClass[numberOfRows - 1];
    // ListGridRecord[] arrayOfRecords = new ListGridRecord[numberOfRows-1];
    
    // ------------------------------
    // key 1 2 3 ...
    // 0 1 2 3 ...
    // 1 1 2 3 ...
    // ------------------------------
    // For each row in the JSON array, parse it, create a new record, and
    // add it to the array of records.
    for (int i = 1; i < numberOfRows; i++) {
      // The First Row is not a record, it is a field
      JSONArray row = jsonArrayOfRows.get(i).isArray();
      
      DataClass newRecord = new DataClass();
      for (int j = 0; j < row.size(); j++) {
        newRecord.setAttribute((j + 1) + "", row.get(j).isString().stringValue());
      }
      newRecord.setAttribute("key", i);
      arrayOfRecords[i - 1] = newRecord;
      // arrayOfRecords[i-1]=newRecord;
    }
    return arrayOfRecords;
  }
  
  /**
   * @param jsonArray
   * @return an array of DataSourceFields are returned. This is used to populate the data source and is not
   *         used to determine the order of the columns
   */
  private DataSourceField[] JSONToDataSourceField(JSONArray jsonArray) {
    JSONArray firstRow = jsonArray.get(0).isArray();
    // Plus one is to put the key in the front
    DataSourceField[] arrayOfFields = new DataSourceField[firstRow.size() + 1];
    
    for (int i = 0; i < firstRow.size(); i++) {
      arrayOfFields[i + 1] = new DataSourceField(i + 1 + "", FieldType.TEXT, firstRow.get(i).isString()
          .stringValue());
    }
    
    // Make a primary key
    DataSourceField key = new DataSourceField("key", FieldType.INTEGER, "Key");
    key.setPrimaryKey(true);
    arrayOfFields[0] = key;
    return arrayOfFields;
  }
  
  /**
   * @param array
   * @return an array of ListGridField's are returned. This array is used to determine the order of the
   *         columns.
   * 
   * <br>
   *         [ArrayIndex]uniquekey...(name) <br>
   *         [0]key...( Key) <br>
   *         [1]1...(Term Source) <br>
   *         [2]26...(Comment) <br>
   *         [3]2...(Sample Name) <br>
   *         etc.
   */
  private ListGridField[] JSONToListGridField(JSONArray array) {
    JSONArray firstRow = array.get(0).isArray();
    // Plus one is to put the key in the front
    ListGridField[] arrayOfFields = new ListGridField[firstRow.size() + 1];
    
    for (int i = 0; i < firstRow.size(); i++) {
      arrayOfFields[i + 1] = new ListGridField(i + 1 + "", firstRow.get(i).isString().stringValue());
      // Should the column be hidden? If true hide, else set it to auto
      // width
      if (GlobalConfigs.shouldExclude(arrayOfFields[i + 1].getAttribute("title"))) {
        arrayOfFields[i + 1].setHidden(true);
      } else {
        arrayOfFields[i + 1].setAutoFitWidthApproach(AutoFitWidthApproach.BOTH);
        arrayOfFields[i + 1].setAutoFitWidth(true);
      }
    }
    // At first, the unique key count is instantiated to the number of
    // fields present
    numColumnsBeforeModification = firstRow.size();
    uniqueKeyCount = numColumnsBeforeModification;
    
    // Make a primary key
    ListGridField key = new ListGridField("key", "Key");
    key.setAutoFitWidthApproach(AutoFitWidthApproach.BOTH);
    key.setAutoFitWidth(true);
    arrayOfFields[0] = key;
    return arrayOfFields;
  }
  
  /**
   * Gets a unique key that represents a column
   * 
   * @return a new unique key that represents a column
   */
  private int getNewUniqueKey() {
    uniqueKeyCount++;
    return uniqueKeyCount;
  }
  
  public DataSource getDataSource() {
    return data;
  }
  
  public ListGridRecord[] getAllRecords() {
    
    ListGridRecord[] recordArray = new ListGridRecord[mostCurrentData.length];
    for (int i = 0; i < mostCurrentData.length; i++) {
      recordArray[i] = new ListGridRecord(mostCurrentData[i].getJsObj());
    }
    return recordArray;
  }
  
  // private DataClass[] map2Array(Collection<DataClass> values) {
  // DataClass[] returnArray = new DataClass[values.size()];
  // Iterator<DataClass> iterator = values.iterator();
  // int i = 0;
  // while (iterator.hasNext()) {
  // returnArray[i] = iterator.next();
  // i++;
  // }
  // return returnArray;
  // }
  public void setValueForSelectedRecords(RecordList listOfRecords, String uniqueKey, String value) {
    String intermediate = value + ""; // Strange bug where the compiled
    // version doesn't identify this as text
    logger.log(Level.INFO, listOfRecords.getLength() + " records Will be set to " + value);
    
    HashMap<String,DataClass> dataMap = new HashMap<String,DataClass>();
    
    for (int i = 0; i < mostCurrentData.length; i++) {
      dataMap.put(mostCurrentData[i].getAttribute("key"), mostCurrentData[i]);
    }
    
    for (int i = 0; i < listOfRecords.getLength(); i++) {
      try {
        dataMap.get(listOfRecords.get(i).getAttribute("key")).setAttribute(uniqueKey, intermediate);
      } catch (NullPointerException e) {
        System.err.println("Fetching Did Not Finish");
        e.printStackTrace();
      }
    }
    updateDataSource();
  }
  
  private static DataClass[] deepCopyData(DataSource data, ListGridField[] listOfAllFields) {
    DataClass[] newData = new DataClass[data.getTestData().length];
    
    if (listOfAllFields.length != 0) {
      int i = 0;
      for (DataClass record : data.getTestData()) {
        newData[i] = new DataClass();
        for (ListGridField column : listOfAllFields) {
          newData[i].setAttribute(column.getName(), record.getAttribute(column.getName()));
        }
        i++;
      }
    }
    return newData;
  }
  
  public void saveState() {
    logger.log(Level.INFO, "A call was made to save the state");
    if (history.size() - 1 > currentState) {
      Iterator<DataClass[]> historyIterator = history.iterator();
      
      while (historyIterator.hasNext()) {
        int i = history.indexOf(historyIterator.next());
        if (i <= currentState) {
          // do nothing
        } else {
          historyIterator.remove();
          logger.log(Level.INFO, "Deleting State: " + i);
        }
        i++;
      }
    }
    System.out.println("Size:" + history.size());
    history.add(deepCopyData(data, listOfAllFields));
    currentState = (history.size() - 1);
    
    logger.log(Level.INFO, "Saved at: " + currentState);
  }
  
  public void undo() {
    logger.log(Level.INFO, "A call was made to undo the state");
    if (currentState > 0) {
      currentState--;
      data.setTestData(clone(history.get(currentState), listOfAllFields));
      logger.log(Level.INFO, "Restore back to " + currentState);
    }
    
  }
  
  private static DataClass[] clone(DataClass[] dataToClone, ListGridField[] listOfAllFields) {
    DataClass[] cloneRecord = new DataClass[dataToClone.length];
    int i = 0;
    for (DataClass record : dataToClone) {
      cloneRecord[i] = new DataClass();
      for (ListGridField column : listOfAllFields) {
        cloneRecord[i].setAttribute(column.getName(), record.getAttribute(column.getName()));
      }
      i++;
    }
    return cloneRecord;
  }
  
  public void redo() {
    logger.log(Level.INFO, "A call was made to redo the state");
    if ((currentState + 1) < history.size()) {
      currentState++;
      data.setTestData(clone(history.get(currentState), listOfAllFields));
      logger.log(Level.INFO, "Restore back to " + currentState);
    }
  }
  
  public int getState() {
    return currentState;
  }
}
