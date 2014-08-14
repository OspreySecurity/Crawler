    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package standards;

import java.util.List;
import org.jsoup.nodes.Document;

/**
 *
 * @author Jared
 */
public class DomainHttps extends Standard {

   /**
    * calls the Standard class constructor
    *
    * @param doc
    */
   public DomainHttps(Document doc) {
      super(doc);
   }

   /**
    * Tests if the domain is https
    *
    * @return String result
    */
   @Override
   public List<String> test() {
      result.add("Domain is https");
      result.add(Boolean.toString(isHttps()));
      return result;
   }

   /**
    * Check is the base uri is https or not
    *
    * @return
    */
   public Boolean isHttps() {
      return DOM.baseUri().contains("https");
   }

}
