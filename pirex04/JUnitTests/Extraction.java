package JUnitTests;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;

import pirex04.src.DirUtils;
import pirex04.src.SearchUtils;

/**
 * 
 * @author okadacs
 *
 */
public class Extraction
{
  private final int ord1 = 53391;
  private final String filePath = "testFiles/text4.txt";
  private final String title = "Home Amusements";
  private final String author = "M. E. W. Sherwood";

  /**
   * Test title extraction.
   */
  @Test
  public void titleExtractionTest()
  {
    File file = new File(filePath);
    DirUtils.clearDataStore();
    DirUtils.store(file);
    
    SearchUtils.createOpus(title, author, ord1, file);
    
    assertEquals("titleExtraction()", title, SearchUtils.getOpus(ord1).getTitle());
    
    DirUtils.clearDataStore();   
  }
  
  /**
   * Test author extraction.
   */
  @Test
  public void authorExtractionTest()
  {
    File file = new File(filePath);
    DirUtils.clearDataStore();
    DirUtils.store(file);
    
    SearchUtils.createOpus(title, author, ord1, file);
    
    assertEquals("authorExtraction()", author, SearchUtils.getOpus(ord1).getAuthor());
    
    DirUtils.clearDataStore();   
  }

}
