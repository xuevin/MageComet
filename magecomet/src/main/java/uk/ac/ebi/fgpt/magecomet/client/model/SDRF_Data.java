package uk.ac.ebi.fgpt.magecomet.client.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;


import com.smartgwt.client.core.DataClass;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.types.AutoFitWidthApproach;
import com.smartgwt.client.types.FieldType;
import com.smartgwt.client.widgets.grid.ListGridField;

public class SDRF_Data {
  private Logger logger = Logger.getLogger(getClass().toString());
  
  private ColumnField[] allFields;
  private HashMap<String,RowRecord> allRecords;
  private int uniqueKeyCount;
  private ArrayList<HashMap<String,RowRecord>> recordhistory;
  private ArrayList<ColumnField[]> fieldHistory;
  
  private int currentState;
  
  public SDRF_Data(String[][] twoDimensionalArray) {
    allFields = fieldArrayToColumnFieldArray(twoDimensionalArray[0]);
    allRecords = twoDimArrayToRowRecordArray(twoDimensionalArray);
    
    uniqueKeyCount = twoDimensionalArray[0].length;
    
    recordhistory = new ArrayList<HashMap<String,RowRecord>>();
    fieldHistory = new ArrayList<ColumnField[]>();
    currentState = 0;
    saveState();
  }
  
  public ColumnField[] getAllFields() {
    return allFields;
  }
  
  /*
   * No tests because this is uses smartgwt components
   */
  public ListGridField[] getAllFieldsAsListGridField() {
    ListGridField[] array = new ListGridField[allFields.length];
    for (int i = 0; i < allFields.length; i++) {
      ListGridField lgf = new ListGridField(allFields[i].getUniqueName(), allFields[i].getVisibleName());
      lgf.setAutoFitWidthApproach(AutoFitWidthApproach.BOTH);
      lgf.setAutoFitWidth(allFields[i].isAutofit());
      lgf.setHidden(allFields[i].isHidden());
      array[i] = lgf;
    }
    return array;
  }
  
  public HashMap<String,RowRecord> getAllRecords() {
    return allRecords;
  }
  
  private ColumnField[] fieldArrayToColumnFieldArray(String[] fieldArray) {
    
    // The field is one longer because I need to add a key field
    ColumnField[] arrayOfFields = new ColumnField[fieldArray.length + 1];
    
    for (int i = 0; i < fieldArray.length; i++) {
      arrayOfFields[i + 1] = new ColumnField(i + 1 + "", fieldArray[i]);
      if (GlobalConfigs.shouldExclude(arrayOfFields[i + 1].getVisibleName())) {
        arrayOfFields[i + 1].setHidden(true);
      } else {
        arrayOfFields[i + 1].setAutofit(true);
      }
    }
    
    // Make a primary key
    ColumnField key = new ColumnField("key", "Key");
    arrayOfFields[0] = key;
    return arrayOfFields;
    
  }
  
  private HashMap<String,RowRecord> twoDimArrayToRowRecordArray(String[][] twoDimArray) {
    // Number of rows is one less because row zero contains data about the
    // field name.
    int numberOfRows = twoDimArray.length;
    
    HashMap<String,RowRecord> mapOfRecords = new HashMap<String,RowRecord>();
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
      String[] row = twoDimArray[i];
      
      RowRecord newRecord = new RowRecord();
      for (int j = 0; j < row.length; j++) {
        newRecord.put((j + 1) + "", row[j]);
      }
      newRecord.put("key", i + "");
      mapOfRecords.put("" + i, newRecord);
      // arrayOfRecords[i-1]=newRecord;
    }
    return mapOfRecords;
  }
  
  public void addAttributeToAllRecords(final String uniqueColumnKey, final String value) {
    for (RowRecord record : allRecords.values()) {
      record.put(uniqueColumnKey, value);
    }
  }
  
  public void setValueForSelectedRecords(String[] listOfRowKeys, String uniqueColumnKey, String value) {
    String intermediate = value + ""; // Strange bug where the compiled
    // version doesn't identify this as text
    logger.log(Level.INFO, listOfRowKeys.length + " records Will be set to " + value);
    
    for (int i = 0; i < listOfRowKeys.length; i++) {
      try {
        allRecords.get(listOfRowKeys[i]).put(uniqueColumnKey, intermediate);
      } catch (NullPointerException e) {
        System.err.println("Fetching Did Not Finish");
        e.printStackTrace();
      }
    }
  }
  
  public String addNewColumn_Characteristic_AndGetKey(String visibleName) {
    String uniqueKey = getNewUniqueKey();
    ColumnField newColumnField = new ColumnField(uniqueKey, visibleName);
    
    ColumnField[] newFields = new ColumnField[allFields.length + 1];
    // Find the right place to put this new field.
    int offset = 0;
    for (int i = 0; i < allFields.length; i++) {
      newFields[i + offset] = allFields[i];
      if (allFields[i].getVisibleName().contains("Source Name")) {
        offset = 1;
        newFields[i + 1] = newColumnField;
      }
    }
    allFields = newFields;
    return uniqueKey;
  }
  
  public String addNewColumn_FactorValue_AndGetKey(String visibleName) {
    String uniqueKey = getNewUniqueKey();
    
    ColumnField newColumnField = new ColumnField(uniqueKey, visibleName);
    
    ColumnField[] newFields = new ColumnField[allFields.length + 1];
    
    // Find the right place to put this new field
    for (int i = 0; i < allFields.length; i++) {
      newFields[i] = allFields[i];
    }
    newFields[allFields.length] = newColumnField;
    
    allFields = newFields;
    return uniqueKey;
  }
  
  /**
   * This gets a unique column key but does not add it to the list. Used for clipboard.
   * 
   * @return
   */
  public String getNewUniqueKey() {
    uniqueKeyCount++;
    return uniqueKeyCount + "";
  }
  
  public void updateColumnNames(ColumnField[] newArrayOfFields) {
    allFields = newArrayOfFields;
  }
  
  public void saveState() {
    logger.log(Level.INFO, "A call was made to save the state");
    
    // For recordHistory
    if (recordhistory.size() - 1 > currentState) {
      Iterator<HashMap<String,RowRecord>> historyIterator = recordhistory.iterator();
      
      while (historyIterator.hasNext()) {
        int i = recordhistory.indexOf(historyIterator.next());
        if (i <= currentState) {
          // do nothing
        } else {
          historyIterator.remove();
          logger.log(Level.INFO, "Deleting State: " + i);
        }
        i++;
      }
    }
    recordhistory.add(deepCopyData(allRecords));
    
    // For fieldHistory
    if (fieldHistory.size() - 1 > currentState) {
      Iterator<ColumnField[]> historyIterator = fieldHistory.iterator();
      
      while (historyIterator.hasNext()) {
        int i = fieldHistory.indexOf(historyIterator.next());
        if (i <= currentState) {
          // do nothing
        } else {
          historyIterator.remove();
          logger.log(Level.INFO, "Deleting State: " + i);
        }
        i++;
      }
    }
    fieldHistory.add(deepCopyData(allFields));
    
    currentState = (recordhistory.size() - 1);
    logger.log(Level.INFO, "Saved at: " + currentState);
  }
  
  public void undo() {
    logger.log(Level.INFO, "A call was made to undo the state");
    if (currentState > 0) {
      currentState--;
      allRecords = deepCopyData(recordhistory.get(currentState));
      allFields = deepCopyData(fieldHistory.get(currentState));
      logger.log(Level.INFO, "Restore back to " + currentState);
    }
    
  }
  
  public void redo() {
    logger.log(Level.INFO, "A call was made to redo the state");
    if ((currentState + 1) < recordhistory.size()) {
      currentState++;
      allRecords = deepCopyData(recordhistory.get(currentState));
      allFields = deepCopyData(fieldHistory.get(currentState));
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
  
  public static HashMap<String,RowRecord> deepCopyData(HashMap<String,RowRecord> recordsToCopy) {
    HashMap<String,RowRecord> newCopy = new HashMap<String,RowRecord>();
    for (String key : recordsToCopy.keySet()) {
      newCopy.put(key, recordsToCopy.get(key).deepClone());
    }
    return newCopy;
  }
  
  public static ColumnField[] deepCopyData(ColumnField[] recordsToCopy) {
    ColumnField[] newCopy = new ColumnField[recordsToCopy.length];
    for (int i = 0; i < recordsToCopy.length; i++) {
      newCopy[i] = recordsToCopy[i].deepClone();
    }
    return newCopy;
  }
  
  public String getString() {
    
    String sdrfAsString = "";
    if (allFields.length != 0) {
      
      // Print out all fields
      for (ColumnField column : allFields) {
        if (!column.getVisibleName().equals("Key")) {
          sdrfAsString += (column.getVisibleName() + "\t");
        }
      }
      // Remove last tab and make it a new line
      sdrfAsString = sdrfAsString.substring(0, sdrfAsString.length() - 1);
      sdrfAsString += "\n";
      
      for (RowRecord record : allRecords.values()) {
        for (ColumnField column : allFields) {
          if (!column.getVisibleName().equals("Key")) {
            
            if (record.get(column.getUniqueName()) == null) {
              sdrfAsString += "\t";
            } else {
              sdrfAsString += record.get(column.getUniqueName()) + "\t";
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
  
  public int getState() {
    return currentState;
  }
  
  // FIXME
  // Can't test this
  public DataSource getNewDataSource() {
    DataSource data = new DataSource();
    data.setTestData(convertToDataClassArray(allRecords));
    data.setFields(convertToFieldArray(allFields));
    data.setClientOnly(true);
    return data;
    
  }
  
  private static DataSourceField[] convertToFieldArray(ColumnField[] allFields) {
    DataSourceField[] array = new DataSourceField[allFields.length];
    for (int i = 1; i < allFields.length; i++) {
      DataSourceField field = new DataSourceField(allFields[i].getUniqueName(), FieldType.TEXT, allFields[i]
          .getVisibleName());
      array[i] = field;
    }
    array[0] = new DataSourceField(allFields[0].getUniqueName(), FieldType.INTEGER, allFields[0]
        .getVisibleName());
    array[0].setPrimaryKey(true);
    
    return array;
  }
  
  private static DataClass[] convertToDataClassArray(HashMap<String,RowRecord> allRecords) {
    DataClass[] array = new DataClass[allRecords.size()];
    int i = 0;
    for (RowRecord record : allRecords.values()) {
      DataClass newDataClass = new DataClass();
      for (String key : record.keySet()) {
        newDataClass.setAttribute(key, record.get(key));
      }
      array[i] = newDataClass;
      i++;
    }
    return array;
  }
  
  public void setCell(String recordKey, String fieldKey, String newValue) {
    allRecords.get(recordKey).put(fieldKey, newValue);
  }
}
