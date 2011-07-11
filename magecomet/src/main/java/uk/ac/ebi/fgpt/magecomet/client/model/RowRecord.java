package uk.ac.ebi.fgpt.magecomet.client.model;

import java.util.HashMap;

public class RowRecord extends HashMap<String,String>{
  public RowRecord() {
    super();
  }
  public RowRecord deepClone(){
    RowRecord newRowRecord = new RowRecord();
    for(String key:this.keySet()){
      newRowRecord.put(key, this.get(key));
    }
    return newRowRecord;
    
  }
}
