package uk.ac.ebi.fgpt.magecomet.client.gui.window;

import java.util.ArrayList;

import uk.ac.ebi.fgpt.magecomet.client.GuiMediator;
import uk.ac.ebi.fgpt.magecomet.client.Highlight;
import uk.ac.ebi.fgpt.magecomet.client.tagcloud.ClickAction;
import uk.ac.ebi.fgpt.magecomet.client.tagcloud.EFOService;
import uk.ac.ebi.fgpt.magecomet.client.tagcloud.EFOServiceAsync;
import uk.ac.ebi.fgpt.magecomet.client.tagcloud.TagCloud;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.HeaderControls;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

public class TagCloudWindow extends Window{
	
	private final Tab autofillCloud = new Tab("Weight By Location");
	private final Tab highlightCloud = new Tab("Highlight Mode");
	private final TagCloud tagCloudAutofillPopup = new TagCloud();
	private final TagCloud tagCloudHighlight = new TagCloud();

	private GuiMediator guiMediator;
	private EFOServiceAsync efoServiceAsync = GWT.create(EFOService.class);
	private final ArrayList<AutofillPopup> listOfActiveAutofillPopups= new ArrayList<AutofillPopup>();
	private final ArrayList<String> listOfHighlightedTerms = new ArrayList<String>();


	public TagCloudWindow(GuiMediator guiMediator){
		super();
		this.guiMediator=guiMediator;
		this.guiMediator.registerTagCloud(this);
		
//		listOfActiveAutofillPopups.add(new AutofillPopup("RNA",efoServiceAsync,guiMediator));
		
		
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
//		tagCloudTabSet.setOverflow(Overflow.VISIBLE);
		tagCloudTabSet.addTab(autofillCloud);
		tagCloudTabSet.addTab(highlightCloud);
		
		
		tagCloudAutofillPopup.setWidth("100%");
		tagCloudHighlight.setWidth("100%");
		
		Canvas gwtCanvas = new Canvas();
		gwtCanvas.addChild(tagCloudAutofillPopup);
		gwtCanvas.setOverflow(Overflow.AUTO);
		
		Canvas gwtCanvas2 = new Canvas();
		gwtCanvas2.addChild(tagCloudHighlight);
		gwtCanvas2.setOverflow(Overflow.AUTO);
		
		autofillCloud.setPane(gwtCanvas);
		highlightCloud.setPane(gwtCanvas2);
	
		addItem(tagCloudTabSet);
		
		minimize();
	}
	public void addWord(final String word, int number){
		
		ClickAction popupAction = new ClickAction() {
			public void execute(){
				for(AutofillPopup popup: listOfActiveAutofillPopups){
					if(popup.getTitle().equals(word)){
//							popup.updateColumns();
							popup.centerInPage();
							popup.show();
							return;
					}
				}
				listOfActiveAutofillPopups.add(new AutofillPopup(word,efoServiceAsync,guiMediator));
			}
		};
		
		ClickAction highlightAction = new ClickAction() {
			public void execute(){
				boolean found = false;
				for(String title: listOfHighlightedTerms){
					if(title.equals(word)){
						listOfHighlightedTerms.remove(title);
						found=true;
						break;
					}
				}
				//If it was not found, add it to the list;
				if(!found){
					listOfHighlightedTerms.add(word);
					Highlight.highlightTerm(word);	
					
				}else{
					Highlight.unhighlightAll();
					for(String word:listOfHighlightedTerms){
						Highlight.highlightTerm(word);	
					}
				}
			}
		};
		
		tagCloudAutofillPopup.addWord(word, popupAction, number);
		tagCloudHighlight.addWord(word, highlightAction, number);
		
	}
	public void refreshTagClouds(){
		tagCloudAutofillPopup.refresh();
		tagCloudHighlight.refresh();
	}
}
