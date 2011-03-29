package uk.ac.ebi.fgpt.magecomet.client.tagcloud;

import java.util.ArrayList;


import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;

public class TagCloud extends FlowPanel{

	private ArrayList<WordTag> listOfWordTags;
	public TagCloud(){
		super();
		setHeight("100%");
		listOfWordTags = new ArrayList<WordTag>();
	}
	public void addWord(String word,ClickAction action){
		for(WordTag wtag: listOfWordTags){
			if(wtag.getWord().equals(word)){
				wtag.addHit();
				return;
			}
		}
		listOfWordTags.add(new WordTag(word,action));
	}
	public void addWord(String word,ClickAction action,int number){
		for(WordTag wtag: listOfWordTags){
			if(wtag.getWord().equals(word)){
				wtag.addHit(number);
				return;
			}
		}
		listOfWordTags.add(new WordTag(word,action,number));
	}
	public void refresh(){
		clear();		
		for(WordTag tag:listOfWordTags){
			Button lbl = new Button(tag.getWord());
			final WordTag item=tag;
			lbl.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler() {
				public void onClick(ClickEvent arg0) {
					item.getClickAction().execute();
				}
			});
			lbl.setStyleName("");
	
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
			add(lbl);
		}
		
		

	}
}
