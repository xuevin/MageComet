package uk.ac.ebi.fgpt.magecomet.server;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import uk.ac.ebi.arrayexpress2.magetab.datamodel.IDF;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.SDRF;
import uk.ac.ebi.arrayexpress2.magetab.exception.ParseException;
import uk.ac.ebi.arrayexpress2.magetab.parser.IDFParser;
import uk.ac.ebi.arrayexpress2.magetab.parser.SDRFParser;

public class JSONUtilsTest {
  private Logger log = Logger.getLogger(this.getClass());
  private SDRF sdrf;
  private IDF idf;
  private File idfFile;
  private File sdrfFile;
  
  @Before
  public void setup() throws AnnotareValidationException,
                     URISyntaxException,
                     MalformedURLException,
                     ParseException,
                     IOException {
    
    idfFile = new File(getClass().getClassLoader().getResource("E-GEOD-18781.idf.txt").toURI());
    sdrfFile = new File(getClass().getClassLoader().getResource("E-GEOD-18781.sdrf.txt").toURI());
    
    if (sdrfFile == null || idfFile == null) {
      log.warn("Files are not found");
      fail();
    }
    // SDRF Parser
    SDRFParser sdrfParser = new SDRFParser();
    sdrf = sdrfParser.parse(sdrfFile.toURI().toURL().openStream());
    
    // IDF Parser
    IDFParser idfParser = new IDFParser();
    idf = idfParser.parse(idfFile.toURI().toURL().openStream());
    
  }
  
  @Test
  public void testFoo() {
    assertEquals(true, true);
  }
  
  @Test
  public void showThat_getJSONArrayFromIDF_Works() {
    JSONArray jsonArray = JSONUtils.getJSONArrayFromIDF(idf);
    assertEquals("[\"Investigation Title\",\"Transcription profiling of human sarcoidosis patients\"]",
      jsonArray.get(0).toString());
  }
  
  @Test
  public void showThat_getJSONArrayFromSDRF_Works() {
    JSONArray jsonArray = JSONUtils.getJSONArrayFromSDRF(sdrf);
    jsonArray.get(0);
    assertEquals("Source Name", ((JSONArray) jsonArray.get(0)).get(0).toString());
  }
  
  @Test
  public void showThat_getJSONArrayFromWhatIzIt_Works() {
  // // WhatIzIt Items
  // String monqInput = (String) getServletContext().getAttribute(
  // "monqInput");
  // responseJSONObject.put("whatizitIDF", JSONUtils
  // .getJSONArrayFromWhatIzIt(idfFile, monqInput));
  // responseJSONObject.put("whatizitSDRF", JSONUtils
  // .getJSONArrayFromWhatIzIt(sdrfFile, monqInput));
  // System.out.println("WhatIzIt stored in JSON Array");
  }
  
  @Test
  public void showThat_getErrorArray_Works() throws AnnotareValidationException {
    JSONArray array = JSONUtils.getErrorArray(sdrfFile, idfFile);
    assertEquals("1", ((JSONObject) array.get(0)).get("code"));
    
    log.info("Error Items are stored in JSON Array");
  }
}