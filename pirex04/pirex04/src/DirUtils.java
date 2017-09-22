package pirex04.src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Utility class for operations dealing with the data store.
 * @author team 04 - okadacs, johns2nc
 * This complies with the JMU honor code.
 */
public final class DirUtils
{
  private static String DEFAULT_DATA_STORE = "pirexData/";  
  private static String PIREX_HELP = "pirexHelp/";
  private static String SAVED_QUERIES = "pirexData/queries.txt";
  //private static final int SUM_MAGIC_NUM = -25623;
  
  /**
   * getPirexHelp returns the path to the help folder.
   * 
   * @return the path
   */
  public static String getPirexHelp()
  {
    return PIREX_HELP;
  }
  
  /**
   * changePirexHelp changes the pirex help folder and moves contents.
   * @param path is the path to the new folder
   */
  public static void changePirexHelp(String path)
  {
    Path oldPath = Paths.get(PIREX_HELP);
    Path newPath = Paths.get(path);
    if (!Files.exists(oldPath))
    {
      File file = new File(path);
      file.mkdir();
    }
    
    try
    {
      Files.move(oldPath, newPath);
      PIREX_HELP = path;
    }
    catch (IOException ioe)
    {
      System.out.println("Not valid path");
    }
  }
 
  /**
   * getDefaultDataStore returns the relative path to the default data store.
   * 
   * @return path to data store
   */
  public static String getDefaultDataStore()
  {
    return DEFAULT_DATA_STORE;
  }
  
  /**
   * setDefaultDataStore sets the new data store location.
   * 
   * @param dataStorePath contains the new path
   */
  private static void setDefaultDataStore(String dataStorePath)
  {
    DEFAULT_DATA_STORE = dataStorePath;
  }
  
  /**
   * Stores all files on the default data store.
   * 
   * @param file contain the file path
   * @throws IOException 
   * 
   * @return the file from the file path
   */
  public static File store(File file)
  {
    String pathname = getDefaultDataStore();
    
    File dir = new File(pathname);
    if(!dir.exists())
    {
      dir.mkdir();
    }
    
    try
    {
      Path path = Paths.get(file.getAbsolutePath());
      FileInputStream stream = new FileInputStream(file);
      FileOutputStream output = new FileOutputStream(pathname
          + path.getFileName().toString());
      FileChannel file1Channel = stream.getChannel();
      FileChannel file2Channel = output.getChannel();
      file1Channel.transferTo(0, file.length(), file2Channel);
      
      stream.close();
      output.close();
      
      return file;
    }
    catch(FileNotFoundException fe)
    {
      return null;
    }
    catch(IOException ioe)
    {
      return null;
    }
  }

  /**
   * Checks if the data store contains files.
   * @return true if the data store contains files
   */
  public static boolean checkDataStore()
  {
    boolean dataStore;
    File file = new File (DirUtils.getDefaultDataStore());
    
    dataStore = false;
    if(file.isDirectory())
    {
      System.out.println("Checking files in Pirex Data Store");

      if(file.list().length == 1)
      {
        System.out.println("Data Store contains only .ser file\nEntering Admin Mode.");
      }
      else if(file.list().length > 1)
      {
        System.out.println("Data store contains " + file.list().length  +" files\n");
        dataStore = true;
      }
      else
      {
        System.out.println("Data Store contains no files\nEntering Admin Mode.");
      }
    }
    
    return dataStore;
  }
  
  /**
   * Clears the default data store.
   */
  public static void clearDataStore()
  {
    File dir = new File(DirUtils.getDefaultDataStore());
    
    File[] files = dir.listFiles();
    
    if (files != null && files.length != 0)
    {
      for(File f : files)
      {
        f.delete();
      }
    }
    
    File f = new File(SAVED_QUERIES);
    f.delete();
    
    SearchUtils.clearMaps();
    //SearchUtils.getDocMap().remove(SUM_MAGIC_NUM);
  }
  

  
  /**
   * changeDataStore changes the data store location.
   * 
   * @param path is the path of the new data store
   */
  public static void changeDataStore(String path)
  {
    Path dir = Paths.get(path);
    
    if (!Files.exists(dir))
    {
      File file = new File(path);
      file.mkdir();
    }
    
    setDefaultDataStore(path);
  }
  
  /**
   * Delete single opus from data store.
   * @param opus to delete.
   */
  public static void deleteOpus(Opus opus)
  {
    File fp = new File(opus.getFilePath());
    fp.delete();
    SearchUtils.removeOpus(opus.getOrdNumber());
    
    File dir = new File(getDefaultDataStore());
    String[] files = dir.list();
    
    for(String fs : files)
    {
      fp = new File(fs);
      SearchUtils.createOpus(SearchUtils.parseText(fp, "Title: "), 
          SearchUtils.parseText(fp, "Author: "), SearchUtils.parseOrd(fp), fp);
    }
  }
  
  /**
   * saveQueries saves queries to a file.
   * @param query the terms in the text box
   * 
   * @return if it was successful
   */
  public static boolean saveQueries(String query)
  {
    boolean result = true;
    String queryList = "";
    File fp = new File(SAVED_QUERIES);
    String lineString = "\r\n";

    if (query.length() == 0 || query == null)
    {
      result = false;
    }

    else
    {
      
      try
      {
        if(!fp.exists())
        {
          fp.createNewFile();
        }
        
        for (String line: Files.readAllLines(Paths.get(SAVED_QUERIES)))
        {
          if(!(line.equals("")))
            queryList+=(line+lineString);
        }
        queryList+=(query+lineString);
        FileWriter file = new FileWriter(fp);
        
        file.write(queryList);
        file.close();
      }
      catch (IOException e)
      {
        result = false;
      }
    }
    return result;

  }
  
  /**
   * getSavedQueries returns an array list of the queries.
   * 
   * @return array list
   */
  public static ArrayList<String> getSavedQueries()
  {
    File fp = new File(SAVED_QUERIES);

    try
    {
      FileInputStream stream = new FileInputStream(fp);
      InputStreamReader in = new InputStreamReader(stream);
      BufferedReader br = new BufferedReader(in);
      String line;
      ArrayList<String> queries = new ArrayList<String>();

      while((line = br.readLine()) != null)
      {
        if(!line.equals("\n"))
        {
          queries.add(line);
        }
      }
      
      br.close();
      return queries;
    }
    catch (FileNotFoundException e)
    {
      return null;
    }
    catch (IOException e)
    {
      return null;
    }
    
  }
}
