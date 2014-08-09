/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PiCrawler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Jared
 */
public class addSites {

   public addSites(Document doc) {

   }

   public void write(Document doc) {
      DOM = doc;
      Set<String> newSites = new HashSet<String>();
      String[] parts;
      String linkHref;
      String middle;

      Elements href = this.DOM.getElementsByAttribute("href");

      for (Element link : href) {
         linkHref = link.attr("href");
         if (linkHref.contains("http")) {
            parts = linkHref.split("/");
            middle = "//";
            if (!linkHref.contains("www"))
               middle += "www.";
            linkHref = parts[0] + middle + parts[2]; // only grab the domain name
            newSites.add(linkHref);

         }
      }

      BufferedWriter writer = null;

      try {
         File file = new File(getClass().getResource("websites.txt").toURI());
         writer = new BufferedWriter(new FileWriter(file, true));

//         System.out.println("\n");
         //ArrayList<String> parts = new ArrayList<String>();
         for (String site : newSites) {
//        System.out.println("Writeing " + site + " to file: " + getClass().
//                getResource("websites.txt").toURI());
            if (site.contains("imprintedstudios.com"))
               continue;

            if (site.contains("http")) {
               writer.write(site);
               writer.newLine();
//               System.out.println("Wrote : " + site);
            }
         }
      } catch (IOException | URISyntaxException ex) {
         Logger.getLogger(addSites.class.getName()).log(Level.SEVERE, null, ex);
      } finally {
         try {
            writer.close();
         } catch (IOException ex) {
            Logger.getLogger(addSites.class.getName()).log(Level.SEVERE, null,
                                                           ex);
         }
      }
   }

   protected Document DOM;
}
