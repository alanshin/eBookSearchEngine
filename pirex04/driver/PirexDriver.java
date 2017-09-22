package driver;
import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JDialog;
import javax.swing.ImageIcon;
import pirex04.src.DirUtils;
import pirex04.src.Documents;
import pirex04.src.Opus;
import pirex04.src.SearchEngine;
import pirex04.src.SearchUtils;
import pirex04.src.gui.PirexWindow;

/**
 * Driver class for Pirex search engine.
 * @author Team 04 - okadacs, mparchu, johns2nc
 *
 * This code complies with the JMU Honor Code.
 */
public final class PirexDriver
{
  private static ArrayList<String> stringFileList;
  private static final int CASE_ONE = 1;
  private static final int CASE_TWO = 2;
  private static final int CASE_THREE = 3;
  private static final int CASE_FOUR = 4;
  private static final int CASE_FIVE = 5;
  private static final int X_VALUE = 800;
  private static final int Y_VALUE = 600;
  private static int numOption;
  private static Scanner sc;

  /**
   * Main function.
   * 
   * @param args command line arguments
   * 
   * @throws InterruptedException.
   */
  public static void main(String[] args)
  {
    if (!SearchUtils.deserialize())
    {
      DirUtils.clearDataStore();
    }
    /*
    sc = new Scanner(System.in); 
    stringFileList = new ArrayList<String>();
    Object[] mode = {"GUI Mode", "Command-Line mode", "Exit"};
    
    int pirexMode = JOptionPane.showOptionDialog(null, 
        "Which mode do you want to run Pirex in?", "Pirex Search Engine", 
        JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
        null, mode, mode[1]);
    */
    int pirexMode = 0;
    switch(pirexMode)
    {
      case 0:
        JDialog frame = new PirexWindow();
        Image img = new ImageIcon("PirexIconJPG.jpg").getImage();
        frame.getOwner().setIconImage(img);
        frame.setMinimumSize(new Dimension(X_VALUE,Y_VALUE));
        frame.setModal(true);
        frame.setVisible(true);
        break;
      case 1:
        System.out.println("***Pirex Search Engine***\n");
        
        if(!DirUtils.checkDataStore())
        {
          adminMode();
        }
        
        printMenu();
        break;
      default:
        break;
        
    }
    
    SearchUtils.serialize();

  }
  
  /**
   * Function that creates the menu list.
   */
  private static void printMenu()
  {
    System.out.println("Enter number for following options:");
    System.out.println("  1. Admin Mode");
    System.out.println("  2. User Mode");
    System.out.println("  3. Quit");
    
    numOption = parseLine(sc.nextLine());
    
    switch(numOption)
    {
      case CASE_ONE:
        adminMode();
        break;
      case CASE_TWO:
        userMode();
        break;
      case CASE_THREE:
        System.out.println("Shutting Down . . .");
        System.exit(0);
        break;
      default:
        printMenu();
        break;
    }
    
  }

  /**
   * Outputs files to admin.
   * @param list of files to output
   */
  private static void printStringFileList(ArrayList<String> list)
  {
    System.out.println("================\n" + "Current files in list:");
    
    for(int i = 0; i < list.size(); i++)
    {
      System.out.printf("%d: %s\n", i+1, list.get(i));
    }
    
    System.out.println("================");
    adminMode();
  }
  
  /**
   * Parses line for an int.
   * @param line to parse for int
   * @return the int the admin entered or 0 otherwise
   */
  private static int parseLine(String line)
  {
    try
    {
      return Integer.parseInt(line);     
    }
    catch(NumberFormatException nfe)
    {
      return 0;
    }
  }
  
  /**
   * Admin menu of options.
   */
  private static void adminMode()
  {
    System.out.println("===Admin Mode===");
    String filePath = "";
    System.out.println("  1. To enter a file path");
    System.out.println("  2. Delete previous file");
    System.out.println("  3. Upload Files to Data Store");
    System.out.println("  4. Delete entire Data Store");
    System.out.println("  5. To exit to main menu");
    
    numOption = parseLine(sc.nextLine());
    
    switch(numOption)
    {
      case CASE_ONE:
        System.out.println("Enter file path");
        filePath = sc.nextLine();
        String[] paths;
        if(filePath.equals(""))
        {
          System.out.println("No File name entered!");
          adminMode();
        }
        
        paths = filePath.split(" ");
        for(int i = 0; i < paths.length; i++)
        {
          stringFileList.add(paths[i]);
        }
        printStringFileList(stringFileList);
        break;
      case CASE_TWO:
        if(stringFileList.size() > 0)
        {
          stringFileList.remove(stringFileList.size() - 1);
        }
        printStringFileList(stringFileList);
        break;
      case CASE_THREE:
        if(stringFileList.size() > 0)
        {
          String[] files = stringFileList.toArray(new String[0]);
          for(String s : files)
          {
            File fp = DirUtils.store(new File(s));
            if(fp != null)
            {
              SearchUtils.createOpus(SearchUtils.parseText(fp, "Title: "), 
                  SearchUtils.parseText(fp, "Author: "), SearchUtils.parseOrd(fp), fp);
            }
          }
        }
        stringFileList.clear();
        adminMode();
        break;
      case CASE_FOUR:
        DirUtils.clearDataStore();
        System.out.println("Successfully cleared Data Store");
        adminMode();
        break;
      case CASE_FIVE:
        stringFileList.clear();
        printMenu();
        break;
      default:
        stringFileList.clear();
        adminMode();
        break;
    }       
  }
  
  /**
   * UserMode a method that will check if the Index needs to be updated, as well
   * as ask the user for a list of search terms that will be passed into the searching
   * functionality. Will return to the menu if less than 1 term, over 100 terms, or if
   * the list of opus read in is null;
   */
  private static void userMode()
  {
    String query;
    String[] formattedQuery;

    
    if(!DirUtils.checkDataStore())
    {
      adminMode();
    }
    
    System.out.println("===User Mode===");
        
    // Asks the user for the search terms
    System.out.println("Enter Search Terms (Seperated by spaces): ");
    query = sc.nextLine();
    
    
    if (!query.matches(".*\\w.*"))
    {
      System.out.println("Invalid Search, can not enter all spaces ... returning to Menu.");
      printMenu();    
    }
    
    formattedQuery = SearchUtils.formatQuery(query);
    
    if(formattedQuery == null)
    {
      System.out.println("Invalid Search term limit");
      printMenu();
    }
    
    List<Documents> list = SearchEngine.search(formattedQuery);
    
    System.out.println("Results");
    
    for(Documents doc : list)
    {
      Opus opus = doc.getOpus();
      System.out.println(opus.toString() + " Short Form: " + doc.getShortParagraph());
    }
    System.out.println();
    printMenu();
  }
}
