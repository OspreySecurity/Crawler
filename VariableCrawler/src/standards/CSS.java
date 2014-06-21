/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package standards;

import java.util.List;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 *
 * @author Jared
 */
public class CSS extends Standard {

  public CSS(Document doc) {
    super(doc);
  }

  public Elements getStylesheets() {
    return this.stylesheets;
  }

  public int countStylesheets() {
    return this.stylesheets.size();
  }

  private Elements getStyleTags() {
    return this.DOM.getElementsByTag("style");
  }

  @Override
    public List<String> test() {
    return null;
  }

  protected Elements stylesheets = getStyleTags();
}
