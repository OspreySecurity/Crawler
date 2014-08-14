/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package standards;

import java.util.List;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Jared
 */
public class CheckPassUrl extends Standard {

  public CheckPassUrl(Document doc) {
    super(doc);
  }

  @Override
  public List<String> test() {
    
    result.add("Total forms with Password as GET");
    result.add(Integer.toString(passwordsRevealed()));
    return result;
  }
  
  /**
   * See if passwords are revealed in a form
   * @return 
   */
  public int passwordsRevealed(){
    // Get all forms
    Elements forms = this.DOM.getElementsByTag("form");
    int sensative = 0;
    for (Element form : forms) {
      if (form.attr("method").toUpperCase().equals("GET")) {
        Elements children = form.getAllElements();
        for (Element child : children) {
          if (child.attr("type").toLowerCase().equals("password")) {
            sensative++;
          } else if (child.attr("name").toLowerCase().contains("pass")) {
            sensative++;
          } else if (child.attr("id").toLowerCase().contains("pass")) {
            sensative++;
          }
        }
      }
    }
      return sensative;
  }
}
