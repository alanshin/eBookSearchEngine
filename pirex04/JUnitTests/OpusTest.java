package JUnitTests;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;

import pirex04.src.Opus;

/**
 * 
 * @author Team 4 - okadacs, johns2nc
 *
 */
public class OpusTest
{
  private static final int TEST_ORD_NUMBER = 12345;
  //private static final double PRECISION = 0.001;
  
  /**
   * Tests Author.
   */
  @Test
  public void testAuthor()
  {
    Opus opus;
    File file;
    String title, author;
    int ordNumber;
    
    file = new File("testfile");
    author = "Smith Doe";
    title = "Charger";
    ordNumber = TEST_ORD_NUMBER;
    
    opus = new Opus(title, author, ordNumber, file);
    
    assertEquals("getAuthor()", author, opus.getAuthor());
  }
  
  /**
   * Tests title.
   */
  @Test
  public void testTitle()
  {
    Opus opus;
    File file;
    String title, author;
    int ordNumber;
    
    file = new File("test");
    author = "John Smith";
    title = "hello";
    ordNumber = TEST_ORD_NUMBER;
    
    opus = new Opus(title, author, ordNumber, file);
    
    assertEquals("getTitle() test", title, opus.getTitle());
  }
  
  
  /**
   * Tests ordNumber.
   */
  @Test
  public void testOrdNumber()
  {
    Opus opus;
    String title, author;
    int ordNumber;
    
    author = "John green";
    title = "california";
    ordNumber = TEST_ORD_NUMBER;
    
    opus = new Opus(title, author, ordNumber, null);
    
    assertEquals("getOrdNumber()", ordNumber, opus.getOrdNumber());
  }
  
  /**
   * Tests set Title.
   */
  @Test
  public void testSetTitle()
  {
    Opus opus;
    File file;
    String title, author, nullTitle;
    int ordNumber;
    
    file = new File("files/text2.txt");
    author = "John black";
    title = "httr";
    nullTitle = "NO TITLE";
    ordNumber = TEST_ORD_NUMBER;
    
    opus = new Opus(null, author, ordNumber, file);
    
    assertEquals("getTitle()", nullTitle, opus.getTitle());
    
    opus.setTitle(title);
    assertEquals("Title test", title, opus.getTitle());
  }
  
  /**
   * Tests set author.
   */
  @Test
  public void testSetAuthor()
  {
    Opus opus;
    String  author, nullAuthor;
    
    author = "John Doe";
    nullAuthor = "NO AUTHOR";
    
    opus = new Opus();
    
    assertEquals("setAuthor", nullAuthor, opus.getAuthor());
    
    opus.setAuthor(author);
    assertEquals("Author test", author, opus.getAuthor());
  }
  
  /**
   * Tests to see if file is correct.
   */
  @Test
  public void fileTest()
  {
    Opus opus;
    File file;
    String path = "testFiles/text12.txt";
    
    file = new File(path);
    
    opus = new Opus(null, null, -1, file);
    
    assertEquals("file test", file.getAbsolutePath(), opus.getFilePath());
  }

}
