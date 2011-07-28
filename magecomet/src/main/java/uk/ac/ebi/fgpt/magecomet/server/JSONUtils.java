package uk.ac.ebi.fgpt.magecomet.server;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import monq.jfa.Dfa;
import monq.jfa.DfaRun;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.mged.magetab.error.ErrorItem;

import uk.ac.ebi.arrayexpress2.magetab.datamodel.IDF;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.SDRF;
import uk.ac.ebi.arrayexpress2.magetab.listener.ErrorItemListener;
import uk.ac.ebi.arrayexpress2.magetab.parser.MAGETABParser;
import uk.ac.ebi.arrayexpress2.magetab.renderer.IDFWriter;
import uk.ac.ebi.arrayexpress2.magetab.renderer.SDRFWriter;
import uk.ac.ebi.arrayexpress2.magetab.utils.MAGETABUtils;
import uk.ac.ebi.arrayexpress2.magetab.validator.MAGETABValidator;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class JSONUtils {
  public static JSONArray getJSONArrayFromWhatIzIt(File file, Dfa dfa) throws WhatIzItException {
    
    // Get Json Array from file
    try {
      String passage = Files.toString(file, Charsets.ISO_8859_1);
      
      Map<String,Integer> map = new HashMap<String,Integer>();
      
      // get a machinery (DfaRun) to operate the Dfa
      DfaRun r = new DfaRun(dfa);
      r.clientData = map;
      
      // Get only the filtered text
      
      r.filter(passage);
      
      JSONArray jsonArray = new JSONArray();
      for (String string : map.keySet()) {
        System.out.println(string);
        jsonArray.add(string);
      }
      return jsonArray;
      
    } catch (IOException e) {
      throw new WhatIzItException(e);
    }
  }
  
  public static JSONArray getErrorArray(File idf, File sdrf) throws AnnotareValidationException {
    JSONArray errorArray = new JSONArray();
    
    try {
      System.out.println("Started Validation");
      // Create validataor
      // Validator<MAGETABInvestigation> validator = new SemanticValidator(idf.getAbsolutePath());
      
      MAGETABValidator validator = new MAGETABValidator();
      
      MAGETABParser parser = new MAGETABParser(validator);
      
      // register error item listener
      final List<ErrorItem> errorList = new ArrayList<ErrorItem>();
      
      parser.addErrorItemListener(new ErrorItemListener() {
        public void errorOccurred(ErrorItem item) {
          if (item.getErrorCode() != 1031) {
            errorList.add(item);
          }
        }
      });
      
      // need to get the url of this file, as the parser only takes urls
      parser.parse(idf);
      
      // do parse
      System.out.println("Parsing " + idf.getAbsolutePath() + "...");
      
      int i = 0;
      
      for (ErrorItem error : errorList) {
        JSONObject errorItem = new JSONObject();
        errorItem.put("code", error.getErrorCode() + "");
        errorItem.put("type", error.getErrorType());
        errorItem.put("message", error.getMesg());
        errorItem.put("comment", error.getComment());
        errorItem.put("line", error.getLine() + "");
        errorItem.put("column", error.getCol() + "");
        errorArray.add(i, errorItem);
        i++;
      }
      System.out.println("Finished Validation " + idf.getAbsolutePath() + "...");
      
      return errorArray;
    } catch (Exception e) {
      e.printStackTrace();
      throw new AnnotareValidationException("Annotare Validation Failed");
    }
  }
  
  public static JSONArray getJSONArrayFromIDF(IDF object) {
    StringWriter out = new StringWriter();
    IDFWriter writer = new IDFWriter(out);
    try {
      writer.write(object);
      return getJSONArrayFromString(out.toString());
    } catch (IOException e) {
      e.printStackTrace();
    }
    return getJSONArrayFromString("Error Writing Parsing");
    
  }
  
  public static JSONArray getJSONArrayFromSDRF(SDRF object) {
    StringWriter out = new StringWriter();
    SDRFWriter writer = new SDRFWriter(out);
    try {
      writer.write(object);
      return getJSONArrayFromString(out.toString());
    } catch (IOException e) {
      e.printStackTrace();
    }
    return getJSONArrayFromString("Error Writing Parsing");
    
  }
  
  private static JSONArray getJSONArrayFromString(String oneLongString) {
    // 01_Create Array Of rows by splitting on new line
    String[] rows = oneLongString.split("\\r?\\n");
    
    // 02_Create Array of rows that you will return
    JSONArray jsonRows = new JSONArray();
    
    // 03_For each row, split on the tab and put the column into the row array
    for (int i = 0; i < rows.length; i++) {
      JSONArray jsonColumnArray = new JSONArray();
      String[] columns = rows[i].split("\\t");
      for (String value : columns) {
        jsonColumnArray.add(value);
      }
      jsonRows.add(i, jsonColumnArray);
    }
    return jsonRows;
  }
  
  public static JSONArray getJSONArrayFromInputStream(InputStream in) throws IOException {
    String[][] array = MAGETABUtils.readTabDelimitedInputStream(in, "iso-8859-1", true);
    JSONArray jsonRows = new JSONArray();
    
    for (int i = 0; i < array.length; i++) {
      JSONArray jsonColumnArray = new JSONArray();
      for (String value : array[i]) {
        jsonColumnArray.add(value);
      }
      jsonRows.add(i, jsonColumnArray);
    }
    return jsonRows;
  }
}
