package uk.ac.ebi.fgpt.magecomet.client;

import com.smartgwt.client.types.HeaderControls;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

public class TagCloudWindow extends Window{
	
	private final Tab tagCloudTab1 = new Tab("Weight By Location");
	private final Tab tagCloudTab2 = new Tab("Weight By Errors");
	private final TagCloud tagCloud = new TagCloud();
	private GuiMediator guiMediator;

	public TagCloudWindow(GuiMediator guiMediator){
		super();
		this.guiMediator=guiMediator;
		this.guiMediator.registerTagCloud(this);
		
		setHeaderControls(HeaderControls.HEADER_LABEL,HeaderControls.MINIMIZE_BUTTON);
		setTitle("EFO Tag Cloud");
		setWidth("600");
		setHeight("300");
		setOverflow(Overflow.VISIBLE);
		setCanDragResize(true);
		
		//TabSet for cloud
		TabSet tagCloudTabSet = new TabSet();
		tagCloudTabSet.setTabBarPosition(Side.TOP);
		tagCloudTabSet.setTabBarAlign(Side.LEFT);
		tagCloudTabSet.setHeight100();
		tagCloudTabSet.setWidth100();
		tagCloudTabSet.setPaneContainerOverflow(Overflow.VISIBLE);
		tagCloudTabSet.setOverflow(Overflow.VISIBLE);
		tagCloudTabSet.addTab(tagCloudTab1);
		tagCloudTabSet.addTab(tagCloudTab2);
		
		
		
		tagCloud.setWidth("100%");
		
		Canvas gwtCanvas = new Canvas();
		gwtCanvas.addChild(tagCloud);

		tagCloudTab1.setPane(gwtCanvas);
	


		addItem(tagCloudTabSet);
		
		minimize();
	}
	public void addWord(String word,ClickAction action, int number){
		tagCloud.addWord(word, action, number);
	}
}
