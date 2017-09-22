package pirex04.src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * BasicSearch is the searching class that will search through the passed user terms
 * and passed indexed data store information and display the results after comparing the
 * two databases.
 * 
 * @author Team04 - mparchu, johns2nc
 *
 * This code complies with the JMU Honor Code.
 */
public final class SearchEngine 
{    
  /**
   * search, is a method that will take in the user passed search terms, and the pirex index
   * terms from the userMode method from the pirex driver and compare the terms and display
   * the amount of search terms that have been found.
   * 
   * @param query search terms
   * @return docList a list of documents objects
   */
  public static List<Documents> search(String[] query)
  {
    HashMap<Integer, HashMap<Integer, Documents>> docMap;
    HashMap<Integer, Documents> insideDoc;
    HashMap<Integer, List<Integer>> insideIndex; 
    HashMap<String, HashMap<Integer, List<Integer>>> indexMap;
    HashMap<String, HashMap<Integer, List<Integer>>> searchResults;
    List<Documents> docList;
    List<String> notTerms;
    String emptyString;
    String[] stemmedQuery;
    
    docList = new ArrayList<Documents>();
    emptyString = " ";
    indexMap = SearchUtils.getIndexMap();
    notTerms = new ArrayList<String>();
    
    searchResults = new HashMap<String, HashMap<Integer, List<Integer>>>();
    
    for (int i = 0; i < query.length; i++)
    {
      if (query[i].trim().startsWith("-"))
      {     
        notTerms.add(emptyString + query[i].trim().substring(1) + emptyString);
        query[i] = "wordThatShouldNotBeFoundOnlyAPlaceHolder1545184564";
      }  
    }
    
    SearchUtils.setNotTerms(notTerms);
    
    stemmedQuery = Stemming.stemmingTerms(query);
    
    for(String s : stemmedQuery)
      if(indexMap.containsKey(s))
        searchResults.put(s, indexMap.get(s));

    docMap = SearchUtils.getDocMap();
    
    if (!searchResults.isEmpty())
      for (String key : searchResults.keySet())
      {
        insideIndex = searchResults.get(key);
        for (int insideKey : insideIndex.keySet())
        {
          int i = 0;
          insideDoc = docMap.get(insideKey);
          while(i < insideDoc.size())
          {
            i++;
            for (int k = 0; k < insideIndex.get(insideKey).size(); k++)
            {
              if(insideDoc.get(i).getWordCounts().contains(insideIndex.get(insideKey).get(k)))
                docList.add(insideDoc.get(i));
            }     
          }
        }
      }   
    return docList;
  }
}
