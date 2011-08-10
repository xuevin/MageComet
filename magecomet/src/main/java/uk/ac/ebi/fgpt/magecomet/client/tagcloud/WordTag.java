package uk.ac.ebi.fgpt.magecomet.client.tagcloud;

/**
 * A class that describes the elements in the tag cloud (namely a word tag)
 * 
 * @author Vincent Xue
 * 
 */
public class WordTag {
  private String word;
  private int hits;
  private ClickAction clickAction;
  
  public WordTag(String word, ClickAction action) {
    this.word = word;
    this.hits = 1;
    this.clickAction = action;
  }
  
  public WordTag(String word, ClickAction action, int number) {
    this.word = word;
    this.hits = number;
    this.clickAction = action;
  }
  
  public int getHits() {
    return hits;
  }
  
  public void addHit() {
    hits++;
  }
  
  public void addHit(int number) {
    hits += number;
  }
  
  public String getWord() {
    return word;
  }
  
  public void setClickHandeler(ClickAction clickAction) {
    this.clickAction = clickAction;
  }
  
  public ClickAction getClickAction() {
    return clickAction;
  }
}
