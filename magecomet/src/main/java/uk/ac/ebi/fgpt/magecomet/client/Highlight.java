package uk.ac.ebi.fgpt.magecomet.client;


public class Highlight {
	public static native void highlightTerm(String term) /*-{
	  $wnd.localSearchHighlight(term);
	}-*/;

}
