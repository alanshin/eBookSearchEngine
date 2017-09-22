package pirex04.src.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import pirex04.src.DirUtils;

/**
 * Class containing main window for gui that takes files, search terms and displays results.
 * 
 * @author team04 - johns2nc, shinah
 *
 */
public class PirexWindow extends JDialog implements ChangeListener, ActionListener
{
  private static final long serialVersionUID = 1L;
  private static final String SAVE = "Save";
  private static final String LOAD = "Load";
  private static final String EXPORT = "Export";
  private static final String EXIT = "Exit";
  private static final String ABOUT = "About";
  private static final String INDEX = "Index";
  private static final String SOURCES = "Sources";
  private static final String CLEAR = "Clear Data Store";
  private static final String DOTS = " . . . ";
  private static final int QUERY_LENGTH = 50;
  private static final int SUB_LENGTH = 47;
  private Container contentPane;
  private JTabbedPane tabs;
  private PirexSearchTab searchTab;
  private PirexLoadTab loadTab;
  private PirexSummarizeTab sumTab;
  private JMenuBar menuBar;
  private JMenu fileMenu, helpMenu, optionsMenu;
  private JMenuItem save, load, export, exit, about, sources, index, clearDataStore;

  /**
   * Constructor for the GUI interface. This constructor will construct the tabs and layout of each
   * tab.
   */
  public PirexWindow()
  {
    setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    setTitle("Pirex");
    tabs = new JTabbedPane();
    tabs.addChangeListener(this);
    // tabs.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));
    contentPane = getContentPane();
    contentPane.setLayout(new BorderLayout());
    contentPane.add(tabs, BorderLayout.CENTER);
    
    createMenuBar();
    
    searchTab = new PirexSearchTab(tabs);
    loadTab = new PirexLoadTab(tabs);
    sumTab = new PirexSummarizeTab(tabs);
  }

  
  /**
   * state change method.
   * 
   * @param ce
   *          change event var
   */
  public void stateChanged(ChangeEvent ce)
  {
    JTabbedPane sourceTab = (JTabbedPane) ce.getSource();
    int indextab = sourceTab.getSelectedIndex();
    if (sourceTab.getTitleAt(indextab).equals("Summarize Documents"))
    {
      sumTab.updateSummaryTab();
    }
    
  }
  
  /**
   * method that creates the menu bar.
   */
  public void createMenuBar()
  {
    menuBar = new JMenuBar();
    
    //FILE MENU
    fileMenu = new JMenu("File");
    
    save = new JMenuItem(SAVE);
    save.addActionListener(this);
    fileMenu.add(save);
    
    load = new JMenuItem(LOAD);
    load.addActionListener(this);
    fileMenu.add(load);
    
    export = new JMenuItem(EXPORT);  
    export.addActionListener(this);
    fileMenu.add(export);   
    
    exit = new JMenuItem(EXIT);
    exit.addActionListener(this);
    fileMenu.add(exit);
    
    //HELP MENU
    helpMenu = new JMenu("Help");
    
    about = new JMenuItem(ABOUT);
    about.addActionListener(this);
    helpMenu.add(about);
    
    index = new JMenuItem(INDEX);
    index.addActionListener(this);
    helpMenu.add(index);
    
    //OPTIONS MENU
    optionsMenu = new JMenu("Options");

    sources = new JMenuItem(SOURCES);
    sources.addActionListener(this);
    optionsMenu.add(sources);
    
    clearDataStore = new JMenuItem(CLEAR);
    clearDataStore.addActionListener(this);
    optionsMenu.add(clearDataStore);
    
    menuBar.add(fileMenu);
    menuBar.add(helpMenu);
    menuBar.add(optionsMenu);
    
    contentPane.add(menuBar, BorderLayout.NORTH);
  }

  /**
   * ActionListener method.
   * @param event ActionEvent variable.
   */
  public void actionPerformed(ActionEvent event)
  {
    String command = event.getActionCommand();

    switch (command)
    {
      case SAVE:
        
        if(!DirUtils.saveQueries(searchTab.getSearchQueries()))
        {
          JOptionPane.showMessageDialog(null, "Problem saving search!",
              null, JOptionPane.ERROR_MESSAGE);
        }
        else
        {
          JOptionPane.showMessageDialog(null, "Save Succesful!",
              null, JOptionPane.INFORMATION_MESSAGE);
        }
        break;

      case LOAD:
        
        ArrayList<String> queries = DirUtils.getSavedQueries();
        if(queries == null)
        {
          JOptionPane.showMessageDialog(null, "No saved searches!",
              null, JOptionPane.ERROR_MESSAGE);
        }
        else
        {
          String newquery;
          ArrayList<String> queriesTemp = new ArrayList<String>();
          HashMap<String, String> queryMap = new HashMap<String,String>();
          for(String query : queries)
          {
            if(query.length() > QUERY_LENGTH)
            {
              newquery = query.substring(0,SUB_LENGTH) + DOTS;
              queriesTemp.add(newquery);
              queryMap.put(newquery, query);
            }
            else
            {
              newquery = query;
              queriesTemp.add(newquery);
              queryMap.put(newquery, query);
            }
          }
          
          String selectedQuery = (String) JOptionPane.showInputDialog(null, "Saved Queries", 
              "Select one", JOptionPane.PLAIN_MESSAGE, null, queriesTemp.toArray(), null);
          searchTab.setSearchBox(queryMap.get(selectedQuery));
        }
        break;
      
      case EXPORT:
        JFileChooser saveExport = new JFileChooser();
        String text = searchTab.getResults();
        if(text == null || text.equals(""))
        {
          JOptionPane.showMessageDialog(null, "Empty search results!");
          break;
        }
        saveExport.showSaveDialog(null);
        
        File file = saveExport.getSelectedFile();
        String fileName = file.getAbsolutePath();
        BufferedWriter bw;
        try
        {
          bw = new BufferedWriter(new FileWriter(fileName+".txt"));
          bw.write(text);
          bw.close();
        }
        catch (IOException e)
        {
          JOptionPane.showMessageDialog(null, "Error in exporting!");
        }
        break;
        
      case EXIT:
        dispose();
        break;
        
      case ABOUT:
        JOptionPane.showMessageDialog(null, "1. Navigate to Load Documents tab.\n"
            + "2. Browse for Gutenberg file.\n"
            + "3. Click process to index the file.\n"
            + "4. Navigate to Search Tab.\n"
            + "5. Enter in search terms in the search bar and press enter.\n"
            + "6. Click on search results to reveal long form.\n"
            + "7. Navigate to Summarize Tab to see all files processed.");
    
        break;
       
      case INDEX:
        try
        {
          File htmlFile = new File("Help Page/Help Page/index.html");
          Desktop.getDesktop().browse(htmlFile.toURI());
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
        break;
        
      case SOURCES:
        loadTab.setFileChooser();
        break;
      
      case CLEAR:
        DirUtils.clearDataStore();
        sumTab.clearSummary();
        loadTab.setClear();
        searchTab.setDocumentCounterToZero();
        searchTab.setTextDisplayAreaToEmpty();
        searchTab.validatePanel();
        break;
      default:
        break;
    }    
    
  }
}
