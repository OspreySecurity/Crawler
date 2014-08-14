/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package standards;

import java.util.ArrayList;
import java.util.List;
import org.jsoup.nodes.Document;

/**
 *
 * @author Jared
 */
public abstract class Standard {
  
  public Standard(Document doc) {
    DOM = doc;
    result = new ArrayList();
  }
  
  public abstract List<String> test();
  
  protected Document DOM;
  protected List<String> result;
}
