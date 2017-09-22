package pirex04.src.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;

import pirex04.src.SearchEngine;
import pirex04.src.SearchUtils;
import pirex04.src.Documents;

/**
 * Class that constructs the search tab layout and functionality.
 * 
 * @author Alan
 *
 */
public class PirexSearchTab implements ActionListener, MouseListener
{
  private static final int FIFTEEN = 15; 
  private static final int ONE = 1;
  private static final int THREEH = 300;
  private static final int TEN = 10;
  private static final int TWENTYFIVE = 25;
  private static final int FIVE = 5;
  private static final int SIXH = 600;
  private static final String SPACE = "   ";
  private static final String SEARCHT = "Search";
  private static final String CLEANT = "Clear";
  private static final String DOC_RETRIEVED = "                 Documents retrieved: 0";
  private JTextArea textDisplayArea;
  private JButton clear;
  private JScrollPane scroll, resultsScrollPane;
  private JPanel search, query, resultsPanel, docCountPanel;
  private JLabel queryText;
  private JLabel documentCountLabel;
  private JTextField searchBox;
  private String[] formattedQuery;
  private List<Documents> documents;
  private String results, searchTermResults;
  private int exportCounter;
  private int documentCounter;
 
  private final Border border = BorderFactory.createLineBorder(Color.BLACK);

  /**
   * Constructor that constructs the layout of search tab and adds it to the main tab window.
   * 
   * @param tabPane
   *          main tab window.
   */
  public PirexSearchTab(JTabbedPane tabPane)
  {
    results = "";
    searchTermResults = "";
    search = new JPanel();
    search.setLayout(new BoxLayout(search, BoxLayout.Y_AXIS));

    // QUERY Panel
    query = new JPanel();
    query.setLayout(new FlowLayout(FlowLayout.LEFT, FIFTEEN, ONE));

    queryText = new JLabel("Query:");
    searchBox = new JTextField();
    searchBox.setPreferredSize(new Dimension(SIXH, TWENTYFIVE));
    searchBox.setBorder(border);
    searchBox.addActionListener(this);
    searchBox.setActionCommand(SEARCHT);

    clear = new JButton(CLEANT);
    query.add(queryText);

    query.add(searchBox);

    query.add(clear);
    clear.addActionListener(this);
    
    docCountPanel = new JPanel();
    docCountPanel.setLayout(new FlowLayout(FlowLayout.LEFT, FIFTEEN, ONE));
    documentCountLabel = new JLabel(DOC_RETRIEVED);
    docCountPanel.add(documentCountLabel);
    
    
    search.add(Box.createRigidArea(new Dimension(FIFTEEN, FIFTEEN)));
    search.add(query);
    //search.add(Box.createRigidArea(new Dimension(FIFTEEN, FIFTEEN)));
    search.add(docCountPanel);
    search.add(Box.createRigidArea(new Dimension(FIFTEEN, FIFTEEN)));

    // Text search area
    // UPDATE: switched textarea for panel which will fit inside a scrollpane.
    resultsPanel = new JPanel();
    resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
    
    resultsScrollPane = new JScrollPane(resultsPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    resultsPanel.setBackground(Color.WHITE);
    resultsScrollPane.setPreferredSize(new Dimension(THREEH, THREEH));
    // search.add(searchResults);
    search.add(resultsScrollPane);
    search.add(Box.createRigidArea(new Dimension(FIFTEEN, FIFTEEN)));

    // Text document test display area
    textDisplayArea = new JTextArea(TEN, TEN);
    textDisplayArea.setLineWrap(true);
    textDisplayArea.setWrapStyleWord(true);
    textDisplayArea.setBorder(border);

    textDisplayArea.setEditable(false);
    scroll = new JScrollPane(textDisplayArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    search.add(scroll);
    // ADD SEARCH TO TABS
    tabPane.addTab("Search for Documents", search);
  }

  /**
   * method that takes in multiple string inputs of the opus fields and adds the following to a
   * panel to be displayed and clicked on.
   * 
   * @param authr
   *          opus author.
   * @param t
   *          opus title.
   * @param doc
   *          opus doc.
   * @param shortForm
   *          opus shortForm.
   * @param longForm
   *          opus longForm.
   */
  public void addPanel(String authr, String t, int doc, String shortForm, String longForm)
  {
    JLabel opusDetail;
    String opusDetailResults;
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, FIFTEEN, ONE));
    opusDetail = new JLabel(authr + SPACE + t + SPACE + doc + SPACE + shortForm);
    opusDetailResults = String.format("%d.\r\n Author: %s\r\n Title: %s\r\n "
                        + "Document #: %s\r\n Short Form: %s\r\n\r\n", 
                        exportCounter, authr, t, doc, shortForm);
    results += opusDetailResults;
    exportCounter++;
    documentCounter++;
    panel.add(opusDetail);
    panel.addMouseListener((new java.awt.event.MouseAdapter()
    {
      public void mouseEntered(java.awt.event.MouseEvent evt)
      {
        panel.setBackground(Color.GRAY);
      }

      public void mouseExited(java.awt.event.MouseEvent evt)
      {
        panel.setBackground(UIManager.getColor("Panel.background"));
      }

      public void mouseClicked(java.awt.event.MouseEvent evt)
      {
        textDisplayArea.setText(longForm);
        Highlighter highlighter = textDisplayArea.getHighlighter();
        HighlightPainter painter = 
            new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW);
        int p0;
        int p1;
        for(int i = 0; i < formattedQuery.length;i++)
        {
          String term = formattedQuery[i];
          p0 = longForm.toLowerCase().indexOf(term);
          p1 = p0 + term.length();
          
          while(p0 >= 0)
          {               
            try
            {
              highlighter.addHighlight(p0, p1, painter);
            }
            catch (BadLocationException e)
            {
              e.printStackTrace();
            }
            p0 = longForm.toLowerCase().indexOf(term, p0+term.length());
            p1 = p0 + term.length();
          }
          
        }
        
      }
    }));
    panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, panel.getMinimumSize().height + FIVE));
    resultsPanel.add(panel);
    resultsPanel.revalidate();
    resultsPanel.repaint();
  }
  
  /**
   * getSearchQueries retruns the contents of the search box.
   * 
   * @return the contents of the search box
   */
  public String getSearchQueries()
  {
    return searchBox.getText();
  }
  
  /**
   * setSearchBox  sets the contents of the search box.
   * @param string to set
   */
  public void setSearchBox(String string)
  {
    searchBox.setText(string);
  }
  
  /**
   * sets the two variables to empty.
   */
  public void setSearchTermResultsToEmpty()
  {
    searchTermResults = "";
    results = "";
  }
  /**
   * get the search results.
   * @return search results
   */
  public String getResults()
  {
    return searchTermResults + results;
  }
  /**
   * Method to display error incase user enters search terms passed the 100 limit.
   */
  public static void invalidSearchLimit()
  {
    JOptionPane.showMessageDialog(null, "Search terms exceeded over 100",
        "Invalid Search Term Limit", JOptionPane.INFORMATION_MESSAGE);
  }

  /**
   * Method that clears the results panel.
   */
  public void setTextDisplayAreaToEmpty()
  {
    textDisplayArea.setText("");
  }
  
  /**
   * revalidates the panel and repaints.
   */
  public void validatePanel()
  {
    resultsPanel.validate();
    resultsPanel.removeAll();
    resultsPanel.revalidate();
    resultsPanel.repaint();
    
  }
  
  /**
   * Method for listening for action performed and processing those events.
   * 
   * @param event
   *          event commands.
   */
  public void actionPerformed(ActionEvent event)
  {
    String command = event.getActionCommand();

    switch (command)
    {
      case CLEANT:
        textDisplayArea.setText("");
        documentCountLabel.setText(DOC_RETRIEVED);
        // searchResults.setText("");
        searchBox.setText("");
        setSearchTermResultsToEmpty();
        resultsPanel.validate();
        resultsPanel.removeAll();
        resultsPanel.revalidate();
        resultsPanel.repaint();
        
        break;

      case SEARCHT:
        textDisplayArea.setText("");
        searchTermResults = "Search Terms: " + searchBox.getText() + "\r\n\r\n";
        exportCounter = 1;
        documentCounter = 0;
        if (!"".equals(searchBox.getText()))
        {
          formattedQuery = SearchUtils.formatQuery(searchBox.getText());

          if (formattedQuery == null)
          {
            invalidSearchLimit();
            break;
          }
          else
            documents = SearchEngine.search(formattedQuery);
        }
        
        resultsPanel.validate();
        resultsPanel.removeAll();
        resultsPanel.revalidate();
        resultsPanel.repaint();
        
        boolean notTermFlag = false;
        for (int i = 0; i < documents.size(); i++)
        {
          if (i != 0)
            if (documents.get(i).getOpus().getOrdNumber() 
                == documents.get(i - ONE).getOpus().getOrdNumber())
              if (documents.get(i).getDocumentNumber() 
                  == documents.get(i - ONE).getDocumentNumber())
                continue;
          
          for (int k = 0; k < SearchUtils.getNotTerms().size(); k++)
            if (documents.get(i).getParagraph().toLowerCase()
                .contains(SearchUtils.getNotTerms().get(k).toLowerCase()))
              notTermFlag = true;
          
          if (!notTermFlag)
            addPanel(documents.get(i).getOpus().getAuthor(), documents.get(i).getOpus().getTitle(),
                documents.get(i).getDocumentNumber(), documents.get(i).getShortParagraph(),
                documents.get(i).getParagraph());
          
          notTermFlag = false;
        }
        documentCountLabel.setText(String
              .format("                 Documents retrieved: %d", documentCounter));
        break;
      default:
        break;

    }
  }

  /**
   * Sets the documents retrieved label to 0.
   */
  public void setDocumentCounterToZero()
  {
    documentCountLabel.setText(DOC_RETRIEVED);
  }
  /**
   * method to determine which state tab is in currently.
   */
  public void selectedSearch()
  {
    System.out.println("Search tab selected");
  }
  
  @Override
  public void mousePressed(MouseEvent e)
  {
  }

  @Override
  public void mouseReleased(MouseEvent e)
  {
  }

  @Override
  public void mouseClicked(MouseEvent arg0)
  {
  }

  @Override
  public void mouseEntered(MouseEvent arg0)
  {
  }

  @Override
  public void mouseExited(MouseEvent arg0)
  {
  }

}
