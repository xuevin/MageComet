package uk.ac.ebi.fgpt.magecomet.server.services;

import gwtupload.server.exceptions.UploadActionException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.util.HashMap;

import monq.jfa.CompileDfaException;
import monq.jfa.ReSyntaxException;

import org.apache.commons.net.ftp.FTPClient;
import org.json.JSONException;
import org.json.JSONObject;

import uk.ac.ebi.arrayexpress2.magetab.datamodel.IDF;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.SDRF;
import uk.ac.ebi.arrayexpress2.magetab.exception.ParseException;
import uk.ac.ebi.arrayexpress2.magetab.parser.IDFParser;
import uk.ac.ebi.arrayexpress2.magetab.parser.MAGETABParser;
import uk.ac.ebi.arrayexpress2.magetab.parser.SDRFParser;
import uk.ac.ebi.fgpt.magecomet.client.ftpservice.FTPException;
import uk.ac.ebi.fgpt.magecomet.client.ftpservice.FTPService;
import uk.ac.ebi.fgpt.magecomet.server.JSONUtils;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class FTPServiceImpl extends RemoteServiceServlet implements FTPService {
	private String arrayExpressFtp = "ftp.ebi.ac.uk";
	private String arrayExpressFtpPath = "/pub/databases/microarray/data/experiment/";
	
	private  HashMap<String, File> hashOfAccessionFilesForSDRF=new HashMap<String, File>();
	private  HashMap<String, File> hashOfAccessionFilesForIDF=new HashMap<String, File>();
	
	public String getExperimentJSON(String experimentAccession) throws FTPException {
		if(experimentAccession.length()<6){
			throw new FTPException("Too Short");
		}
		JSONObject responseJSONObject = new JSONObject();
		
		try {
			downloadAccession(experimentAccession.toUpperCase());
			
			File idfFile = hashOfAccessionFilesForIDF.get(experimentAccession);
			File sdrfFile =hashOfAccessionFilesForSDRF.get(experimentAccession); 
			
			if(sdrfFile==null){
				System.out.println("DULL");
			}
						
			//SDRF Parser			
			SDRFParser sdrfParser = new SDRFParser();
			SDRF sdrf = sdrfParser.parse(sdrfFile.toURI().toURL().openStream());
//			SDRF sdrf = sdrfParser.parse(sdrfFile.toURI().toURL());

			responseJSONObject.put("sdrfArray", JSONUtils.getJSONArrayFromSDRF(sdrf));
			
			//IDF Parser
			IDFParser idfParser = new IDFParser();
			IDF idf = idfParser.parse(idfFile.toURI().toURL().openStream());
//			System.out.println((idf.experimentDescription));
//			IDF idf = idfParser.parse(idfFile.toURI().toURL());

			responseJSONObject.put("idfArray",JSONUtils.getJSONArrayFromIDF(idf));
			
			//Error Items
			responseJSONObject.put("error",JSONUtils.getErrorArray(hashOfAccessionFilesForIDF.get(experimentAccession),
					hashOfAccessionFilesForSDRF.get(experimentAccession)));
			
			//WhatIzIt Items
			String monqInput = (String) getServletContext().getAttribute("monqInput");
			responseJSONObject.put("whatizitIDF",JSONUtils.getJSONArrayFromWhatIzIt(idfFile, monqInput));
			responseJSONObject.put("whatizitSDRF",JSONUtils.getJSONArrayFromWhatIzIt(sdrfFile, monqInput));

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (UploadActionException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ReSyntaxException e) {
			e.printStackTrace();
		} catch (CompileDfaException e) {
			e.printStackTrace();
		}catch (FTPException e){
			throw e;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return responseJSONObject.toString();
	}

	private void downloadAccession(String accession) throws FTPException {

		FTPClient client = new FTPClient();
		FileOutputStream fos_sdrf = null;
		FileOutputStream fos_idf = null;

		File temp_sdrf = null;
		File temp_idf = null;
		
		
		try {
			client.connect(arrayExpressFtp);
			client.login("anonymous", "");
			
				//Only download the files which are needed
				if(hashOfAccessionFilesForSDRF.containsKey(accession) &&
						hashOfAccessionFilesForIDF.containsKey(accession)){
					return;
				}
				
				try {
					File tempDir = File.createTempFile("kama_", "tmp");
					tempDir.delete();
					tempDir.mkdir();	
					
					temp_sdrf = new File ((tempDir.getAbsoluteFile()+"/")+accession+".sdrf.txt");
					temp_sdrf.deleteOnExit();
					
					temp_idf=new File ((tempDir.getAbsoluteFile()+"/")+accession+".idf.txt");
					temp_idf.deleteOnExit();
					
					fos_sdrf = new FileOutputStream(temp_sdrf);
					fos_idf = new FileOutputStream(temp_idf);

					String pipeline = accession.substring(2,6);
					String sdrfFile = accession+".sdrf.txt";
					String idfFile = accession+".idf.txt";
					
					client.retrieveFile(arrayExpressFtpPath+
							pipeline+"/"+
							accession+"/"+
							sdrfFile, fos_sdrf);
					
					if(client.getReplyString().contains("226")){
						System.out.println(sdrfFile + "\tFile Received");
						hashOfAccessionFilesForSDRF.put(accession, temp_sdrf);
					}else{
						System.out.println(sdrfFile + "\tFailed");
						throw new FTPException(sdrfFile);
					}
					

					client.retrieveFile(arrayExpressFtpPath+
							pipeline+"/"+
							accession+"/"+
							idfFile, fos_idf);
					
					if(client.getReplyString().contains("226")){
						System.out.println(idfFile + "\tFile Received");
						hashOfAccessionFilesForIDF.put(accession, temp_idf);
					}else{
						System.out.println(idfFile + "\tFailed");
						throw new FTPException(idfFile);
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				}finally{
					try{
						if(fos_sdrf!=null){
							fos_sdrf.close();
							fos_idf.close();							
						}
					}catch (IOException e) {
						e.printStackTrace();
					}
				}	
			client.disconnect();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}