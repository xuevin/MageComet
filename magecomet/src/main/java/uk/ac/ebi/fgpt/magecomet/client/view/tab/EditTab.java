package uk.ac.ebi.fgpt.magecomet.client.view.tab;

import uk.ac.ebi.fgpt.magecomet.client.GuiMediator;
import uk.ac.ebi.fgpt.magecomet.client.view.section.IDF_Section;
import uk.ac.ebi.fgpt.magecomet.client.view.section.SDRF_Section;

import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.tab.Tab;

/**
 * The tab that holds the IDF and SDRF sections
 * 
 * @author Vincent Xue
 * 
 */
public class EditTab extends Tab {
  private GuiMediator guiMediator;
  // Section for Stacks
  private final SectionStack sectionStack = new SectionStack();
  private IDF_Section idfSection;
  private SDRF_Section sdrfSection;
  
  public EditTab(GuiMediator guiMediator) {
    super("Edit");
    this.guiMediator = guiMediator;
    this.guiMediator.registerEditTab(this);
    setPane(sectionStack);
    setIcon("[SKIN]actions/edit.png");
    
    idfSection = new IDF_Section(guiMediator);
    sdrfSection = new SDRF_Section(guiMediator);
    
    sectionStack.setVisibilityMode(VisibilityMode.MULTIPLE);
    sectionStack.setSections(idfSection, sdrfSection);
    sectionStack.setHeight100();
    sectionStack.setWidth100();
    sectionStack.setMargin(0);
    sectionStack.setPadding(0);
    sectionStack.expandSection(0);
    sectionStack.expandSection(1);
    
  }
  
}
