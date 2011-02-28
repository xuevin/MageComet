package uk.ac.ebi.fgpt.magecomet.server;

/*
 * Modified From GWTUpload_GettingStarted
 */


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import monq.jfa.CompileDfaException;
import monq.jfa.DfaRun;
import monq.jfa.ReSyntaxException;
import monq.programs.DictFilter;

import org.apache.commons.fileupload.FileItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mged.annotare.validator.SemanticValidator;
import org.mged.magetab.error.ErrorItem;


import uk.ac.ebi.arrayexpress2.magetab.datamodel.IDF;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.MAGETABInvestigation;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.SDRF;
import uk.ac.ebi.arrayexpress2.magetab.exception.ErrorItemListener;
import uk.ac.ebi.arrayexpress2.magetab.exception.ParseException;
import uk.ac.ebi.arrayexpress2.magetab.handler.ParserMode;
import uk.ac.ebi.arrayexpress2.magetab.parser.IDFParser;
import uk.ac.ebi.arrayexpress2.magetab.parser.MAGETABParser;
import uk.ac.ebi.arrayexpress2.magetab.parser.SDRFParser;
import uk.ac.ebi.arrayexpress2.magetab.validator.Validator;

import gwtupload.server.UploadAction;
import gwtupload.server.exceptions.UploadActionException;

public class UploadServlet extends UploadAction{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	
	Hashtable<String, String> receivedContentTypes = new Hashtable<String, String>();
	
	/**
	 * Maintain a list with received files and their content types. 
	 */
	Hashtable<String, File> receivedFiles = new Hashtable<String, File>();
	 
	
	/**
	   * Override executeAction to save the received files in a custom place
	   * and delete this items from session.  
	   */
	public String executeAction(HttpServletRequest request,List<FileItem> sessionFiles) throws UploadActionException {
		String response = "";
		int cont = 0;
		
		for (FileItem item : sessionFiles) {
			if (false == item.isFormField()) {
				cont++;
				try {
					// 01_Create a temporary folder placed in the default system
					// temp folder based on the session ID
					File tempDir = new File("/tmp/" + request.getSession().getId());
					if(!tempDir.exists()){
						tempDir.delete();
						tempDir.mkdir();	
					}
					
					
					File file = new File ((tempDir.getAbsoluteFile()+"/")+item.getName());
					item.write(file);				
					
					
					// 02_Save a list with the received files
					receivedFiles.put(item.getFieldName(), file);
					receivedContentTypes.put(item.getFieldName(), item.getContentType());
					
					JSONObject responseJSONObject = new JSONObject();
					try{
						// 03_Parse files. 
						if(file.getName().contains("sdrf")){		
							SDRFParser sdrfParser = new SDRFParser();
							SDRF sdrf = sdrfParser.parse(file.toURI().toURL());
							responseJSONObject.put("sdrfArray", getJSONArrayFromString(sdrf.toString()));
						}else if(file.getName().contains("idf")){
							IDFParser idfParser = new IDFParser();
							IDF idf = idfParser.parse(file.toURI().toURL());
							responseJSONObject.put("idfArray",getJSONArrayFromString(idf.toString())); 
						}else{
							System.err.println("Unknown File Format Uploaded");
							throw new UploadActionException("Unknown File Name");
						}	
					}catch(ParseException e){
						throw new UploadActionException("Parse Error!");
					}
					
					
					//Add tagcloud array of terms to JSON Object
					responseJSONObject.put("whatizit",getJSONArrayFromWhatIzIt(file));
					
					
					//	After each upload, check if the other file exists
					
					//If SDRF was updated then look for IDF 
					if(item.getName().contains("sdrf")){
						String idfFileString = item.getName().substring(0, item.getName().length()-9)+".idf.txt";
						File idf = new File("/tmp/"+request.getSession().getId()+"/"+idfFileString);
						if(idf.exists()){
							System.out.println(idf.getAbsolutePath());
							responseJSONObject.put("error",getErrorArray(idf,file));
						}
					}else if(item.getName().contains("idf")){ //else look for SDRF
						String sdrfFileString = item.getName().substring(0, item.getName().length()-8)+".sdrf.txt";
						File sdrf = new File("/tmp/"+request.getSession().getId()+"/"+sdrfFileString);
						if(sdrf.exists()){
							System.out.println(sdrf.getAbsolutePath());
							responseJSONObject.put("error",getErrorArray(file,sdrf));
						}
					}
					
					
					
					
					response=responseJSONObject.toString();
					
				} catch (Exception e) {
					throw new UploadActionException(e);
				}
			}
		}

		// / Remove files from session because we have a copy of them
		removeSessionFileItems(request);

		// / Send information of the received files to the client.
		return response;
	}

	private JSONArray getErrorArray(File idf, File sdrf) throws UploadActionException {
		try {
			
			// make a new parser, in read only mode
			MAGETABParser parser = new MAGETABParser(ParserMode.READ_ONLY);

			// register error item listener
			final List<ErrorItem> errorList = new ArrayList<ErrorItem>();

			parser.addErrorItemListener(new ErrorItemListener() {
				public void errorOccurred(ErrorItem item) {
					errorList.add(item);
				}
			});
			
			// Create validataor 
			Validator<MAGETABInvestigation> validator = new SemanticValidator(idf.getAbsolutePath());

			// set validator on the parser
			parser.setValidator(validator);

			// do parse
			System.out.println("Parsing " + idf.getAbsolutePath() + "...");

			// need to get the url of this file, as the parser only takes urls
			parser.parse(idf.toURI().toURL());
			
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
			return errorArray;
			
		} catch (ParseException e) {
			// This happens if parsing failed.
			// Any errors here will also have been reported by the listener
			e.printStackTrace();
			throw new UploadActionException("Validation Failed");
		} catch (MalformedURLException e) {
			// This is if the url from the file is bad
			e.printStackTrace();
			throw new UploadActionException("Validation Failed");
		} catch (JSONException e) {
			e.printStackTrace();
			throw new UploadActionException("Validation Failed");
		}
		
	}

	/**
	 * Get the content of an uploaded file.
	 */
	public void getUploadedFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String fieldName = request.getParameter(PARAM_SHOW);
		File f = receivedFiles.get(fieldName);
		if (f != null) {
			
			response.setContentType(receivedContentTypes.get(fieldName));
			FileInputStream is = new FileInputStream(f);
			copyFromInputStreamToOutputStream(is, response.getOutputStream());	
		} else {
			renderXmlResponse(request, response, ERROR_ITEM_NOT_FOUND);
		}
	}

	/**
	 * Remove a file when the user sends a delete request.
	 */
	public void removeItem(HttpServletRequest request, String fieldName) throws UploadActionException {
		File file = receivedFiles.get(fieldName);
		receivedFiles.remove(fieldName);
		receivedContentTypes.remove(fieldName);
		if (file != null) {
			file.delete();
		}
	}
	private List<String[]> convertMapToListOfArrays(Map<String, Set<String>> map) {
		List<String[]> listOfArrays = new ArrayList<String[]>();
		
		Iterator<String> itr = map.keySet().iterator();
		while(itr.hasNext()){
			String field=itr.next();
			listOfArrays.add(listToArray(("Comments ["+field+"]"), map.get(field)));
		}
		return listOfArrays;
	}

	private String[] stringToArray(String fieldName, String value) {
		String[] temp = new String[2];
		temp[0]=fieldName;
		temp[1]=value;
		return temp;
	}
	private String[] listToArray(String fieldName, List<String> value){
		String[] temp = new String[value.size()+1];
		temp[0] = fieldName;
		for(int i=0;i<value.size();i++){
			temp[i+1]=value.get(i);
		}
		return temp;
	}
	private String[] listToArray(String fieldName, Set<String> value){
		String[] temp = new String[value.size()+1];
		temp[0] = fieldName;
		Iterator<String> itr = value.iterator();
		int i = 1;
		while(itr.hasNext()){
			temp[i]=itr.next();
			i++;
		}
		return temp;
	}

	private JSONArray getJSONArrayFromWhatIzIt(File file) throws IOException, ReSyntaxException, CompileDfaException{
		try{
			//TODO make this customizable
			FileReader mwt = new FileReader("src/main/resources/EFO_inferred_v142.xml");
			
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
	private JSONArray getJSONArrayFromString(String oneLongString){
		
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

	@Deprecated
	private JSONArray convertMapToJSONArray(Map<String, Set<String>> map){
		
		//////////////////
		//
		//	00 01 02 03
		//	10 11 12 13
		//	20 21 22 23
		//
		// Anything that ends in a zero is a field name
		// Else, everything is a value.
		
		JSONArray rowArray = new JSONArray();
		Iterator<String> itr =map.keySet().iterator();
		int i =0;
		while(itr.hasNext()){
			
			String field = itr.next();
			ArrayList<String> newList = new ArrayList<String>();
			newList.add(field);
			for(String value:map.get(field)){
				newList.add(value);
			}
			try {
				rowArray.put(i, new JSONArray(newList));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			i++;
		}
		return rowArray;
	}
	/**
		 * Convert SDRF to JSON so that the client may be updated
		 */
		@Deprecated
		private JSONObject converSDRFToJSOB(SDRF sdrf){
			JSONObject jsonObject = new JSONObject();
			String oneLongString = sdrf.toString();
			String[] rows = oneLongString.split("\\r?\\n");
			
			JSONArray jsonRows = new JSONArray(); 
			try {
			
				for(int i = 0; i<rows.length; i++){
					String[] column = rows[i].split("\\t");
					jsonRows.put(i,column);
				}
				jsonObject.put("array", jsonRows);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
			return jsonObject;
			
		}

		
		
	@Deprecated
	private JSONArray convertIDFToJSONArray(IDF idf){
		JSONArray jsonArray = new JSONArray();
		
		try{
			jsonArray.put(0,new JSONArray(stringToArray("Date Of Experiment",idf.dateOfExperiment)));
			jsonArray.put(1,new JSONArray(stringToArray("Experiment Description", idf.experimentDescription)));
			jsonArray.put(2,new JSONArray(stringToArray("Investigation Title", idf.investigationTitle)));
			jsonArray.put(3,new JSONArray(stringToArray("Magetab Version", idf.magetabVersion)));
			jsonArray.put(4,new JSONArray(stringToArray("Public Release Date", idf.publicReleaseDate)));
			jsonArray.put(5,new JSONArray(listToArray("Experimental Design",idf.experimentalDesign)));
			jsonArray.put(6,new JSONArray(listToArray("Experimental Design Term Accession",idf.experimentalDesignTermAccession)));
			jsonArray.put(7,new JSONArray(listToArray("Experimental Design Term Source REF",idf.experimentalDesignTermSourceREF)));
			jsonArray.put(8,new JSONArray(listToArray("Experimental Factor Name",idf.experimentalFactorName)));
			jsonArray.put(9,new JSONArray(listToArray("Experimental Factor Term Accession",idf.experimentalFactorTermAccession)));
			jsonArray.put(10,new JSONArray(listToArray("Experimental Factor Term Source REF",idf.experimentalFactorTermSourceREF)));
			jsonArray.put(11,new JSONArray(listToArray("Experimental Factor Type",idf.experimentalFactorType)));
			jsonArray.put(12,new JSONArray(listToArray("Normalization Term Accession",idf.normalizationTermAccession)));
			jsonArray.put(13,new JSONArray(listToArray("Normalization Term Source REF",idf.normalizationTermSourceREF)));
			jsonArray.put(14,new JSONArray(listToArray("Normalization Type",idf.normalizationType)));
			jsonArray.put(15,new JSONArray(listToArray("Person Address",idf.personAddress)));
			jsonArray.put(16,new JSONArray(listToArray("Person Affiliation",idf.personAffiliation)));
			jsonArray.put(17,new JSONArray(listToArray("Person Email",idf.personEmail)));
			jsonArray.put(18,new JSONArray(listToArray("Person Fax",idf.personFax)));
			jsonArray.put(19,new JSONArray(listToArray("Person First Name",idf.personFirstName)));
			jsonArray.put(20,new JSONArray(listToArray("Person Last Name",idf.personLastName)));
			jsonArray.put(21,new JSONArray(listToArray("Person Mid Initials",idf.personMidInitials)));
			jsonArray.put(22,new JSONArray(listToArray("Person Phone",idf.personPhone)));
			jsonArray.put(23,new JSONArray(listToArray("Person Roles",idf.personRoles)));
			jsonArray.put(24,new JSONArray(listToArray("Person Roles Term Accession",idf.personRolesTermAccession)));
			jsonArray.put(25,new JSONArray(listToArray("Person Roles Term Source REF",idf.personRolesTermSourceREF)));
			jsonArray.put(26,new JSONArray(listToArray("Protocol Contact",idf.protocolContact)));
			jsonArray.put(27,new JSONArray(listToArray("Protocol Description",idf.protocolDescription)));
			jsonArray.put(28,new JSONArray(listToArray("Protocol Hardware",idf.protocolHardware)));
			jsonArray.put(29,new JSONArray(listToArray("Protocol Name",idf.protocolName)));
			jsonArray.put(30,new JSONArray(listToArray("Protocol Parameters",idf.protocolParameters)));
			jsonArray.put(31,new JSONArray(listToArray("Protocol Software",idf.protocolSoftware)));
			jsonArray.put(32,new JSONArray(listToArray("Protocol Term Accession",idf.protocolTermAccession)));
			jsonArray.put(33,new JSONArray(listToArray("Protocol Term Source REF",idf.protocolTermSourceREF)));
			jsonArray.put(34,new JSONArray(listToArray("Protocol Type",idf.protocolType)));
			jsonArray.put(35,new JSONArray(listToArray("Publication Author List",idf.publicationAuthorList)));
			jsonArray.put(36,new JSONArray(listToArray("Publication DOI",idf.publicationDOI)));
			jsonArray.put(37,new JSONArray(listToArray("Publication Status",idf.publicationStatus)));
			jsonArray.put(38,new JSONArray(listToArray("Publication Status Term Accession",idf.publicationStatusTermAccession)));
			jsonArray.put(39,new JSONArray(listToArray("Publication Status Term Source REF",idf.publicationStatusTermSourceREF)));
			jsonArray.put(40,new JSONArray(listToArray("Publication Title",idf.publicationTitle)));
			jsonArray.put(41,new JSONArray(listToArray("PubMedId",idf.pubMedId)));
			jsonArray.put(42,new JSONArray(listToArray("Quality Control TermAccession",idf.qualityControlTermAccession)));
			jsonArray.put(43,new JSONArray(listToArray("Quality Control Term Source REF",idf.qualityControlTermSourceREF)));
			jsonArray.put(44,new JSONArray(listToArray("Quality Control Type",idf.qualityControlType)));
			jsonArray.put(45,new JSONArray(listToArray("Replicate Term Accession",idf.replicateTermAccession)));
			jsonArray.put(46,new JSONArray(listToArray("Replicate Term Source REF",idf.replicateTermSourceREF)));
			jsonArray.put(47,new JSONArray(listToArray("Replicate Type",idf.replicateType)));
			jsonArray.put(48,new JSONArray(listToArray("SDRF File",idf.sdrfFile)));
			jsonArray.put(49,new JSONArray(listToArray("Term Source File",idf.termSourceFile)));
			jsonArray.put(50,new JSONArray(listToArray("Term Source Name",idf.termSourceName)));
			jsonArray.put(51,new JSONArray(listToArray("Term Source Version",idf.termSourceVersion)));
			List<String[]> comments = convertMapToListOfArrays(idf.getComments());
			for(int i = 0; i<comments.size();i++){
				jsonArray.put(i+52,new JSONArray(comments.get(i)));
			}	
		}catch (JSONException e) {
			// TODO: handle exception
			System.err.println(e);
		}
		
		return jsonArray;
		
	}

	/**
	 * Convert IDF to JSON so that the client may be updated
	 */
	@Deprecated
	private JSONObject convertIDFToJSOB(IDF idf){
		
		
		JSONObject object = new JSONObject();
		try {
			object.put("dateOfExperiment", idf.dateOfExperiment);
			object.put("experimentDescription", idf.experimentDescription);
			object.put("investigationTitle", idf.investigationTitle);
			object.put("magetabVersion", idf.magetabVersion);
			object.put("publicReleaseDate", idf.publicReleaseDate);
			object.put("experimentalDesign", new JSONArray(idf.experimentalDesign));
			object.put("experimentalDesignTermAccession", new JSONArray(idf.experimentalDesignTermAccession));
			object.put("experimentalDesignTermSourceREF", new JSONArray(idf.experimentalDesignTermSourceREF));
			object.put("experimentalFactorName", new JSONArray(idf.experimentalFactorName));
			object.put("experimentalFactorTermAccession", new JSONArray(idf.experimentalFactorTermAccession));
			object.put("experimentalFactorTermSourceREF", new JSONArray(idf.experimentalFactorTermSourceREF));
			object.put("experimentalFactorType", new JSONArray(idf.experimentalFactorType));
			object.put("normalizationTermAccession", new JSONArray(idf.normalizationTermAccession));
			object.put("normalizationTermSourceREF", new JSONArray(idf.normalizationTermSourceREF));
			object.put("normalizationType", new JSONArray(idf.normalizationType));
			object.put("personAddress", new JSONArray(idf.personAddress));
			object.put("personAffiliation", new JSONArray(idf.personAffiliation));
			object.put("personEmail", new JSONArray(idf.personEmail));
			object.put("personFax", new JSONArray(idf.personFax));
			object.put("personFirstName", new JSONArray(idf.personFirstName));
			object.put("personLastName", new JSONArray(idf.personLastName));
			object.put("personMidInitials", new JSONArray(idf.personMidInitials));
			object.put("personPhone", new JSONArray(idf.personPhone));
			object.put("personRoles", new JSONArray(idf.personRoles));
			object.put("personRolesTermAccession", new JSONArray(idf.personRolesTermAccession));
			object.put("personRolesTermSourceREF", new JSONArray(idf.personRolesTermSourceREF));
			object.put("protocolContact", new JSONArray(idf.protocolContact));
			object.put("protocolDescription", new JSONArray(idf.protocolDescription));
			object.put("protocolHardware", new JSONArray(idf.protocolHardware));
			object.put("protocolName", new JSONArray(idf.protocolName));
			object.put("protocolParameters", new JSONArray(idf.protocolParameters));
			object.put("protocolSoftware", new JSONArray(idf.protocolSoftware));
			object.put("protocolTermAccession", new JSONArray(idf.protocolTermAccession));
			object.put("protocolTermSourceREF", new JSONArray(idf.protocolTermSourceREF));
			object.put("protocolType", new JSONArray(idf.protocolType));
			object.put("publicationAuthorList", new JSONArray(idf.publicationAuthorList));
			object.put("publicationDOI", new JSONArray(idf.publicationDOI));
			object.put("publicationStatus", new JSONArray(idf.publicationStatus));
			object.put("publicationStatusTermAccession", new JSONArray(idf.publicationStatusTermAccession));
			object.put("publicationStatusTermSourceREF", new JSONArray(idf.publicationStatusTermSourceREF));
			object.put("publicationTitle", new JSONArray(idf.publicationTitle));
			object.put("pubMedId", new JSONArray(idf.pubMedId));
			object.put("qualityControlTermAccession", new JSONArray(idf.qualityControlTermAccession));
			object.put("qualityControlTermSourceREF", new JSONArray(idf.qualityControlTermSourceREF));
			object.put("qualityControlType", new JSONArray(idf.qualityControlType));
			object.put("replicateTermAccession", new JSONArray(idf.replicateTermAccession));
			object.put("replicateTermSourceREF", new JSONArray(idf.replicateTermSourceREF));
			object.put("replicateType", new JSONArray(idf.replicateType));
			object.put("sdrfFile", new JSONArray(idf.sdrfFile));
			object.put("termSourceFile", new JSONArray(idf.termSourceFile));
			object.put("termSourceName", new JSONArray(idf.termSourceName));
			object.put("termSourceVersion", new JSONArray(idf.termSourceVersion));
			object.put("commentsMap",convertMapToJSONArray(idf.getComments()));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return object;
	}
}
