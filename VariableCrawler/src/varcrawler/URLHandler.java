/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package varcrawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jared
 */
public class URLHandler implements Runnable {

   public URLHandler(crawlerEngine engine, String fileName) {
      this.engine = engine;
      this.fileName = fileName;

      System.out.println("Started new Handler");

   }

   @Override
   public void run() {
      do {
      int numSites = 0;
      try {
         File file = new File(getClass().getResource(fileName).toURI());
         BufferedReader reader = new BufferedReader(new FileReader(file));
         String url = reader.readLine();

         while (url != null && engine.add(url)) {
            System.out.println("\n\n\nAdded site: " + url
                               + " to the queue\n\n\n");
            url = reader.readLine();
            ++numSites;
         }
      } catch (FileNotFoundException ex) {
         Logger.getLogger(crawlerEngine.class.getName()).log(Level.SEVERE, null,
                                                             ex);
      } catch (IOException | URISyntaxException ex) {
         Logger.getLogger(crawlerEngine.class.getName()).log(Level.SEVERE, null,
                                                             ex);
      }
//
//         System.out.println("\nThere are " + numSites
//                            + " sites in websites.txt\n");
//      }
//    engine.shutdown();
      } while (engine.keepHandlerAlive());
   }

   private final crawlerEngine engine;

   private final String fileName;
}
