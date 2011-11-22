package uk.ac.ebi.fgpt.magecomet.server.services;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import uk.ac.ebi.fgpt.magecomet.client.service.ftpservice.FTPException;

public class FTPServiceImplTest {
  
  @Before
  public void setUp() throws Exception {}
  
  @Test
  public void testGettExperimentJSON() throws FTPException {
    FTPServiceImpl foo = new FTPServiceImpl();
    assertNotNull(foo.getExperimentJSON("E-GEOD-13367"));
    System.out.println(foo.getExperimentJSON("E-GEOD-13367").toString());
  }
  
}
