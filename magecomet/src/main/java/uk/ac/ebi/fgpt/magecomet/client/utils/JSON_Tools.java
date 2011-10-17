package uk.ac.ebi.fgpt.magecomet.client.utils;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;

/**
 * This class describes some static tools used for parsing JSON objects.
 * 
 * @author Vincent Xue
 * 
 */
public class JSON_Tools {
  public static String[][] get2DArray(String name, JSONObject object) {
    JSONArray rows = object.get(name).isArray();
    
    // Find the row with the most columns
    int maxSize = 0;
    for (int i = 0; i < rows.size(); i++) {
      if (rows.get(i).isArray().size() > maxSize) {
        maxSize = rows.get(i).isArray().size();
      }
    }
    // Fill in the columns
    String[][] returnArray = new String[rows.size()][maxSize];
    for (int i = 0; i < rows.size(); i++) {
      JSONArray columns = rows.get(i).isArray();
      for (int j = 0; j < columns.size(); j++) {
        returnArray[i][j] = columns.get(j).isString().stringValue();
      }
    }
    
    return returnArray;
  }
  
}
