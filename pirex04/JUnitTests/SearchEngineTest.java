package JUnitTests;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import pirex04.src.Documents;
import pirex04.src.SearchEngine;
import pirex04.src.SearchUtils;

/**
 * JUNIT test cases that tests the Searching function of the SearchEngine
 * class.
 * 
 * @author mparchu
 * 
 * This complies with the JMU honor code.
 */
public class SearchEngineTest
{
  private static final int TEST_ONE = 1;
  private static final int TEST_ORD = 1234;
  private static final int TEST_ORD2 = 1235;
  private static final int TEST_TWO = 2;
  private static final int TEST_ZERO = 0;
  
  private static final String TEST_AUTHOR = "TestingTitle";
  private static final String TEST_EXPECTED = "JUNIT simple second test. the "
      + "people take to the streets at the end of this line just trying to get to a "
      + "long number that is all.";
  private static final String TEST_JUNIT = "junit";
  private static final String TEST_STREETS = "streets";
  private static final String TEST_TITLE = "TestingAuthor";
  
  private String junitFilePath = 
      "testFiles/JUNITTESTSEARCH3.txt";
  private String junitFilePath3 = 
      "testFiles/JUNITTESTSEARCH5.txt";
  
  /**
   * JUNIT test case dealing with a Single Term and Single EBook.
   */
  @Test
  public void testSingleTermSingleBook()
  {
    String[] query = new String[] {TEST_STREETS};
    
    SearchUtils.resetIndex();
    SearchUtils.resetDocMap();
    SearchUtils.createOpus(TEST_TITLE, TEST_AUTHOR, TEST_ORD, 
        new File(junitFilePath3));
      
    assertEquals("testSingleTermSingleBook", TEST_EXPECTED, SearchEngine.search(query)
        .get(TEST_ZERO).getParagraph().trim());
  }
  
  /**
   * JUNIT test case dealing with a Single Term and Single EBook with a not term.
   */
  @Test
  public void testSingleTermSingleBookNotTerm()
  {
    String query = TEST_STREETS + " -junit";
    
    SearchUtils.resetIndex();
    SearchUtils.resetDocMap();
    SearchUtils.createOpus(TEST_TITLE, TEST_AUTHOR, TEST_ORD, 
        new File(junitFilePath3));
      
    assertEquals("testSingleTermSingleBookNotTerm", TEST_EXPECTED, 
        SearchEngine.search(SearchUtils.formatQuery(query))
        .get(TEST_ZERO).getParagraph().trim());
  }
  
  /**
   * JUNIT test case dealing with a Single Term and Single EBook with a hundred terms.
   */
  @Test(expected=NullPointerException.class)
  public void testSingleTermSingleBookHundredTerm()
  {
    String query = "1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 "
        + "1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1"
        + "1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1"
        + "1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1"
        + "1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 ";
    
    SearchUtils.resetIndex();
    SearchUtils.resetDocMap();
    SearchUtils.createOpus(TEST_TITLE, TEST_AUTHOR, TEST_ORD, 
        new File(junitFilePath3));
    SearchEngine.search(SearchUtils.formatQuery(query));
  }
  
  /**
   * JUNIT test case dealing with a Single Term and Multiple EBook.
   */
  @Test
  public void testSingleTermMultiBook()
  {
    SearchUtils.resetIndex();
    SearchUtils.resetDocMap();
    String[] query = new String[] {TEST_STREETS};
    
    SearchUtils.createOpus(TEST_TITLE, TEST_AUTHOR, TEST_ORD, 
        new File(junitFilePath));
    
    SearchUtils.createOpus(TEST_TITLE, TEST_AUTHOR, TEST_ORD2, 
        new File(junitFilePath3));
      
    assertEquals("testSingleTermMultiBook", TEST_EXPECTED, SearchEngine.search(query)
        .get(TEST_ZERO).getParagraph().trim());
    SearchUtils.resetIndex();
    SearchUtils.resetDocMap();
  }
  
  /**
   * JUNIT test case dealing with a No found Term and Multiple EBook.
   */
  @Test
  public void testNotFoundTermMultiBook()
  {
    SearchUtils.resetIndex();
    SearchUtils.resetDocMap();
    List<Documents> docList;
    String[] query = new String[] {"chicken"};
    
    SearchUtils.createOpus(TEST_TITLE, TEST_AUTHOR, TEST_ORD, 
        new File(junitFilePath));
    
    SearchUtils.createOpus(TEST_TITLE, TEST_AUTHOR, TEST_ORD2, 
        new File(junitFilePath3));
      
    docList = new ArrayList<Documents>();
    assertEquals("testNotFoundTermMultiBook", docList, SearchEngine.search(query));
    SearchUtils.resetIndex();
    SearchUtils.resetDocMap();
  }
  
  /**
   * JUNIT test case dealing with a Multiple Term and Single EBook.
   */
  @Test
  public void testMultiTermSingleBook()
  {
    String[] query = new String[] {TEST_STREETS, TEST_JUNIT};
    
    SearchUtils.resetIndex();
    SearchUtils.resetDocMap();
    SearchUtils.createOpus(TEST_TITLE, TEST_AUTHOR, TEST_ORD, 
        new File(junitFilePath3));
      
    assertEquals("testMultiTermSingleBook", TEST_EXPECTED, SearchEngine.search(query)
        .get(TEST_TWO).getParagraph().trim());
    SearchUtils.resetIndex();
    SearchUtils.resetDocMap();
  }
 
  /**
   * JUNIT test case dealing with a Multiple Term and Multiple EBook.
   */
  @Test
  public void testMultiTermMultiBook()
  {
    String[] query = new String[] {TEST_STREETS, TEST_JUNIT};
    
    SearchUtils.resetIndex();
    SearchUtils.resetDocMap();
    SearchUtils.createOpus(TEST_TITLE, TEST_AUTHOR, TEST_ORD, 
        new File(junitFilePath3));
    
    SearchUtils.createOpus(TEST_TITLE, TEST_AUTHOR, TEST_ORD2, 
        new File(junitFilePath));
      
    assertEquals("testMultiTermMultiBook", TEST_EXPECTED, SearchEngine.search(query)
        .get(TEST_ONE).getParagraph().trim());
    SearchUtils.resetIndex();
    SearchUtils.resetDocMap();
  } 
  
  
}
