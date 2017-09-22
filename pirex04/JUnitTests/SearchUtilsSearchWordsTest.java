package JUnitTests;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;

import pirex04.src.SearchUtils;

/**
 * JUNIT test cases that test the Search Terms being handled in the
 * SearchUtils class.
 * 
 * @author mparchu
 * 
 * This complies with the JMU honor code. *
 */
public class SearchUtilsSearchWordsTest
{
  private static final int TEST_ORD = 1234;
  private static final int TEST_ORD2 = 1235;
  private static final int TEST_ORD3 = 3409;
  private static final int TEST_ONE = 1;
  private static final String TEST_AUTHOR = "TestingTitle";
  private static final String TEST_TITLE = "TestingAuthor";
  private static final String WORD_HUNDRED = "a a a a a a a a a a a a a a "
      + "b b b b b b b b b b b b b b b b b b b b b b b b b b b b b b b b b"
      + "c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c"
      + "d d d d d d d d d d d d d d d d d d d d d d d d d d d d d d d d d"
      + "e e e e e e e e e e e e e e e e e e e e e e e e e e e e e e e e e";
  
  private String junitFilePath = 
      "testFiles/JUNITTESTSEARCH3.txt";
  private String junitFilePath2 = 
      "testFiles/JUNITTESTSEARCH4.txt";
  private String junitFilePath3 = 
      "testFiles/text5.txt";

  /**
   * JUNIT test case dealing with Removing an Opus.
   */
  @Test
  public void testRemoveOpus()
  {
    SearchUtils.resetIndex();
    SearchUtils.resetDocMap();
    SearchUtils.createOpus(TEST_TITLE, TEST_AUTHOR, TEST_ORD, 
        new File(junitFilePath));
    SearchUtils.createOpus(TEST_TITLE, TEST_AUTHOR, TEST_ORD2, 
        new File(junitFilePath2));
    SearchUtils.removeOpus(TEST_ORD);
    
    assertEquals("testRemoveOpus", null, SearchUtils.getOpus(TEST_ORD));
    SearchUtils.resetIndex();
    SearchUtils.resetDocMap();
  }
  
  /**
   * JUNIT test case dealing with Getting an Opus Object.
   */
  @Test
  public void testGetOpusObjects()
  {
    SearchUtils.resetIndex();
    SearchUtils.resetDocMap();
    SearchUtils.createOpus(TEST_TITLE, TEST_AUTHOR, TEST_ORD, 
        new File(junitFilePath));
    SearchUtils.createOpus(TEST_TITLE, TEST_AUTHOR, TEST_ORD2, 
        new File(junitFilePath2));
    
    SearchUtils.setOpusObjects(SearchUtils.getOpusObjects());
    assertEquals("testGetOpusObjects", TEST_AUTHOR, 
        SearchUtils.getOpusObjects().get(TEST_ORD).getAuthor());
    SearchUtils.resetIndex();
    SearchUtils.resetDocMap();
  }
  
  /**
   * JUNIT test case dealing with formatting a Query.
   */
  @Test
  public void testFormatQuery()
  {
    SearchUtils.resetIndex();
    SearchUtils.resetDocMap();
    String[] actual;
    String[] query = new String[] {"apple", "orange"};
    actual = SearchUtils.formatQuery("apple orange");
    
    
    assertEquals("testFormatQuery", query[TEST_ONE], actual[TEST_ONE]);
    SearchUtils.resetIndex();
    SearchUtils.resetDocMap();
  }

  /**
   * JUNIT test case dealing with formatting a Query of over a Hundred terms.
   */
  @Test(expected=NullPointerException.class)
  public void testFormatQueryHundredTerms()
  {
    SearchUtils.resetIndex();
    SearchUtils.resetDocMap();
    String[] actual;
    actual = SearchUtils.formatQuery(WORD_HUNDRED);
    
    assertEquals("testFormatQueryHundredTerms", null, actual[TEST_ONE]);
    SearchUtils.resetIndex();
    SearchUtils.resetDocMap();
  }
  
  /**
   * JUNIT test case dealing with Parsing Text without a file.
   */
  @Test
  public void testParseTextFileNotFound()
  {
    SearchUtils.resetIndex();
    SearchUtils.resetDocMap();
    
    assertEquals("testParseTextFileNotFound", null, 
        SearchUtils.parseText(new File("nothing"), "FileNotFound"));
    SearchUtils.resetIndex();
    SearchUtils.resetDocMap();
  }
  
  /**
   * JUNIT test case dealing with Parsing an Ordinal number.
   */
  @Test
  public void testParseOrd()
  {
    SearchUtils.resetIndex();
    SearchUtils.resetDocMap();
    
    assertEquals("testParseOrd", TEST_ORD3, 
        SearchUtils.parseOrd(new File(junitFilePath3)));
    SearchUtils.resetIndex();
    SearchUtils.resetDocMap();
  }
  
  /**
   * JUNIT test case dealing with Parsing an Ordinal Number without a file.
   */
  @Test
  public void testParseOrdFileNotFound()
  {
    SearchUtils.resetIndex();
    SearchUtils.resetDocMap();
    
    assertEquals("testParseOrdFileNotFound", -1, 
        SearchUtils.parseOrd(new File("Nothing")));
    SearchUtils.resetIndex();
    SearchUtils.resetDocMap();
  }
}
