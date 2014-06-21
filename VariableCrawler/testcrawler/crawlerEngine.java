/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testcrawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jared
 */
public class crawlerEngine {

  public List getUrls() {
    List<String> urls = new ArrayList<>();

    try {
      File file = new File(getClass().getResource("websites.txt").toURI());
      BufferedReader reader = new BufferedReader(new FileReader(file));
      String url = reader.readLine();
      
      while (url != null) {
        urls.add(url);
        url = reader.readLine();
      }
    } catch (FileNotFoundException ex) {
      Logger.getLogger(crawlerEngine.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
      Logger.getLogger(crawlerEngine.class.getName()).log(Level.SEVERE, null, ex);
    } catch (URISyntaxException ex) {
      Logger.getLogger(crawlerEngine.class.getName()).log(Level.SEVERE, null, ex);
    }

    return urls;
  }

  /**
   *
   * @param args
   */
  public static void main(String[] args) {

    crawlerEngine engine = new crawlerEngine();

    ThreadGroup group = new ThreadGroup("crawlers");

    List<String> urls = engine.getUrls();

    
    for (String url : urls) {
      
//      if (group.activeCount() >= 7) {
//        while(group.activeCount() >= 7);
//      }
      TestCrawler thread = new TestCrawler(group, "thread", url);
      thread.start();
    }
  }
}
