package uk.ac.ebi.fgpt.magecomet.client.model;

import java.util.LinkedHashMap;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * This class describes the IDF data.
 * 
 * @author Vincent Xue
 *
 */
public class IDF_Data {
  private String filteredOutData;
  private ListGridRecord[] listOfAllRecords;
  private ListGridField[] listOfAllFields;
  
  @Deprecated
  public IDF_Data(JSONObject object) {
    JSONArray jsonArray = object.get("idfArray").isArray();
    listOfAllRecords = getAllRecordsFromJSONArray(jsonArray);
    listOfAllFields = getFieldsFromJSONObject(jsonArray);
  }
  
  public IDF_Data(String[][] array) {
    listOfAllRecords = getAllRecordsFromArray(array);
    listOfAllFields = getFieldsFromArray(array);
  }
  
  public String getString() {
    String isfAsString = "";
    if (listOfAllFields.length != 0) {
      // //Print out all fields
      ListGridField[] listOfFields = listOfAllFields;
      for (ListGridRecord record : listOfAllRecords) {
        for (ListGridField column : listOfFields) {
          if (record.getAttribute(column.getName()) == null) {
            isfAsString += "\t";
          } else {
            isfAsString += record.getAttribute(column.getName()) + "\t";
          }
        }
        isfAsString = isfAsString.trim();// Remove last tab and make it
        // a new line
        isfAsString += "\n";
      }
      isfAsString = isfAsString.trim();// Remove last new line
      
      return isfAsString;
    }
    return "";
    
  }
  
  public ListGridRecord[] getAllRecords() {
    return listOfAllRecords;
  }
  
  public ListGridField[] getAllFields() {
    return listOfAllFields;
  }
  
  public String getFilterData() {
    return filteredOutData;
  }
  
  private ListGridRecord[] getAllRecordsFromArray(String[][] array) {
    ListGridRecord[] listOfRecords = new ListGridRecord[array.length];
    
    // Get the data from the rows and make each a record
    for (int row = 0; row < array.length; row++) {
      
      String[] columnArray = array[row];
      ListGridRecord tempRow = new ListGridRecord();
      for (int column = 0; column < columnArray.length; column++) {
        tempRow.setAttribute(column + "", columnArray[column]);
      }
      listOfRecords[row] = tempRow;
      
      // In order to prevent iterating text two times, pull out the
      // information
      // during the first iteration.
      
      // Find the column with experiment description
      if (columnArray[0].equals("Experiment Description")) {
        filteredOutData = (columnArray[1]);
      }
      // Find the column with Protocol descriptions
      if (columnArray[0].equals("Protocol Description")) {
        int i = 1;
        while (i < columnArray.length) {
          filteredOutData += "<br>=============================<br>";
          filteredOutData += (columnArray[i]);
          i++;
        }
      }
    }
    return listOfRecords;
  }
  
  @Deprecated
  private ListGridRecord[] getAllRecordsFromJSONArray(JSONArray jsonArray) {
    ListGridRecord[] listOfRecords = new ListGridRecord[jsonArray.size()];
    
    // Get the data from the rows and make each a record
    for (int row = 0; row < jsonArray.size(); row++) {
      
      JSONArray columnArray = jsonArray.get(row).isArray();
      ListGridRecord tempRow = new ListGridRecord();
      for (int column = 0; column < columnArray.size(); column++) {
        tempRow.setAttribute(column + "", columnArray.get(column).isString().stringValue());
      }
      listOfRecords[row] = tempRow;
      
      // In order to prevent iterating text two times, pull out the
      // information
      // during the first iteration.
      
      // Find the column with experiment description
      if (columnArray.get(0).isString().stringValue().equals("Experiment Description")) {
        filteredOutData = (columnArray.get(1).isString().stringValue());
      }
      // Find the column with Protocol descriptions
      if (columnArray.get(0).isString().stringValue().equals("Protocol Description")) {
        int i = 1;
        while (i < columnArray.size()) {
          filteredOutData += "<br>=============================<br>";
          filteredOutData += (columnArray.get(i).isString().stringValue());
          i++;
        }
      }
    }
    return listOfRecords;
  }
  
  private ListGridField[] getFieldsFromArray(String[][] array) {
    
    int longestRow = 0;
    for (int i = 0; i < array.length; i++) {
      if (longestRow < array[i].length) {
        longestRow = array[i].length;
      }
    }
    
    return makeFields(longestRow);
  }
  
  @Deprecated
  private ListGridField[] getFieldsFromJSONObject(JSONArray jsonArray) {
    
    int longestRow = 0;
    for (int i = 0; i < jsonArray.size(); i++) {
      if (longestRow < jsonArray.get(i).isArray().size()) {
        longestRow = jsonArray.get(i).isArray().size();
      }
    }
    
    return makeFields(longestRow);
  }
  
  private ListGridField[] makeFields(int longestRow) {
    ListGridField[] listOfFields = new ListGridField[longestRow];
    // Make the columns
    for (int i = 1; i < longestRow; i++) {
      listOfFields[i] = new ListGridField(("" + i), "");
      listOfFields[i].setEditorType(new TextAreaItem());
    }
    listOfFields[0] = new ListGridField("0", "Field");
    listOfFields[0].setWidth(240);
    return listOfFields;
  }
  
  public void setFactorValues(LinkedHashMap<String,String> factorNameToType) {
    boolean addedFactorName = false;
    boolean addedFactorType = false;
    
    if (listOfAllFields.length < (factorNameToType.size() + 1)) {
      listOfAllFields = makeFields(factorNameToType.size() + 1);
    }
    
    // Find out if the records for FactorValueName and FactorValue Type
    // exist.
    for (ListGridRecord record : listOfAllRecords) {
      if (record.getAttribute("0").equals("Experimental Factor Name")) {
        addedFactorName = true;
        int i = 1;
        for (String key : factorNameToType.keySet()) {
          record.setAttribute(i + "", key.substring(key.indexOf("[") + 1, key.indexOf("]")));
          i++;
        }
      }
      if (record.getAttribute("0").equals("Experimental Factor Type")) {
        addedFactorType = true;
        int i = 1;
        for (String key : factorNameToType.keySet()) {
          record.setAttribute(i + "", factorNameToType.get(key));
          i++;
        }
      }
    }
    // Case that there there is no row with Factor Value Name
    if (!addedFactorName) {
      ListGridRecord newRec = addRecord("Experimental Factor Name");
      int i = 1;
      for (String key : factorNameToType.keySet()) {
        newRec.setAttribute(i + "", key.substring(key.indexOf("[") + 1, key.indexOf("]")));
        i++;
      }
    }
    // Case that there is no row with Factor Value Type
    if (!addedFactorType) {
      ListGridRecord newRec = addRecord("Experimental Factor Type");
      int i = 1;
      for (String key : factorNameToType.keySet()) {
        newRec.setAttribute(i + "", factorNameToType.get(key));
        i++;
      }
    }
    
  }
  
  private ListGridRecord addRecord(String fieldName) {
    ListGridRecord[] newRecords = new ListGridRecord[listOfAllRecords.length + 1];
    
    for (int i = 0; i < listOfAllRecords.length; i++) {
      newRecords[i] = listOfAllRecords[i];
    }
    
    ListGridRecord newRecord = new ListGridRecord();
    newRecord.setAttribute("0", fieldName);
    newRecords[listOfAllRecords.length] = newRecord;
    listOfAllRecords = newRecords;
    return newRecord;
  }
}
