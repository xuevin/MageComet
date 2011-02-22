package uk.ac.ebi.fgpt.magecomet.client;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;

public class TagCloud extends AbsolutePanel{

	private ArrayList<WordTag> listOfWordTags;
	public TagCloud(){
		super();
		listOfWordTags=new ArrayList<WordTag>();
		
		
		addWord("sarcoidoisis",2);
		addWord("sarcoidoisis",2);
		addWord("protocol",2);
		addWord("Homo sapiens",2);
		addWord("RNA",2);
		addWord("Female",2);
		addWord("array",2);
		addWord("design",2);
		addWord("age",2);
		addWord("blood",2);
		addWord("disease",2);
		addWord("organism",2);
		
	}
	public void addWord(String word){
		for(WordTag wtag: listOfWordTags){
			if(wtag.getWord().equals(word)){
				wtag.addHit();
				refresh();
				return;
			}
		}
		listOfWordTags.add(new WordTag(word));
		refresh();
	}
	public void addWord(String word,int number){
		for(WordTag wtag: listOfWordTags){
			if(wtag.getWord().equals(word)){
				wtag.addHit(number);
				refresh();
				return;
			}
		}
		listOfWordTags.add(new WordTag(word,number));
		refresh();
	}
	public void refresh(){
		clear();
		for(WordTag tag:listOfWordTags){
			Button lbl = new Button(tag.getWord());
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
