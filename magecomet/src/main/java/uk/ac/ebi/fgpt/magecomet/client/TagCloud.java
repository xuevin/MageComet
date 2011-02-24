package uk.ac.ebi.fgpt.magecomet.client;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.smartgwt.client.widgets.Window;

public class TagCloud extends AbsolutePanel{

	private ArrayList<WordTag> listOfWordTags;
	public TagCloud(){
		super();
		listOfWordTags=new ArrayList<WordTag>();
	}
	public void addWord(String word,ClickHandler action){
		for(WordTag wtag: listOfWordTags){
			if(wtag.getWord().equals(word)){
				wtag.addHit();
				refresh();
				return;
			}
		}
		listOfWordTags.add(new WordTag(word,action));
		refresh();
	}
	public void addWord(String word,ClickHandler action,int number){
		for(WordTag wtag: listOfWordTags){
			if(wtag.getWord().equals(word)){
				wtag.addHit(number);
				refresh();
				return;
			}
		}
		listOfWordTags.add(new WordTag(word,action,number));
		refresh();
	}
	public void refresh(){
		clear();
		for(WordTag tag:listOfWordTags){
			Button lbl = new Button(tag.getWord());
			lbl.addClickHandler(tag.getClickHandeler());
			lbl.setStyleName("");
			//lbl.setWordWrap(true);
			
			lbl.setStylePrimaryName("tagcloud");
			switch (tag.getHits()) {
			case 1:
				lbl.addStyleDependentName("size1");
				break;
			case 2:
				lbl.addStyleDependentName("size2");
				break;
			case 3:
				lbl.addStyleDependentName("size3");
				break;
			case 4:
				lbl.addStyleDependentName("size4");
				break;
			case 5:
				lbl.addStyleDependentName("size5");
				break;
			}
			//lbl.addClickHandler(tag.getClickHandeler());
			add(lbl);
		}
		
	}
}
