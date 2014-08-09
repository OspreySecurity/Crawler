/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PiCrawler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author Jared, Chase, Matt
 */
public class Spider {

   Spider(crawlerEngine engine) {
   }

   /**
    * @param domainName
    * @return
    */
   public Boolean crawl(String domainName) {
      assert (domainName != null);

      Report report = new Report();

      try {
         Date pre = new Date();
         DOM = Jsoup.connect(domainName).timeout(10 * 1000).get();
         Date post = new Date();
         
         URL url = new URL(domainName);
         url.openConnection();

         domainName = url.getHost();

         report.add("Domain Name", domainName);
         report.add("Download Time", Double.toString(getDownloadTime(pre, post)));
         report.add("Default port", Integer.toString(url.getDefaultPort()));
         report.add("Host Name", url.getHost());
         report.add("Protocol", url.getProtocol());
         
         

      } catch (MalformedURLException ex) {
         System.out.println("Heres the bad url : " + domainName);
         Logger.getLogger(Spider.class.getName()).log(Level.SEVERE, null, ex);
//            engine.addSpider();
      } catch (IOException ex) {
         Logger.getLogger(Spider.class.getName()).log(Level.SEVERE, null, ex);
//            engine.addSpider();
      }
      return null;
   }

   /**
    * @param pre
    * @param post
    */
   private double getDownloadTime(Date pre, Date post) {
      return (post.getTime() - pre.getTime()) / 1000.0;
   }

   /**
    *
    * @return
    */
   public double getDownloadTime() {
      return downloadTime;
   }

   /**
    *
    * @return
    */
   public Document getDom() {
      return DOM;
   }

   /**
    * The Site to be crawled
    */
   protected Document DOM;

   /**
    * The time it takes to crawl
    */
   protected double downloadTime;
}
