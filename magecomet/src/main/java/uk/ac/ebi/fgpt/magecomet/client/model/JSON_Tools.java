package uk.ac.ebi.fgpt.magecomet.client.model;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;

public class JSON_Tools {
	public static String[][] get2DArray(String name,JSONObject object){
		JSONArray rows = object.get(name).isArray();
		
		//Instantiate size of the 2d array to the size of the first row
		String[][] returnArray = new String[rows.size()][rows.get(0).isArray().size()];
		
		for(int i = 0;i<rows.size();i++){
			JSONArray columns = rows.get(i).isArray();
			for(int j = 0; j< columns.size(); j++){
				returnArray[i][j] = columns.get(j).isString().stringValue();
			}
		}
		
		return returnArray;
	}
	

}
