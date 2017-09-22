package JUnitTests;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pirex04.src.DirUtils;
import pirex04.src.SearchUtils;

/**
 * JUNIT testing of the DirUtils class and the capabilities of creating, storing, 
 * and checking the data store for the pirexData.
 * 
 * @author Team04 - johns2nc, okadacs
 *
 * This code complies with the JMU Honor Code.
 */
public class DirUtilsTest
{ 
  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final int ord1 = 53391;
  private final int ord2 = 53409;
  
  /**
   * Sets up the use of printing statements found in the methods.
   */
  @After
  public void cleanUpStreams()
  {
    System.setOut(null);
  }
  
  /**
   * Sets up the use of printing statements found in the methods.
   */
  @Before
  public void setUpStreams()
  {
    System.setOut(new PrintStream(outContent));
  }
  
  /**
   * checks if DirUtils can create and return proper number of opus objects.
   */
  @Test
  public void createAndGetOpus()
  {   
    String[] files = {"testFiles/text4.txt", "testFiles/text5.txt"};
    DirUtils.clearDataStore();
    DirUtils.store(new File(files[0]));
    DirUtils.store(new File(files[1]));
    
    SearchUtils.createOpus("Home Amusements", "M. E. W. Sherwood", ord1, new File(files[0]));
    SearchUtils.createOpus("Ocean Gardens. The History of the Marine Aquarium",
        "H. Noel Humphreys", ord2, new File(files[1]));
    
    
    assertEquals("Create opus test", "", outContent.toString());
    
    DirUtils.clearDataStore();    
  }
  
  /**
   * check if data store is empty.
   */
  @Test
  public void checkDataStoreZero()
  {
    DirUtils.clearDataStore();
    assertEquals("empty data store test", false, DirUtils.checkDataStore());  
  }
  
  /**
   * Checks if data store is full.
   */
  @Test
  public void checkDataStoreFull()
  {
    DirUtils.clearDataStore();
    String[] files = {"testFiles/text1.txt", "testFiles/text2.txt", "testFiles/text3.txt"};
    DirUtils.store(new File(files[0]));
    DirUtils.store(new File(files[1]));
    DirUtils.store(new File(files[2]));
    assertEquals("Full data store test", true, DirUtils.checkDataStore());
    DirUtils.clearDataStore();
  }
}
