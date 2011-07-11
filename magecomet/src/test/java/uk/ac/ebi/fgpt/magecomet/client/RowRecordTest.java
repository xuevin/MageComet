package uk.ac.ebi.fgpt.magecomet.client;

import org.junit.Before;
import org.junit.Test;

import uk.ac.ebi.fgpt.magecomet.client.model.RowRecord;

import static org.junit.Assert.*;

public class RowRecordTest {
  private RowRecord rowRecord;
  
  @Before
  public void setUp() throws Exception {
    rowRecord = new RowRecord();
    rowRecord.put("key1", "value1");
    rowRecord.put("key2", "value2");
    rowRecord.put("key3", "value3");
    rowRecord.put("key4", "value4");
  }
  
  @Test
  public void testClone() {
    RowRecord copy = rowRecord.deepClone();
    assertNotSame(copy, rowRecord);
    
    copy.put("key1", "valueFoo");
    assertNotSame(copy.get("key1"), rowRecord.get("key1"));
  }
  
}
