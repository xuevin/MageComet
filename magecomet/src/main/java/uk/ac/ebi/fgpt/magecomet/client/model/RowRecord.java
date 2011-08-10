package uk.ac.ebi.fgpt.magecomet.client.model;

import java.util.HashMap;

/**
 * This class describes a row. (A row is a hash of 2 strings, field to value)
 * 
 * @author Vincent Xue
 * 
 */
public class RowRecord extends HashMap<String,String> {
  public RowRecord() {
    super();
  }
  
  public RowRecord deepClone() {
    RowRecord newRowRecord = new RowRecord();
    for (String key : this.keySet()) {
      newRowRecord.put(key, this.get(key));
    }
    return newRowRecord;
    
  }
}
