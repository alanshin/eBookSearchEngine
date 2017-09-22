package pirex04.src;
import java.io.File;
import java.io.Serializable;

/**
 * Class that contains Opus information.
 * @author team 04 - johns2nc, okadacs
 *
 */
public class Opus implements Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private String title;
  private String author;
  private int ordNumber;
  private File file;

  
  /**
   * Explicit value constructor.
   * 
   * @param title of the book
   * @param author of the book
   * @param ordNumber of the book
   * @param file object containing specific file
   */
  public Opus(String title, String author, int ordNumber, File file)
  {
    setTitle(title);
    setAuthor(author);   
    this.ordNumber = ordNumber;
    
    if(file != null && file.exists())
      this.file = file;
    else
      this.file = null;
        
  }
  
  /**
   * Default constructor.
   */
  public Opus()
  {
    setTitle(null);
    setAuthor(null);
    file = null;
    
    ordNumber = -1;
  }
  
  /**
   * get method to return title.
   * @return title of the book
   */
  public String getTitle()
  {
    return title;
  }
  
  /**
   * get method to return author.
   * @return author of the book
   */
  public String getAuthor()
  {
    return author;
  }
  
  /**
   * get method to return file object.
   * @return file object associated with opus
   */
  public String getFilePath()
  {
    return file.getAbsolutePath();
  }
  
  /**
   * get method to return ordNumber of the book.
   * @return ordNumber of book.
   */
  public int getOrdNumber()
  {
    return ordNumber;
  }

  /**
   * Set method for title, sets title to NO TITLE if invalid.
   * @param newTitle to set to the title attribute
   */
  public void setTitle(String newTitle)
  {
    if(newTitle != null)
      title = newTitle;
    else
      title = "NO TITLE";
  }
  
  /**
   * set method for author, sets author to NO AUTHOR if invalid.
   * @param newAuthor to set to the author attribute
   */
  public void setAuthor(String newAuthor)
  {
    if(newAuthor != null)
      author = newAuthor;
    else
      author = "NO AUTHOR";
  }
  
  /**
   * toString method return a string of attributes.
   * @return string representation of autho and title.
   */
  public String toString()
  {
    return String.format("Author: " + getAuthor() + " Title:" + getTitle());
  }
}
