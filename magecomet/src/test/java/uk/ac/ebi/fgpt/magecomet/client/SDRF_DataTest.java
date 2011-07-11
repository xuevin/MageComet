package uk.ac.ebi.fgpt.magecomet.client;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import uk.ac.ebi.fgpt.magecomet.client.model.ColumnField;
import uk.ac.ebi.fgpt.magecomet.client.model.RowRecord;
import uk.ac.ebi.fgpt.magecomet.client.model.SDRF_Data;

import static org.junit.Assert.*;

public class SDRF_DataTest {
  private SDRF_Data data;
  
  @Before
  public void setUp() throws Exception {
    String[][] mockData = new String[][] { {"Source Name", "Field2", "Field3"},
                                          {"Value1A", "Value1B", "Value1C"},
                                          {"Value2A", "Value2B", "Value2C"}};
    data = new SDRF_Data(mockData);
  }
  
  @Test
  public void showThatDataIsNotNull() {
    assertNotNull(data);
  }
  
  @Test
  public void showThatThereAreFourFields() {
    assertEquals(4, data.getAllFields().length);
    
    assertEquals("key", data.getAllFields()[0].getUniqueName());
    assertEquals("Key", data.getAllFields()[0].getVisibleName());
    
    assertEquals("1", data.getAllFields()[1].getUniqueName());
    assertEquals("Source Name", data.getAllFields()[1].getVisibleName());
  }
  
  @Test
  public void showThatThereAreTwoRowRecords() {
    assertEquals(2, data.getAllRecords().size());
  }
  
  @Test
  public void showThatYouCanFetchTheValuesInTheRowRecord() {
    assertEquals("Value1A", data.getAllRecords().get("1").get("1"));
    assertEquals("Value2A", data.getAllRecords().get("2").get("1"));
  }
  
  @Test
  public void showThatAddAttributeToAllRecordsWorks() {
    data.addAttributeToAllRecords("1", "foo");
    for (RowRecord recordItem : data.getAllRecords().values()) {
      assertEquals(recordItem.get("1"), "foo");
    }
  }
  
  @Test
  public void showThatSetValueForSelectedRecordsWorks() {
    data.setValueForSelectedRecords(new String[] {"1"}, "1", "foobar");
    assertEquals("foobar", data.getAllRecords().get("1").get("1"));
    assertEquals("Value2A", data.getAllRecords().get("2").get("1"));
  }
  
  @Test
  public void showThatAddNewColumn_Characteristic_AndGetKeyWorks() {
    int lengthOfFieldsPriorToAdding = data.getAllFields().length;
    String newUniqeKey = data.addNewColumn_Characteristic_AndGetKey("FooBarField");
    int lengthAfterAdding = data.getAllFields().length;
    assertEquals(lengthOfFieldsPriorToAdding + 1, lengthAfterAdding);
    assertEquals("FooBarField", data.getAllFields()[2].getVisibleName());
    System.out.println("The new key is: " + newUniqeKey);
    for (ColumnField item : data.getAllFields()) {
      System.out.print(item.getVisibleName() + "\t");
    }
    System.out.println();
  }
  
  @Test
  public void showThatAddNewColumn_FactorValue_AndGetKeyWorks() {
    int lengthOfFieldsPriorToAdding = data.getAllFields().length;
    String newUniqeKey = data.addNewColumn_FactorValue_AndGetKey("FooBarField");
    int lengthAfterAdding = data.getAllFields().length;
    assertEquals(lengthOfFieldsPriorToAdding + 1, lengthAfterAdding);
    assertEquals("FooBarField", data.getAllFields()[4].getVisibleName());
    System.out.println("The new key is: " + newUniqeKey);
    for (ColumnField item : data.getAllFields()) {
      System.out.print(item.getVisibleName() + "\t");
    }
    System.out.println();
  }
  
  @Test
  public void showThatGetStringWorks() {
    assertEquals("Source Name\tField2\tField3\nValue2A\tValue2B\tValue2C\nValue1A\tValue1B\tValue1C", data
        .getString());
    System.out.println(data.getString());
  }
  
  @Test 
  public void showThatDeepCopyDataWorks(){
    HashMap<String,RowRecord> copy = SDRF_Data.deepCopyData(data.getAllRecords());
    assertNotSame(copy, data.getAllRecords());
    assertNotSame(copy.get("1"), data.getAllRecords().get("1"));
  }
  @Test
  public void showThatSaveStateWorks(){
    data.saveState();
    assertEquals(1, data.getState());
  }
  @Test
  public void showThatUndoWorks(){
    data.setCell("1", "1", "foobar");
    data.saveState();
    data.undo();
    assertEquals("Value1A",data.getAllRecords().get("1").get("1"));
  }
  @Test
  public void showThatRedoWorks(){
    data.setCell("1", "1", "foobar");
    data.saveState();
    data.undo();
    assertEquals("Value1A",data.getAllRecords().get("1").get("1"));
    data.redo();
    assertEquals("foobar",data.getAllRecords().get("1").get("1"));
  }
  @Test
  public void showThatSetCellWorks(){
    data.setCell("1", "1", "foobar");
    assertEquals("foobar",data.getAllRecords().get("1").get("1"));
  }
  @Test
  public void showThatHistoryWorksWithFields(){
    String uniqueKey = data.addNewColumn_Characteristic_AndGetKey("foo");
    data.addAttributeToAllRecords(uniqueKey, "foobar");
    data.saveState();
    data.undo();
    assertEquals("Value1B", data.getAllRecords().get("1").get("2"));
  }
}
