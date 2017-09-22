package JUnitTests;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import pirex04.src.DirUtils;

/**
 * JUnit test that tests DirUtils in the handling of a list of files and
 * determining if the feature of being able to load at least a total of 10
 * files is implemented.
 * 
 * @author Team 4 - okadacs
 *
 * This code complies with the JMU Honor Code.
 */
public class Load10FilesTest
{
  private static final int TEN = 10;
  private String textExtension = ".txt";
  
  /**
   * JUnit test that loads and checks if 10 files can load successfully.
   */
  @Test
  public void testLoadFiles()
  {
    String[] files = new String[TEN];
    ArrayList<String> actual;
    File dataStore = new File("files/");
    
    for (int i = 0; i < files.length; i++)
    {
      files[i] = "files/text" + i + textExtension;
    }
    
    for (String i : files)
    {
      DirUtils.store(new File(i));
    }
    
    //change names to match list
    for (int i = 0; i < files.length; i++)
    {
      files[i] = "text" + i + textExtension;
    }
    
    actual = new ArrayList<String>(Arrays.asList(dataStore.list()));
    
    for (int i = 0; i < files.length; i++)
    {
      assertTrue("compareFile " + i, actual.contains(files[i]));
    }
    
    DirUtils.clearDataStore();
  }
}
