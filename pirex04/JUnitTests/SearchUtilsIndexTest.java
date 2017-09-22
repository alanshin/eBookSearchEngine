package JUnitTests;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import pirex04.src.SearchUtils;

/**
 * JUNIT test cases that test the Documents Object Class as well as the
 * Indexing function and its corresponding methods.
 * 
 * @author mparchu
 * 
 * This complies with the JMU honor code.
 */
public class SearchUtilsIndexTest
{
  private static final int TEST_EIGHT = 8;
  private static final int TEST_FIVE = 5;
  private static final int TEST_FOUR = 4;
  private static final int TEST_NINE = 9;
  private static final int TEST_ORD = 1234;
  private static final int TEST_ORD2 = 1235;
  private static final int TEST_ONE = 1;
  private static final int TEST_SEVEN = 7;
  private static final int TEST_TEN = 10;
  private static final int TEST_THREE = 3;
  private static final int TEST_TWO = 2;
  private static final String TEST_AUTHOR = "TestingTitle";
  private static final String TEST_TITLE = "TestingAuthor";
  private static final String WORD_JUNIT = "junit";
  private static final String WORD_SIMPLE = "simple";
  private static final String WORD_TEST = "test";
  
  private HashMap<Integer, List<Integer>> insideHash; 
  private HashMap<String, HashMap<Integer, List<Integer>>> indexMap;
  private int newDocumentsCount;
  private int newPostings;
  private int newTerms;
  private List<Integer> wordLocation;
  
  private String junitFilePath = 
      "testFiles/JUNITTESTSEARCH3.txt";
  private String junitFilePath2 = 
      "testFiles/JUNITTESTSEARCH4.txt";
  private String junitFilePath3 = 
      "testFiles/JUNITTESTSEARCH5.txt";
  private String junitFilePath4 = 
      "testFiles/JUNITTESTSEARCH6.txt";
  
  /**
   * JUNIT test case dealing with a Single File.
   */
  @Test
  public void testSingleIndexFile()
  {
    // Resetting of values
    SearchUtils.resetIndex();
    SearchUtils.resetDocMap();
    SearchUtils.createOpus(TEST_TITLE, TEST_AUTHOR, TEST_ORD, 
        new File(junitFilePath));
    
    indexMap = new HashMap<String, HashMap<Integer, List<Integer>>>();
    insideHash = new HashMap<Integer, List<Integer>>(); 
    wordLocation = new ArrayList<Integer>();
    
    // Start of expected Index
    wordLocation.add(TEST_TWO);
    insideHash.put(TEST_ORD, wordLocation);
    indexMap.put(WORD_JUNIT, insideHash);
    
    insideHash = new HashMap<Integer, List<Integer>>(); 
    wordLocation = new ArrayList<Integer>();
    wordLocation.add(TEST_FOUR);
    insideHash.put(TEST_ORD, wordLocation);
    indexMap.put(WORD_TEST, insideHash);
    
    insideHash = new HashMap<Integer, List<Integer>>(); 
    wordLocation = new ArrayList<Integer>();
    wordLocation.add(TEST_THREE);
    insideHash.put(TEST_ORD, wordLocation);
    indexMap.put(WORD_SIMPLE, insideHash);
    
    assertEquals("testSingleIndexFile", indexMap, SearchUtils.getIndexMap());
    
    // Resetting of values
    SearchUtils.resetIndex();
    SearchUtils.resetDocMap();
  }
  
  /**
   * JUNIT test case dealing with a Single File with Dash and Apostrophe.
   */
  @Test
  public void testSingleIndexFileDashApostrophe()
  {
    // Resetting of values
    SearchUtils.resetIndex();
    SearchUtils.resetDocMap();
    SearchUtils.createOpus(TEST_TITLE, TEST_AUTHOR, TEST_ORD, 
        new File(junitFilePath4));
    
    assertEquals("testSingleIndexFileDashApostrophe", true, 
        SearchUtils.getIndexMap().containsKey("stemmer"));
    
    // Resetting of values
    SearchUtils.resetIndex();
    SearchUtils.resetDocMap();
  }

  /**
   * JUNIT test case dealing with a Multiple File.
   */
  @Test
  public void testMultiIndexFile()
  {
    SearchUtils.resetIndex();
    SearchUtils.resetDocMap();
    SearchUtils.createOpus(TEST_TITLE, TEST_AUTHOR, TEST_ORD, 
        new File(junitFilePath));
    SearchUtils.createOpus(TEST_TITLE, TEST_AUTHOR, TEST_ORD2, 
        new File(junitFilePath2));
    
    indexMap = new HashMap<String, HashMap<Integer, List<Integer>>>();
    insideHash = new HashMap<Integer, List<Integer>>(); 
    wordLocation = new ArrayList<Integer>();
    
    // Start of expected Index
    wordLocation.add(TEST_TWO);
    insideHash.put(TEST_ORD, wordLocation);
    wordLocation = new ArrayList<Integer>();
    wordLocation.add(TEST_TWO);
    wordLocation.add(TEST_SEVEN);
    insideHash.put(TEST_ORD2, wordLocation);
    indexMap.put(WORD_JUNIT, insideHash);
    
    insideHash = new HashMap<Integer, List<Integer>>(); 
    wordLocation = new ArrayList<Integer>();
    wordLocation.add(TEST_FOUR);
    insideHash.put(TEST_ORD, wordLocation);
    wordLocation = new ArrayList<Integer>();
    wordLocation.add(TEST_FOUR);
    wordLocation.add(TEST_TEN);
    insideHash.put(TEST_ORD2, wordLocation);
    indexMap.put(WORD_TEST, insideHash);
    
    insideHash = new HashMap<Integer, List<Integer>>(); 
    wordLocation = new ArrayList<Integer>();
    wordLocation.add(TEST_THREE);
    insideHash.put(TEST_ORD, wordLocation);
    wordLocation = new ArrayList<Integer>();
    wordLocation.add(TEST_THREE);
    wordLocation.add(TEST_EIGHT);
    insideHash.put(TEST_ORD2, wordLocation);
    indexMap.put(WORD_SIMPLE, insideHash);
    
    insideHash = new HashMap<Integer, List<Integer>>(); 
    wordLocation = new ArrayList<Integer>();
    wordLocation.add(TEST_NINE);
    insideHash.put(TEST_ORD2, wordLocation);
    indexMap.put("second", insideHash);
    
    SearchUtils.setIndexMap(SearchUtils.getIndexMap());
    assertEquals("testMultiIndexFile", indexMap, SearchUtils.getIndexMap());
    
    SearchUtils.resetIndex();
    SearchUtils.resetDocMap();
  }
  
  /**
   * JUNIT test case for NewPostings.
   */
  @Test
  public void testNewPostings()
  {
    SearchUtils.resetIndex();
    SearchUtils.resetDocMap();
    SearchUtils.createOpus(TEST_TITLE, TEST_AUTHOR, TEST_ORD, 
        new File(junitFilePath));
    
    newPostings = TEST_FIVE;
    
    assertEquals("testNewPostings", newPostings, SearchUtils.getNewPostings());
    SearchUtils.resetIndex();
    SearchUtils.resetDocMap();
  }
  
  /**
   * JUNIT test case for TotalPostings.
   */
  @Test
  public void testTotalPostings()
  {
    SearchUtils.resetNumbers();
    SearchUtils.resetIndex();
    SearchUtils.resetDocMap();
    SearchUtils.createOpus(TEST_TITLE, TEST_AUTHOR, TEST_ORD, 
        new File(junitFilePath));
    
    newPostings = TEST_FIVE;
    
    assertEquals("testTotalPostings", newPostings, SearchUtils.getTotalPostings());
    SearchUtils.resetIndex();
    SearchUtils.resetDocMap();
    SearchUtils.resetNumbers();
  }
  
  /**
   * JUNIT test case for NewTerms.
   */
  @Test
  public void testNewTerms()
  {
    SearchUtils.resetIndex();
    SearchUtils.resetDocMap();
    SearchUtils.createOpus(TEST_TITLE, TEST_AUTHOR, TEST_ORD, 
        new File(junitFilePath));
    
    SearchUtils.createOpus(TEST_TITLE, TEST_AUTHOR, TEST_ORD2, 
        new File(junitFilePath2));
    
    newTerms = TEST_ONE;
    
    assertEquals("testNewTerms", newTerms, SearchUtils.getNewTerms());
    SearchUtils.resetIndex();
    SearchUtils.resetDocMap();
  }
  
  /**
   * JUNIT test case for TotalTerms.
   */
  @Test
  public void testTotalTerms()
  {
    SearchUtils.resetNumbers();
    SearchUtils.resetIndex();
    SearchUtils.resetDocMap();
    SearchUtils.createOpus(TEST_TITLE, TEST_AUTHOR, TEST_ORD, 
        new File(junitFilePath));
    
    SearchUtils.createOpus(TEST_TITLE, TEST_AUTHOR, TEST_ORD2, 
        new File(junitFilePath2));
    
    newTerms = TEST_FOUR;
    
    assertEquals("testTotalTerms", newTerms, SearchUtils.getTotalTerms());
    SearchUtils.resetIndex();
    SearchUtils.resetDocMap();
    SearchUtils.resetNumbers();
  }
  
  /**
   * JUNIT test case for OpusNumber.
   */
  @Test
  public void testOpusNumber()
  {
    SearchUtils.resetNumbers();
    SearchUtils.resetIndex();
    SearchUtils.resetDocMap();
    SearchUtils.createOpus(TEST_TITLE, TEST_AUTHOR, TEST_ORD, 
        new File(junitFilePath));
    
    SearchUtils.createOpus(TEST_TITLE, TEST_AUTHOR, TEST_ORD2, 
        new File(junitFilePath2));
    
    assertEquals("testOpusNumber", TEST_TWO, SearchUtils.getOpusNumber());
    SearchUtils.resetIndex();
    SearchUtils.resetDocMap();
    SearchUtils.resetNumbers();
  }
  
  /**
   * JUNIT test case for NewDocumentsCount.
   */
  @Test
  public void testNewDocumentsCount()
  {
    SearchUtils.resetIndex();
    SearchUtils.resetDocMap();
    SearchUtils.createOpus(TEST_TITLE, TEST_AUTHOR, TEST_ORD, 
        new File(junitFilePath));
    
    newDocumentsCount = TEST_ONE;
    
    assertEquals("testNewDocumentsCount", newDocumentsCount, SearchUtils.getNewDocumentsCount());
    SearchUtils.resetIndex();
    SearchUtils.resetDocMap();
  }
  
  /**
   * JUNIT test case for Multiple Opus Documents.
   */
  @Test
  public void testMultiDocumentsMap()
  {
    SearchUtils.resetIndex();
    SearchUtils.resetDocMap();
    SearchUtils.createOpus(TEST_TITLE, TEST_AUTHOR, TEST_ORD, 
        new File(junitFilePath));
    
    SearchUtils.createOpus(TEST_TITLE, TEST_AUTHOR, TEST_ORD2, 
        new File(junitFilePath3)); 
    
    wordLocation = new ArrayList<Integer>();
    wordLocation.add(TEST_TWO);
    wordLocation.add(TEST_THREE);
    wordLocation.add(TEST_FOUR);
    wordLocation.add(TEST_FIVE);
    
    SearchUtils.setDocMap(SearchUtils.getDocMap());
    assertEquals("testMultiDocumentsMap", wordLocation, 
        SearchUtils.getDocMap().get(TEST_ORD2).get(TEST_ONE).getWordCounts());
    SearchUtils.resetIndex();
    SearchUtils.resetDocMap();
  }
  
  /**
   * JUNIT test case for Single Opus Documents.
   */
  @Test
  public void testSingleDocuments()
  {
    SearchUtils.resetIndex();
    SearchUtils.resetDocMap();
    SearchUtils.createOpus(TEST_TITLE, TEST_AUTHOR, TEST_ORD, 
        new File(junitFilePath3));
    
    assertEquals("testSingleDocuments", TEST_TITLE, 
        SearchUtils.getDocMap().get(TEST_ORD).get(TEST_TWO).getOpus().getTitle());
    SearchUtils.resetIndex();
    SearchUtils.resetDocMap();
  }
  
  /**
   * JUNIT test case for Single Opus Documents Number.
   */
  @Test
  public void testSingleDocumentsNumber()
  {
    SearchUtils.resetIndex();
    SearchUtils.resetDocMap();
    SearchUtils.createOpus(TEST_TITLE, TEST_AUTHOR, TEST_ORD, 
        new File(junitFilePath3));
    
    assertEquals("testSingleDocumentsNumber", TEST_TWO, 
        SearchUtils.getDocMap().get(TEST_ORD).get(TEST_TWO).getDocumentNumber());
    SearchUtils.resetIndex();
    SearchUtils.resetDocMap();
  }
  
  /**
   * JUNIT test case for Multiple Document Indexing.
   */
  @Test
  public void testMultiDocuments()
  {
    String actual;
    
    SearchUtils.resetIndex();
    SearchUtils.resetDocMap();
    SearchUtils.createOpus(TEST_TITLE, TEST_AUTHOR, TEST_ORD, 
        new File(junitFilePath2));
    SearchUtils.createOpus(TEST_TITLE, TEST_AUTHOR, TEST_ORD2, 
        new File(junitFilePath3));
    
    actual = SearchUtils.getDocMap().get(TEST_ORD2).get(TEST_TWO).getShortParagraph().trim();
    
    assertEquals("testMultiDocuments ", "JUNIT simple second test. the people take to "
        + "the streets at the end of this", actual);
    SearchUtils.resetIndex();
    SearchUtils.resetDocMap();
  }
  
  /**
   * JUNIT test case for Multiple Document Indexing while printing shortform.
   */
  @Test
  public void testMultiDocumentsShort()
  {
    String actual;
    
    SearchUtils.resetIndex();
    SearchUtils.resetDocMap();
    SearchUtils.createOpus(TEST_TITLE, TEST_AUTHOR, TEST_ORD, 
        new File(junitFilePath2));
    SearchUtils.createOpus(TEST_TITLE, TEST_AUTHOR, TEST_ORD2, 
        new File(junitFilePath3));
    
    actual = SearchUtils.getDocMap().get(TEST_ORD).get(TEST_TWO).getShortParagraph().trim();
    
    assertEquals("testMultiDocuments", "JUNIT simple second test. the", actual);
    SearchUtils.resetIndex();
    SearchUtils.resetDocMap();
  }
}
