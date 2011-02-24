package uk.ac.ebi.fgpt.magecomet.client;

import com.google.gwt.event.dom.client.ClickHandler;


public class WordTag {
	private String word;
	private int hits;
	private ClickHandler clickHandeler;
	
	public WordTag(String word,ClickHandler action){
		this.word = word;
		this.hits=1;
		this.clickHandeler=action;
	}
	public WordTag(String word,ClickHandler action,int number){
		this.word = word;
		this.hits=number;
		this.clickHandeler=action;
	}
	public int getHits(){
		return hits;
	}
	public void addHit(){
		hits++;
	}
	public void addHit(int number){
		hits+=number;
	}
	public String getWord(){
		return word;
	}
	public void setClickHandeler(ClickHandler clickhandeler){
		this.clickHandeler=clickhandeler;
	}
	public ClickHandler getClickHandeler(){
		return clickHandeler;
	}
}
