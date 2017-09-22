package pirex04.src.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

import pirex04.src.DirUtils;
import pirex04.src.Documents;
import pirex04.src.SearchUtils;
import pirex04.src.Opus;

/**
 * PirexLoadTab contains the gui and functionality for the loading tab.
 * 
 * @author shina, okadacs
 */
public class PirexLoadTab implements ActionListener
{
  private static final int FIFTEEN = 15;
  private static final int ONE = 1;
  private static final int TWENTYFIVE = 25;
  private static final int SIXH = 600;
  private static final int THREEHN = 319;
  private static final int SIXHST = 660;
  private static final int SUM_MAGIC_NUM = -25623;
  private static final String TLE = "Title: ";
  private static final String ATH = "Author: ";
  private static final String PRC = "Process";
  private static final String BRW = "Browse";
  private static final String SINGLESPACE = " ";

  private static String summ;
  private static int indexTotal = 0;
  private static int postTotal = 0;
  
  private JTextField textFileArea, opusTitleField, opusAuthorField;
  private JFileChooser fileChooser;
  private JTextArea resultTextArea;
  private JButton browseButton, processButton;
  private JComboBox<String> fileTypeCombo;
  private JPanel load, textFilePanel, fileTypePanel, opusPanel, buttonPanel;
  private JLabel textFileLabel, fileTypeLabel, opusTitleLabel, opusAuthorLabel;
  private File file;
  private File fp;
  private String[] fileTypeList;
  private JSeparator sep;
  private int ordnum;
  private boolean valid = false;
  
  
  private final Border border = BorderFactory.createLineBorder(Color.BLACK);

  /**
   * PirexLoadTab constructs the load tab.
   * 
   * @param tabPane
   *          is the tab pane.
   */
  public PirexLoadTab(JTabbedPane tabPane)
  {
    summ = "";
    load = new JPanel();
    load.setLayout(new BoxLayout(load, BoxLayout.Y_AXIS));

    // creates the first row
    setTextFilePanel();
    // second row
    setFileTypePanel();
    // third row
    setOpusPanel();

    sep = new JSeparator(JSeparator.HORIZONTAL);
    sep.setForeground(Color.BLACK);
    load.add(Box.createRigidArea(new Dimension(FIFTEEN, FIFTEEN)));
    load.add(sep);

    // PROCESS BUTTON
    buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, FIFTEEN, ONE));
    processButton = new JButton(PRC);
    processButton.addActionListener(this);
    buttonPanel.add(processButton);
    load.add(Box.createRigidArea(new Dimension(0, FIFTEEN)));
    load.add(buttonPanel);

    // RESULT TEXT AREA
    resultTextArea = new JTextArea();
    resultTextArea.setBorder(border);
    load.add(Box.createRigidArea(new Dimension(FIFTEEN, FIFTEEN)));
    load.add(resultTextArea);

    tabPane.addTab("Load Documents", load);
  }

  /**
   * setTextFilePanel sets the text file panel.
   */
  private void setTextFilePanel()
  {
    textFilePanel = new JPanel();
    textFileLabel = new JLabel("Text File");
    textFileArea = new JTextField();
    textFileArea.setPreferredSize(new Dimension(SIXH, TWENTYFIVE));
    textFileArea.setEditable(false);
    textFileArea.setBackground(Color.WHITE);
    textFileArea.setBorder(border);
    browseButton = new JButton(BRW);
    browseButton.addActionListener(this);
    textFilePanel.setLayout(new FlowLayout(FlowLayout.LEFT, FIFTEEN, ONE));

    textFilePanel.add(textFileLabel);

    textFilePanel.add(textFileArea);

    textFilePanel.add(browseButton);
    load.add(Box.createRigidArea(new Dimension(FIFTEEN, FIFTEEN)));
    load.add(textFilePanel);
  }

  /**
   * setFileTypePanel sets the file type panel.
   */
  private void setFileTypePanel()
  {
    // instantiate string list for combobox list
    fileTypeList = new String[1];
    fileTypeList[0] = "Project Gutenberg File";
    // FILETYPE PANEL (2nd row)
    fileTypePanel = new JPanel();
    fileTypePanel.setLayout(new FlowLayout(FlowLayout.LEFT, FIFTEEN, ONE));
    fileTypeLabel = new JLabel("Text File Type:");

    // ComboBox initialization and customization
    fileTypeCombo = new JComboBox<String>(fileTypeList);
    fileTypeCombo.setEditable(false);
    fileTypeCombo.setPreferredSize(new Dimension(SIXHST, TWENTYFIVE));
    fileTypeCombo.setBackground(Color.WHITE);
    fileTypeCombo.setForeground(Color.BLACK);

    fileTypePanel.add(fileTypeLabel);
    fileTypePanel.add(fileTypeCombo);
    load.add(Box.createRigidArea(new Dimension(FIFTEEN, FIFTEEN)));
    load.add(fileTypePanel);
  }

  /**
   * setOpusPanel is the Opus panel.
   */
  private void setOpusPanel()
  {
    opusPanel = new JPanel();
    opusPanel.setLayout(new FlowLayout(FlowLayout.LEFT, FIFTEEN, ONE));

    opusTitleLabel = new JLabel("Title:");
    opusAuthorLabel = new JLabel("Author:");
    opusTitleField = new JTextField();
    opusTitleField.setPreferredSize(new Dimension(THREEHN, TWENTYFIVE));
    opusTitleField.setEditable(true);
    opusTitleField.setBackground(Color.WHITE);
    opusTitleField.setBorder(border);
    opusAuthorField = new JTextField();
    opusAuthorField.setPreferredSize(new Dimension(THREEHN, TWENTYFIVE));
    opusAuthorField.setEditable(true);
    opusAuthorField.setBackground(Color.WHITE);
    opusAuthorField.setBorder(border);

    opusPanel.add(opusTitleLabel);
    opusPanel.add(opusTitleField);
    opusPanel.add(opusAuthorLabel);
    opusPanel.add(opusAuthorField);

    load.add(Box.createRigidArea(new Dimension(FIFTEEN, FIFTEEN)));
    load.add(opusPanel);
  }

  /**
   * method for listening to action performed.
   * 
   * @param event
   *          event commands.
   */
  public void actionPerformed(ActionEvent event)
  {
    String command = event.getActionCommand();

    switch (command)
    {
      case BRW:
        if(fileChooser == null)
        {
          fileChooser = new JFileChooser();
        }
        
        fileChooser.showOpenDialog(null);
        file = fileChooser.getSelectedFile();
        //CHECK IF FILE IS AN OPUS FILE
        if(file != null)
        {
          textFileArea.setText(file.getAbsolutePath());
          String title = null;
          String author = null;
          
          title = SearchUtils.parseText(file, TLE);
          if(title != null)
          {
            opusTitleField.setText(title);
          }            
          
          author = SearchUtils.parseText(file, ATH);
          if(author != null)
          {
            opusAuthorField.setText(author);
          }
          
          ordnum = SearchUtils.parseOrd(file);
          
          if(ordnum == -1 && author == null && title == null)
          {
            valid = false;
          }
          else
          {
            valid = true;
          }
        }
        
        break;
      case PRC:
        Opus check = SearchUtils.getOpus(ordnum);
                 
        
        if(file == null || !valid || check != null)
        {
          JOptionPane.showMessageDialog(null, "Bad file!",
              null, JOptionPane.ERROR_MESSAGE);
          
          break;
        }
        resultTextArea.setText("");
        
        fp = DirUtils.store(file);
        SearchUtils.createOpus(opusTitleField.getText(), opusAuthorField.getText(), ordnum, fp);
        Opus opus = SearchUtils.getOpus(new Integer(ordnum));

        setResultText("Opus: ", file.getAbsolutePath());
        setResultText(TLE, opus.getTitle());
        setResultText(ATH, opus.getAuthor());
        setResultText("Opus size: ", SearchUtils.getNewDocumentsCount());
        setResultText("Opus number: ", SearchUtils.getOpusNumber());
        setResultText("New index terms: ", SearchUtils.getNewTerms());
        setResultText("New posting: ", SearchUtils.getNewPostings());
        setResultText("Total index terms: ", SearchUtils.getTotalTerms());
        setResultText("Total postings ", SearchUtils.getTotalPostings());
        if (summ.equals("") && SearchUtils.getDocMap().containsKey(SUM_MAGIC_NUM))
          summ = SearchUtils.getDocMap().get(SUM_MAGIC_NUM).get(SUM_MAGIC_NUM).getParagraph();
        summ += "Opus " + SearchUtils.getOpusNumber() + ":  " + opus.getAuthor() + "    "
            + opus.getTitle() + "   " + SearchUtils.getNewDocumentsCount() + "\n                "
            + file.getAbsolutePath() + '\n';
        indexTotal = SearchUtils.getTotalTerms();
        postTotal = SearchUtils.getTotalPostings();

        Documents tempDoc;
        HashMap<Integer, Documents> tempDocHash;
        HashMap<Integer, HashMap<Integer, Documents>> tempDocMap;
        List<Integer> termAndPostings;
        
        termAndPostings = new ArrayList<Integer>();
        termAndPostings.add(SearchUtils.getTotalTerms());
        termAndPostings.add(SearchUtils.getTotalPostings());
        tempDocHash = new HashMap<Integer, Documents>();
        tempDoc = new Documents(summ, termAndPostings, null, SearchUtils.getOpusNumber());
        tempDocMap = SearchUtils.getDocMap();
        tempDocHash.put(SUM_MAGIC_NUM, tempDoc);
        tempDocMap.put(SUM_MAGIC_NUM, tempDocHash);
        SearchUtils.setDocMap(tempDocMap);
        break;
      default:
        break;

    }
  }

  /**
   * Method returns summ string.
   * 
   * @return String of summ
   */
  public static String getSummString()
  {    
    return summ;
  }

  /**
   * Method returns total indexes.
   * 
   * @return int of index total.
   */
  public static int getTotalIndex()
  {
    if (SearchUtils.getDocMap().containsKey(SUM_MAGIC_NUM))
      indexTotal = SearchUtils.getDocMap().get(SUM_MAGIC_NUM).get(SUM_MAGIC_NUM)
        .getWordCounts().get(0);
    return indexTotal;
  }
  
  /**
   * setTotal sets the total to 0.
   */
  public void setClear()
  {
    indexTotal = 0;
    postTotal = 0;
    summ = "";
  }

  /**
   * Method returns total postings.
   * 
   * @return int of posting total.
   */
  public static int getTotalPosting()
  {
    if (SearchUtils.getDocMap().containsKey(SUM_MAGIC_NUM))
      postTotal = SearchUtils.getDocMap().get(SUM_MAGIC_NUM).get(SUM_MAGIC_NUM)
        .getWordCounts().get(ONE);
    return postTotal;
  }

  /**
   * setResultText sets text.
   * 
   * @param str
   *          ...
   * @param result
   *          ...
   */
  public void setResultText(String str, String result)
  {
    resultTextArea.append(str + SINGLESPACE + result + '\n');
  }

  /**
   * setResultText overrides previous method with an int.
   * 
   * @param str
   *          ...
   * @param result
   *          ...
   */
  public void setResultText(String str, int result)
  {
    resultTextArea.append(str + SINGLESPACE + result + '\n');
  }
  
  /**
   * Method to show that load tab is selected currently.
   */
  public void selectedLoad()
  {
    System.out.println("Load tab selected");
  }
  
  /**
   * Changes path of file chooser.
   */
  public void setFileChooser()
  {
    JFileChooser newPath = new JFileChooser();
    newPath.setDialogTitle("Please select a new path.");
    newPath.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    
    newPath.showOpenDialog(null);
    
    if(newPath != null)
    {
      fileChooser = new JFileChooser(newPath.getSelectedFile());
    }
    
  }
}
