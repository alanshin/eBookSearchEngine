package pirex04.src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
//import java.nio.file.Files;
//import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Utility class for operations dealing with the search and indexing functionality.
 * 
 * @author mparchu, okadacs, johns2nc
 * This complies with the JMU honor code.
 */
public final class SearchUtils
{
  private static HashMap<Integer, HashMap<Integer, Documents>> docMap = 
      new  HashMap<Integer, HashMap<Integer, Documents>>();
  private static HashMap<String, HashMap<Integer, List<Integer>>> indexMap = 
      new HashMap<String, HashMap<Integer, List<Integer>>>(); 
  private static Map<Integer, Opus> opusObjects = new HashMap<Integer, Opus>();
  private static final int HUNDRED = 100;
  private static final int ONE = 1;
  private static final int SUM_MAGIC_NUM = -25623;
  private static final String OPUS_MAP = "opus.ser";  
  private static final String STRING_ALPHA_NUM = "[^a-zA-Z0-9' -]+";
  private static final String STRING_ALPHA_NUM_NO_DASH = "[^a-zA-Z0-9 ]+";
  private static final String STRING_APOSTROPHE = "'";
  private static final String STRING_DASH = "-";
  private static final String STRING_SPLIT = "\\s+";
  private static int newDocumentsCount;
  private static int newPostings;
  private static int newTerms;
  private static int totalPostings = 0;
  private static int totalTerms = 0;
  private static int opusNumber = 0;
  private static List<String> notTerms = new ArrayList<String>();

  /**
   * create Opus creates an opus object.
   * 
   * @param title title
   * @param author author
   * @param ordNumber ordinal number
   * @param file file
   */
  public static void createOpus(String title, String author, int ordNumber, File file)
  {
    Opus opus;
    
    opus = new Opus(title, author, ordNumber, file);
    
    opusObjects.put(ordNumber, opus);
    index(opus);
  }
  
  /**
   * Indexing method that will store the indexed document into a HashMap.
   * Calls on the Documents Objects class to initialize attributse while in
   * the midst of indexing.
   * 
   * @param opus an Opus object that will be indexed
   */
  public static void index(Opus opus)
  { 
    boolean startFlag;
    Documents doc;
    HashMap<Integer, Documents> docHash;
    HashMap<Integer, List<Integer>> tempHash;    
    int wordCount;
    int docCount;
    int termCounter;
    List<Integer> tempLocation;
    List<Integer> wordLocation;
    List<Integer> paragraphWords;
    String modifiedPart;
    String paragraph;
    String[] stopWords;
      
    docCount = 0;
    docHash = new HashMap<Integer, Documents>();  
    modifiedPart = "";
    newPostings = 0;
    newTerms = 0;
    paragraph = "";
    paragraphWords = new ArrayList<Integer>();
    termCounter = indexMap.size();
    startFlag = false;
    stopWords = StopWords.stopWords();
    wordCount = 0;
    
    // Deals with serialization summary
    if (docMap.containsKey(SUM_MAGIC_NUM))
    {
      opusNumber = docMap.get(SUM_MAGIC_NUM).get(SUM_MAGIC_NUM).getDocumentNumber();
      if (docMap.get(SUM_MAGIC_NUM).get(SUM_MAGIC_NUM).getWordCounts().size() > ONE)
      {
        totalTerms = docMap.get(SUM_MAGIC_NUM).get(SUM_MAGIC_NUM).getWordCounts().get(0);
        totalPostings = docMap.get(SUM_MAGIC_NUM).get(SUM_MAGIC_NUM).getWordCounts().get(ONE);
      }
    }
    
    // Start of Indexing Algorithm
    try (BufferedReader br = new BufferedReader(new FileReader(opus.getFilePath()))) 
    {
      String line;
      while ((line = br.readLine()) != null)
      { 
        for (String part : line.split(STRING_SPLIT))
        {           
          if(line.contains("*** START"))
          {
            startFlag = true;
            continue;
          }
          
          if (!startFlag)
            continue;
          
          if(line.contains("*** END"))
          {
            startFlag = false;
            break;
          }
     
          wordCount++;
          
          paragraph += part + " ";
          paragraphWords.add(wordCount);   
          
          if (part.trim().equals(""))
          {
            doc = new Documents(paragraph, paragraphWords, opus, docCount + 1);
            if(!doc.getParagraph().trim()
                .replaceAll(STRING_ALPHA_NUM, "").toLowerCase().equals(""))
            {
              docCount++;
              docHash.put(docCount, doc);
            }
            paragraph = "";
            paragraphWords = new ArrayList<Integer>();
          }          
          modifiedPart = part.replaceAll(STRING_ALPHA_NUM, "").toLowerCase();
          
          if (modifiedPart.trim().equals(""))
            continue;
          
          if (modifiedPart.trim().contains(STRING_DASH))
            if (modifiedPart.trim().startsWith(STRING_DASH) 
                || modifiedPart.trim().endsWith(STRING_DASH))
              modifiedPart = modifiedPart.replaceAll(STRING_ALPHA_NUM_NO_DASH, "");
          
          if (modifiedPart.trim().contains(STRING_APOSTROPHE))
            if (modifiedPart.trim().startsWith(STRING_APOSTROPHE) 
                || modifiedPart.trim().endsWith(STRING_APOSTROPHE))
              modifiedPart = modifiedPart.replaceAll(STRING_ALPHA_NUM_NO_DASH, "");
          
          tempHash = new HashMap<Integer, List<Integer>>();
          wordLocation = new ArrayList<Integer>();
          wordLocation.add(wordCount);
          if (!indexMap.containsKey(modifiedPart))
          {        
            tempHash.put(opus.getOrdNumber(), wordLocation); 
          }
          else
          {
            tempHash = indexMap.get(modifiedPart);
            if (!tempHash.containsKey(opus.getOrdNumber()))
              tempHash.put(opus.getOrdNumber(), wordLocation);
            else
            {
              tempLocation = tempHash.get(opus.getOrdNumber());
              tempLocation.add(wordLocation.get(0));
              tempHash.put(opus.getOrdNumber(), tempLocation);
            }          
          }  
          indexMap.put(modifiedPart, tempHash);
        }
      }
    }
    catch (IOException error)
    {      
    }
    
    for (int i = 0; i < stopWords.length; i++)
      if(indexMap.containsKey(stopWords[i]))
      {
        indexMap.remove(stopWords[i]);
      }
    
    totalTerms = indexMap.size();
    newTerms = totalTerms - termCounter;
    docMap.put(opus.getOrdNumber(), docHash); 
    newDocumentsCount = docCount;
    newPostings = wordCount;
    opusNumber++;
    totalPostings += newPostings; 
  } 
  
  /**
   * removeOpus removes an Opus object from the Hashmap.
   * 
   * @param ordNumber is the key to remove
   */
  public static void removeOpus(Integer ordNumber)
  {
    opusObjects.remove(ordNumber);
  }
  
  /**
   * getOpus returns the opus object mapped to a specific ordinal number.
   * @param ordNumber is the ordinal number
   * @return the value of key ordNumber
   */
  public static Opus getOpus(Integer ordNumber)
  {
    return opusObjects.get(ordNumber);
  }
  
  /**
   * getOpusObjects returns the hash map of Opus objects.
   * 
   * @return global opusObjects
   */
  public static Map<Integer, Opus> getOpusObjects()
  {
    return opusObjects;
  }
  
  /**
   * getIndexMaps returns the inverted index hash map.
   * 
   * @return global opusObjects
   */
  public static HashMap<String, HashMap<Integer, List<Integer>>> getIndexMap()
  {
    return indexMap;
  }
  
  /**
   * getIndexMaps returns the inverted index hash map.
   * 
   * @param indexMaps to set
   */
  public static void setIndexMap(HashMap<String, HashMap<Integer, List<Integer>>> indexMaps)
  {
    indexMap = indexMaps;
  }
  
  /**
   * formatQuery breaks up the term.
   * @param query term
   * @return array of term
   */
  public static String[] formatQuery(String query)
  {
    List<String> searchList = new ArrayList<String>();
    String[] terms;
    terms = new String[HUNDRED];         
    terms = query.toLowerCase().split(STRING_SPLIT);
    
    for (int i = 0; i < terms.length; i++)
      searchList.add(terms[i]);
    
    if(searchList.size() > HUNDRED)
    {
      return null;
    }
    
    return searchList.toArray(new String[searchList.size()]);  
  }
  
  /**
   * parseText parses the author and title.
   * 
   * @param file project Guttenburg file
   * @param str author or title locator
   * @return Author or Title
   */
  public static String parseText(File file, String str)
  {
    String text = null;
    boolean result = true;
    
    if(file.exists() && file.canRead())
    {
      try
      {
        Scanner sc = new Scanner(file);
        
        while (sc.findInLine(Pattern.compile(str)) == null)
        {
          try
          {
            sc.nextLine();
          }
          catch(NoSuchElementException nsee)
          {
            result = false;
            sc = new Scanner (file);
            break;
          }
        }
        
        if(result)
        {  
          text = sc.nextLine();
        }
        else
        {
          text = null;
        }
        sc.close();
      }
      catch(FileNotFoundException fnf)
      {
        return null;
      }
    }
      
    return text;
  }
  
  /**
   * parseOrd returns the ordinal number in a file.
   * 
   * @param file project Guttenburg file
   * @return ordinal number
   */
  public static int parseOrd(File file)
  {
    boolean result = true;
    String str = "EBook #";
    int ordNum = -1;
    
    if(file.exists() && file.canRead())
    {
      try
      {
        Scanner sc = new Scanner(file);
        
        while (sc.findInLine(Pattern.compile(str)) == null)
        {
          try
          {
            sc.nextLine();
          }
          catch(NoSuchElementException nsee)
          {
            result = false;
            sc = new Scanner (file);
            break;
          }
        }
        if(!result)
        {
          sc.close();
          return -1;
        }
        String tempLine = sc.nextLine();
        
        try
        {
          ordNum = Integer.parseInt(tempLine.substring(1, tempLine.length() - 1));
        }
        catch(NumberFormatException nfe)
        {
          ordNum = -1;
        }
        sc.close();
      }
      catch(FileNotFoundException fnf)
      {
        return -1;
      }
    }
      
    return ordNum;
  }
  
  /**
   * Serialize serializes the opusObjects to the default data store.
   */
  public static void serialize()
  { 
    try 
    {
      OpusMap objects = new OpusMap(getOpusObjects(), getIndexMap(), getDocMap());
      FileOutputStream fileOutput = new FileOutputStream(DirUtils.getDefaultDataStore() 
          + OPUS_MAP);
      ObjectOutputStream outputStream = new ObjectOutputStream(fileOutput);
      
      outputStream.writeObject(objects);
      
      fileOutput.close();
      
      outputStream.flush();
      outputStream.close();
    }
    catch(IOException ioe)
    {
      System.out.println("Serialization Failure");
      ioe.printStackTrace();
    }
  }
  
  /**
   * Deserialize gets the serialized hashmap and sets it to opusObjects.
   * 
   * @return success or failure.
   */
  public static boolean deserialize()
  {
    try 
    {
      OpusMap objects;
      FileInputStream fileInput = new FileInputStream(DirUtils.getDefaultDataStore() + OPUS_MAP);
      ObjectInputStream inputStream = new ObjectInputStream(fileInput);
      
      objects = (OpusMap) inputStream.readObject();
      inputStream.close();
      fileInput.close();
      
      setOpusObjects(objects.getMap());
      setDocMap(objects.getDocMap());
      setIndexMap(objects.getIndexMap());
      
      File ser = new File (DirUtils.getDefaultDataStore() + OPUS_MAP);
      ser.delete();
      
      return true;
    }
    catch(IOException ioe)
    {
      return false;
    }
    catch(ClassNotFoundException cnfe)
    {
      return false;
    }
  }
  
  /**
   * setOpusObjects sets the opusObject hashmap.
   * 
   * @param objects a Hashmap of Opus objects
   */
  public static void setOpusObjects(Map<Integer, Opus> objects)
  {
    opusObjects = objects;
  }
  
  /**
   * getNewPostings, a getter method for new Postings.
   * 
   * @return newPostings needed for search results
   */
  public static int getNewPostings()
  {
    return newPostings;
  }
  
  /**
   * getTotalTerms, a getter method for total Terms.
   * 
   * @return totalTerms needed for search results
   */
  public static int getTotalTerms()
  {
    return totalTerms;
  }
  
  /**
   * getTotalPostings, a getter method for total Postings.
   * 
   * @return totalPostings needed for search results
   */
  public static int getTotalPostings()
  {
    return totalPostings;
  }
  
  /**
   * getOpusNumber, a getter method for opusNumber.
   * 
   * @return opusNumber needed for search results
   */
  public static int getOpusNumber()
  {
    return opusNumber;
  }
  
  /**
   * getNewTerms, a getter method for newTerms.
   * 
   * @return newTerms needed for search results
   */
  public static int getNewTerms()
  {
    if (newTerms == 0 && opusNumber == ONE)
      newTerms = totalTerms;
    return newTerms;
  }
  
  /**
   * getNewDocumentsCount, a getter method for newDocumentsCount.
   * 
   * @return newDocumentsCount needed for search results
   */
  public static int getNewDocumentsCount()
  {
    return newDocumentsCount;
  }
  
  /**
   * getDocMap, a getter method for docMap.
   * 
   * @return docMap a HashMap containing the document objects and the ebook
   */
  public static HashMap<Integer, HashMap<Integer, Documents>> getDocMap()
  {
    return docMap;
  }
  
  /**
   * sets document map.
   * 
   * @param docMaps to set.
   */
  public static void setDocMap(HashMap<Integer, HashMap<Integer, Documents>> docMaps)
  {
    docMap = docMaps;
  }
  
  /**
   * resetIndex used for JUNIT testing purposes.
   */
  public static void resetIndex()
  {
    indexMap.clear();
  }
  
  /**
   * resetDocMap used for JUNIT testing purposes.
   */
  public static void resetDocMap()
  {
    docMap.clear();
  }
  
  /**
   * resetNumbers used for JUNIT testing purposes.
   */
  public static void resetNumbers()
  {
    opusNumber = 0;
    totalTerms = 0;
    totalPostings = 0;   
  }
  
  /**
   * get not Terms.
   * @return string array of notTerms.
   */
  public static List<String> getNotTerms()
  {
    return notTerms;
  }
  
  /**
   * sets not Terms array.
   * @param notTermsGiven is a string array.
   */
  public static void setNotTerms(List<String> notTermsGiven)
  {
    notTerms = notTermsGiven;
  }
  
  /**
   * clearMaps clears all data in Maps.
   */
  public static void clearMaps()
  {
    docMap = new  HashMap<Integer, HashMap<Integer, Documents>>();
    indexMap = new HashMap<String, HashMap<Integer, List<Integer>>>(); 
    opusObjects = new HashMap<Integer, Opus>();
  }
}
