package uk.ac.ebi.fgpt.magecomet.client.tagcloud;


public class Highlight {
	public static native void highlightTerm(String term) /*-{
	  $wnd.localSearchHighlight(term);
	}-*/;
	public static native void unhighlightAll() /*-{
	
	$wnd.unhighlightAll();
	
	  
	}-*/;

}
//$doc.unhighlight(document.getElementsByTagName('body')[0]);
//$doc.unhighlight(document.getElementsByTagName('body')[0]);