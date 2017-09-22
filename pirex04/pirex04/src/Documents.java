package pirex04.src;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Documents.java is a Documents object class that stores the 'Document' of the prirex index
 * of Guten formmatted files. Holds the long and short string of document. Holds the Opus, and
 * the integar list of the document word.
 * 
 * @author Maksim
 * 
 * This complies with the JMU honor code.
 */
public class Documents implements Serializable
{
  private static final long serialVersionUID = 1L;
  private static final int SHORT_NUM = 15;
  
  private int documentNumber;
  private Opus opus;
  private String paragraph = "";
  private List<Integer> wordCounts = new ArrayList<Integer>();
  
  /**
   * Explicit value constructor.
   * @param paragraph an item.
   * @param wordCounts an item.
   * @param opus that owns the document.
   * @param documentNumber within opus text.
   */
  public Documents(String paragraph, List<Integer> wordCounts, Opus opus,
      int documentNumber)
  {
    this.paragraph = paragraph;
    this.wordCounts = wordCounts;
    this.opus = opus;
    this.documentNumber = documentNumber;
  }
  
  /**
   * gets the long paragraph version of the document.
   * @return a long string version of the document.
   */
  public String getParagraph()
  {
    return paragraph;
  }
  
  /**
   * An integer list of all the document word counts.
   * @return a list of all the document word counts.
   */
  public List<Integer> getWordCounts()
  {
    return wordCounts;
  }
  
  /**
   * get opus object.
   * @return opus that document is associated with.
   */
  public Opus getOpus()
  {
    return opus;
  }
  
  /**
   * get Document number.
   * @return an item that document is associated with.
   */
  public int getDocumentNumber()
  {
    return documentNumber;
  }
  
  /**
   * gets the short paragraph version of the document.
   * @return a short string version of the document.
   */
  public String getShortParagraph()
  {
    int i = 0;
    String shortParagraph;
    
    shortParagraph = "";
    
    for (String part : paragraph.split("\\s+"))
    {
      i++;    
      shortParagraph += part + " ";
      
      if (i == SHORT_NUM)
        break;
    }
    return shortParagraph.trim();
  }
}
