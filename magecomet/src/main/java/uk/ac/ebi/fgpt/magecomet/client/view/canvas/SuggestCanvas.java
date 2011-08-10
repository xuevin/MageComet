package uk.ac.ebi.fgpt.magecomet.client.view.canvas;

import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.smartgwt.client.widgets.Canvas;

/**
 * A Canvas that wraps around a the GWT components. (allows for GWT and smartGWT to work together)
 * 
 * @author Vincent Xue
 * 
 */
public class SuggestCanvas extends Canvas {
  public static final int HEIGHT = 17;
  private SuggestBox suggestBoxField;
  
  public SuggestCanvas(String name, String title, SuggestOracle suggestOracle) {
    super();
    setHeight(HEIGHT);
    suggestBoxField = new SuggestBox(suggestOracle);
    suggestBoxField.setStyleName("gwt-SuggestBox");
    suggestBoxField.setHeight(getHeight() + "px");
    suggestBoxField.setLimit(3); // Set the limit to 3 suggestions
    
    setStyleName("gwt-SuggestBoxCanvas");
    addChild(suggestBoxField);
    
  }
  
  public String getText() {
    return suggestBoxField.getText();
  }
}
