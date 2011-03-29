package uk.ac.ebi.fgpt.magecomet.client;

import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;

public class SuggestCanvas extends Canvas{
	public static final int HEIGHT = 17;
	private SuggestBox suggestBoxField;
	
	public SuggestCanvas(String name, String title, SuggestOracle suggestOracle) {
		super();
		setHeight(HEIGHT);
		suggestBoxField = new SuggestBox(suggestOracle);
		suggestBoxField.setStyleName("gwt-SuggestBox");
		suggestBoxField.setHeight(getHeight() + "px");
		suggestBoxField.setLimit(3);   // Set the limit to 5 suggestions
		
		setStyleName("gwt-SuggestBoxCanvas");
		addChild(suggestBoxField);	

	}
	public String getText(){
		return suggestBoxField.getText();
	}
}
