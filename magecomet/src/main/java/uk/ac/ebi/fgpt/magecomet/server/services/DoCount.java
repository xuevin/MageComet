package uk.ac.ebi.fgpt.magecomet.server.services;

import java.util.Map;

import monq.jfa.AbstractFaAction;
import monq.jfa.DfaRun;

/**
 * A callback method used by monq
 * 
 * @author Vincent Xue
 * 
 */
public class DoCount extends AbstractFaAction {
  private String word;
  
  public DoCount(String wordToStoreInHash) {
    this.word = wordToStoreInHash;
  }
  
  public void invoke(StringBuffer iotext, int start, DfaRun r) {
    // String word = iotext.substring(start);
    // iotext.setLength(start);
    //    
    Map m = (Map) r.clientData;
    Integer count = (Integer) m.get(word);
    if (count == null) m.put(word, new Integer(1));
    else m.put(word, new Integer(1 + count.intValue()));
  }
}