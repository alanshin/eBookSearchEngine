package pirex04.src.gui;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;

import pirex04.src.SearchUtils;

/**
 * This class constructs the summarize tab.
 * 
 * @author Alan
 *
 */
public class PirexSummarizeTab
{
  private static final int SUM_NUM = -25623;
  private JTextArea summaryTextArea;
  private JScrollPane summaryScrollPane;
  private final Border border = BorderFactory.createLineBorder(Color.BLACK);

  /**
   * Constructor that creates the tab layout and adds itself to the tab window.
   * 
   * @param tabPane
   *          parameter for the main tab window
   */
  public PirexSummarizeTab(JTabbedPane tabPane)
  {
    summaryTextArea = new JTextArea();
    summaryTextArea.setBorder(border);
    summaryTextArea.setEditable(false);
    summaryScrollPane = new JScrollPane(summaryTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    tabPane.addTab("Summarize Documents", summaryScrollPane);
  }

  /**
   * Appends the String argument to the JTextArea.
   * 
   * @param str
   *          the string to append to JTextArea.
   */
  public void setSummaryTextArea(String str)
  {
    this.summaryTextArea.append(str);
    this.summaryTextArea.append("\n\n\nIndex terms: " + PirexLoadTab.getTotalIndex() + '\n');
    this.summaryTextArea.append("Posting:      " + PirexLoadTab.getTotalPosting());
  }

  /**
   * Method that first erases the JTextArea and then appends Strings to it.
   */
  public void updateSummaryTab()
  {
    summaryTextArea.setText("");
    if (SearchUtils.getDocMap().containsKey(SUM_NUM) 
        && PirexLoadTab.getSummString().equals(""))
    {
      setSummaryTextArea(SearchUtils.getDocMap().get(SUM_NUM).get(SUM_NUM).getParagraph());
    }
    else
    {
      setSummaryTextArea(PirexLoadTab.getSummString());
    }
    summaryScrollPane.revalidate();
    summaryScrollPane.repaint();
  }
  
  /**
   * clearSummary clears the text box.
   */
  public void clearSummary()
  {
    summaryTextArea.setText("");
  }
}
