package pirex04.src;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Stemming.java is a helper class to incorporate a simple Stemming function in the PIREX
 * program. The program incorporates some grammar rules and adds stemmed versions of the
 * terms searched to a query.
 * 
 * @author Team04 - mparchu
 *  This complies with the JMU honor code.
 */
public final class Stemming
{
  private static final int TWELVE = 12;
  private static final int ONE = 1;
  private static final int TWO = 2;
  private static final String STRING_E = "e";
  private static final String STRING_ED = "ed";
  private static final String STRING_ER = "er";
  private static final String STRING_ES = "es";
  private static final String STRING_N = "n";
  private static final String STRING_M = "m";
  private static final String STRING_S = "s";
  private static final String STRING_SS = "ss";
  private static String[] newSearchTerms;
     
  /**
   * Method that adds stemmed/stemming versions of the search terms for the program to
   * search the index.
   * 
   * @return a String array of search Terms plus the stemmed terms.
   * @param query the string array of search Terms.
   */
  public static String[] stemmingTerms(String[] query)
  {
    int counter;
    int index;
    int loopCounter;
    String baseTerm;
    
    baseTerm = "";
    counter = 0;
    index = 0;
    loopCounter = 0;
    newSearchTerms = new String[query.length * TWELVE];
    for (index = 0; index < newSearchTerms.length; index++)
    {
      loopCounter = index;
      newSearchTerms[loopCounter++] = query[counter].trim();
      
      if (query[counter].endsWith(STRING_ER) || query[counter].endsWith(STRING_ES))
        baseTerm = query[counter].substring(0, query[counter].length() - TWO).trim();
      else if ((query[counter].endsWith(STRING_S) && !query[counter].endsWith(STRING_SS))
          || query[counter].endsWith(STRING_E))
        baseTerm = query[counter].substring(0, query[counter].length() - ONE).trim();
      else
        baseTerm = query[counter].trim();
      
      newSearchTerms[loopCounter++] = baseTerm;
      newSearchTerms[loopCounter++] = baseTerm.concat("ty");
      newSearchTerms[loopCounter++] = baseTerm.concat(STRING_S);
      newSearchTerms[loopCounter++] = baseTerm.concat(STRING_ES);
      newSearchTerms[loopCounter++] = baseTerm.concat("like");
      newSearchTerms[loopCounter++] = baseTerm.concat("ly");
      newSearchTerms[loopCounter++] = baseTerm.concat(STRING_ED);
      newSearchTerms[loopCounter++] = baseTerm.concat(STRING_ER);
      
      if (baseTerm.endsWith(STRING_N))
        baseTerm = baseTerm.concat(STRING_N);
      else if (baseTerm.endsWith(STRING_M))
        baseTerm = baseTerm.concat(STRING_M);
          
      newSearchTerms[loopCounter++] = baseTerm.concat("ing");
      newSearchTerms[loopCounter++] = baseTerm.concat(STRING_ED);
      newSearchTerms[loopCounter] = baseTerm.concat(STRING_ER);
      
      index = loopCounter;
      counter++;
    }  
    
    newSearchTerms = new HashSet<String>(Arrays.asList(newSearchTerms)).toArray(new String[0]);
    
    return newSearchTerms;
  }
}
