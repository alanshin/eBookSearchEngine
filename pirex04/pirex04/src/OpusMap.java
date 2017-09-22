package pirex04.src;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OpusMap is a class to hold the opus objects for serialization.
 * @author okadacs, johns2nc
 *
 */
public class OpusMap implements Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private Map<Integer, Opus> opusMap;
  private HashMap<String, HashMap<Integer, List<Integer>>> indexMap;
  private HashMap<Integer, HashMap<Integer, Documents>> docMap;
  
  /**
   * OpusMap constructor.
   * @param opusMap contains the opus objects.
   * @param indexMap to serialize
   * @param docMap to serialize
   */
  public OpusMap(Map<Integer, Opus> opusMap, 
      HashMap<String, HashMap<Integer, List<Integer>>> indexMap,
      HashMap<Integer, HashMap<Integer, Documents>> docMap)
  {
    this.opusMap = opusMap;
    this.indexMap = indexMap;
    this.docMap = docMap;
  }
  
  /**
   * getMap returns the Hashmap.
   * @return hashmap
   */
  public Map<Integer, Opus> getMap()
  {
    return opusMap;
  }
  
  /**
   * returns index map.
   * @return index map.
   */
  public HashMap<String, HashMap<Integer, List<Integer>>> getIndexMap()
  {
    return indexMap;
  }
  
  
  /**
   * Returns document map.
   * @return document map
   */
  public HashMap<Integer, HashMap<Integer, Documents>> getDocMap()
  {
    return docMap;
  }
}
