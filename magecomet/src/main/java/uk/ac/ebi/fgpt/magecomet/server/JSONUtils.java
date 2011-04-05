package uk.ac.ebi.fgpt.magecomet.server;

import gwtupload.server.exceptions.UploadActionException;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import monq.jfa.CompileDfaException;
import monq.jfa.DfaRun;
import monq.jfa.ReSyntaxException;
import monq.programs.DictFilter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mged.annotare.validator.SemanticValidator;
import org.mged.magetab.error.ErrorItem;

import uk.ac.ebi.arrayexpress2.magetab.datamodel.IDF;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.MAGETABInvestigation;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.SDRF;
import uk.ac.ebi.arrayexpress2.magetab.listener.ErrorItemListener;
import uk.ac.ebi.arrayexpress2.magetab.parser.MAGETABParser;
import uk.ac.ebi.arrayexpress2.magetab.renderer.IDFWriter;
import uk.ac.ebi.arrayexpress2.magetab.renderer.SDRFWriter;
import uk.ac.ebi.arrayexpress2.magetab.validator.MAGETABValidator;
import uk.ac.ebi.arrayexpress2.magetab.validator.Validator;

public class JSONUtils {
	public static JSONArray getJSONArrayFromWhatIzIt(File file,String monqInput) throws IOException, ReSyntaxException, CompileDfaException{
		try{
			InputStream inputStream =null;
			try {
				inputStream = new ByteArrayInputStream(monqInput.toString().getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
			InputStreamReader mwt = new InputStreamReader(inputStream) ;
			DictFilter dict = new DictFilter(mwt, "xml", "none", false);
			DfaRun r = dict.createRun();
			
			 byte[] buffer = new byte[(int) file.length()];
			 BufferedInputStream f = null;
			 try {
				 f = new BufferedInputStream(new FileInputStream(file.getAbsolutePath()));
				 f.read(buffer);
				 f.close();
			 } catch(IOException e){
				 System.out.println("Error Reading Files");
			 }
			 
			 String filteredString = r.filter(new String(buffer));
			 
			 //Patter to search for
			 Pattern pattern = Pattern.compile("label=\".*\"");
				
			//Search IDF
			Matcher matcher = pattern.matcher(filteredString);
			HashSet<String> uniqueTerms = new HashSet<String>(); 
			while(matcher.find()){
				String match = matcher.group();
				uniqueTerms.add(match.substring(7,match.length()-1));
			}
			return new JSONArray(uniqueTerms);
		}catch(IOException e){
			System.out.println("ERROR Loading EFO");
			throw e;
		}catch(ReSyntaxException e){
			throw e;
		}catch(CompileDfaException e){
			throw e;
		}		
	}
	public static JSONArray getErrorArray(File idf, File sdrf) throws UploadActionException {
		
		try {
			System.out.println("Started Validation");
			// Create validataor 
			Validator<MAGETABInvestigation> validator = new SemanticValidator(idf.getAbsolutePath());
			
			MAGETABValidator v = new MAGETABValidator();
			
			MAGETABParser parser = new MAGETABParser(validator);
			
			// register error item listener
			final List<ErrorItem> errorList = new ArrayList<ErrorItem>();

			parser.addErrorItemListener(new ErrorItemListener() {
				public void errorOccurred(ErrorItem item) {
					if(item.getErrorCode()!=1031){
						errorList.add(item);	
					}
					
				}
			});
			
			// need to get the url of this file, as the parser only takes urls
			parser.parse(idf);

			// do parse
			System.out.println("Parsing " + idf.getAbsolutePath() + "...");

			
			int i =0;
			JSONArray errorArray= new JSONArray();

			for(ErrorItem error:errorList){
				JSONObject errorItem = new JSONObject();
				errorItem.put("code",error.getErrorCode()+"");
				errorItem.put("type",error.getErrorType());
				errorItem.put("message",error.getMesg());
				errorItem.put("line",error.getLine()+"");
				errorItem.put("column",error.getCol()+"");
				errorArray.put(i,errorItem);
				i++;
			}
			System.out.println("Finished Validation " + idf.getAbsolutePath() + "...");

			return errorArray;
		} catch (JSONException e) {
			e.printStackTrace();
			throw new UploadActionException("Validation Failed");
		} catch (Exception e){
			e.printStackTrace();
			throw new UploadActionException("Something else failed");
		}
	}
	public static JSONArray getJSONArrayFromIDF(IDF object){
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
	public static JSONArray getJSONArrayFromSDRF(SDRF object){
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
	private static JSONArray getJSONArrayFromString(String oneLongString){
		//01_Create Array Of rows by splitting on new line
		String[] rows = oneLongString.split("\\r?\\n");
		
		//02_Create Array of rows that you will return
		JSONArray jsonRows = new JSONArray(); 
		try {
			//03_For each row, split on the tab and put the column into the row array
			for(int i = 0; i<rows.length; i++){
				String[] column = rows[i].split("\\t");
				jsonRows.put(i,column);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonRows;
	}
}
