/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package standards;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Crawl the Login Form and perform Security Checks
 *
 * @author Chase Willden
 */
public class LoginForm extends Standard {

  public LoginForm(Document DOM) {
    super(DOM);
  }

  /**
   * Check to see if there's a login form
   *
   * @return
   */
  public boolean hasLoginForm() {
    String[] contains = {"login", "signup", "enter"};
    Boolean hasForm = false;
    String uri = this.DOM.baseUri();
    if (isInList(contains, uri.toLowerCase())) {
      return true;
    }
    Elements forms = this.DOM.getElementsByTag("form");
    for (Element form : forms) {
      String action = form.attr("action");
      String classname = form.attr("class");
      if (isInList(contains, action) || isInList(contains, classname)) {
        return true;
      }
    }
    return hasForm;
  }

  /**
   * Returns true if there is two step validation
   *
   * @return
   */
  public boolean twoStepValidation() {
    boolean password = false;
    boolean username = false;
    Elements forms = this.DOM.getElementsByTag("form");
    for (Element form : forms){
        Elements inputs = form.getElementsByTag("input");
        for (Element input : inputs){
            String type = input.attr("type");
            if (type.toLowerCase().contains("password")){
                password = true;
            }
            if (type.toLowerCase().contains("text")){
                username = true;
            }
        }
    }
    return password && username;
  }

  /**
   * Main function to check encryption of passwords
   *
   * @return
   * @throws java.io.IOException
   */
  public boolean passwordsEncrypted() throws IOException {
    return !hasAction() && isInJavaScript();
  }

  /**
   * Test username
   *
   * @return
   * @throws IOException
   */
  public boolean usernameEncrypted() throws IOException {
    return !hasAction() && isInJavaScript();
  }

  /**
   * Checks to see if forms action attribute has text
   *
   * @return
   */
  public boolean hasAction() {
    Elements forms = this.DOM.getElementsByTag("form");
    for (Element form : forms) {
      if (form.hasAttr("action") && form.attr("action").isEmpty()) {
        return false;
      } else {
        return form.hasAttr("action");
      }
    }
    return false;
  }

  /**
   * Checks for "MD5", "SHA", "BLOWFISH", "RIPEMD", "HMAC" in the JavaScript
   * file
   *
   * @return
   * @throws java.io.IOException
   */
  public boolean isInJavaScript() throws IOException {
    Elements jss = this.DOM.getElementsByTag("script");
    String[] contains = {"MD5", "SHA", "BLOWFISH", "RIPEMD", "HMAC"};
    for (Element js : jss) {
      if (!js.html().isEmpty()) {
        String innerJS = js.html();
        return isInList(contains, innerJS);
      } else {
        Document downloadedJs = Jsoup.connect(js.attr("src")).get();
        String djs = downloadedJs.toString();
        return isInList(contains, djs);
      }
    }
    return false;
  }

  public boolean isInList(String[] contains, String html) {
    for (String contain : contains) {
      if (html.contains(contain.toLowerCase())) {
        return true;
      }
    }
    return false;
  }
  
  /**
   * Checks if the login form has OAuth capabilities
   * @return 
   */
  public boolean hasOauth(){
      String[] contains = {"google", "facebook", "github"};
      Elements hypers = this.DOM.getElementsByTag("a");
      for (Element a : hypers){
          String href = a.attr("href");
          String name = a.attr("name");
          String id = a.attr("id");
          String classes = a.attr("class");
          
          for (String contain : contains){
              if (href.toLowerCase().contains(contain) ||
                  name.toLowerCase().contains(contain) ||
                  id.toLowerCase().contains(contain) ||
                  classes.toLowerCase().contains(contain)){
                  return true;
              }
          }
      }
      return false;
  }
  
  /**
   * Returns the report
   * @return
   */
  @Override
  public List<String> test() {
    try {
      if (!hasLoginForm()) {
        return null;
      }

      result.add("Contains login form");
      result.add(Boolean.toString(hasLoginForm()));
      result.add("Has 2 step validation");
      result.add(Boolean.toString(twoStepValidation()));
      result.add("Passwords are encrypted");
      result.add(Boolean.toString(passwordsEncrypted()));
      result.add("Username / Email are encrypted");
      result.add(Boolean.toString(usernameEncrypted()));
      result.add("Includes OAuth");
      result.add(Boolean.toString(hasOauth()));

      return result;
    } catch (IOException ex) {
      Logger.getLogger(LoginForm.class.getName()).log(Level.SEVERE, null, ex);
    }

    return null;
  }
}
