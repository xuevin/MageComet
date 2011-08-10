package uk.ac.ebi.fgpt.magecomet.client.view.section;

import uk.ac.ebi.fgpt.magecomet.client.GuiMediator;

import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;

/**
 * The view that handles displaying the IDF components
 * 
 * @author Vincent Xue
 * 
 */
public class IDF_Section extends SectionStackSection {
  
  private final ListGrid idfGrid = new ListGrid();
  private final HTMLFlow textBox = new HTMLFlow("Experiment Description");
  private GuiMediator guiMediator;
  
  public IDF_Section(GuiMediator guiMediator) {
    super("IDF");
    
    this.guiMediator = guiMediator;
    this.guiMediator.registerIDFSection(this);
    
    HStack hStack = new HStack();
    
    idfGrid.setCanEdit(true);
    idfGrid.setEditEvent(ListGridEditEvent.DOUBLECLICK);
    idfGrid.setCanSort(false);
    idfGrid.setWidth("60%");
    idfGrid.setShowAllRecords(true);
    idfGrid.setShowBackgroundComponent(false);
    idfGrid.setShowRollOver(false);
    
    textBox.setWidth("40%");
    textBox.setOverflow(Overflow.AUTO);
    textBox.setMargin(5);
    
    hStack.addMember(idfGrid);
    hStack.addMember(textBox);
    
    addItem(hStack);
  }
  
  public void setDescription(String filteredOutData) {
    textBox.setContents(filteredOutData);
  }
  
  public void setData(ListGridField[] allFields, ListGridRecord[] allRecords) {
    idfGrid.setRecords(allRecords);
    idfGrid.setFields(allFields);
  }
}
