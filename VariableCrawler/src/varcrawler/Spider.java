/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package varcrawler;

import standards.LoginForm;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import standards.*;

/**
 *
 * @author Jared, Chase, Matt
 */
public class Spider implements Runnable {

   /**
    */
   @Override
   public void run() {
      while (domainName != null) {
         try {
            
            assert(domainName != null);
            
            URL url = new URL(domainName);
            url.openConnection();
            
            domainName = url.getHost();
            
            report.add("Default port", Integer.toString(url.getDefaultPort()));
            report.add("Host Name", url.getHost());
            report.add("Protocol", url.getProtocol());
            
            
            DomainHttps https = new DomainHttps(getDom());
            LoginForm login = new LoginForm(getDom());
            CheckHttps checkHttps = new CheckHttps(getDom());
            CheckPassUrl checkPassUrl = new CheckPassUrl(getDom());
            addSites adder = new addSites(getDom());
            HeaderParser parser = new HeaderParser(getDom());
            report.add("Webpage Crawled", getUrl());
            report.add("Download Time", getDownloadTime() + " seconds");

            // eventually we will make these dynamically loaded, then this will be epic
            report.add(https.test());
            report.add(checkHttps.test());
            report.add(checkPassUrl.test());
            if (login.hasLoginForm())
               report.add(login.test());
            
            report.add(parser.test());

         // this will handle the report, for now it prints it out.
            // Later it will add it to the database
            report.printReport(); // println is sychronious meaning it wont get interupted by other Threads
            
//            engine.addReport(report);

            report.clear();

            domainName = engine.getNext();

            adder.write();
         } catch (MalformedURLException ex) {
            System.out.println("Heres the bad url : " + domainName);
            Logger.getLogger(Spider.class.getName()).log(Level.SEVERE, null, ex);
//            engine.addSpider();
         } catch (IOException ex) {
            Logger.getLogger(Spider.class.getName()).log(Level.SEVERE, null, ex);
//            engine.addSpider();
         } 
      }
   }

   Spider(crawlerEngine engine) {
//    super(group, thread);
      report = new Report();

      try {

         this.engine = engine;
         domainName = engine.getNext();

         while (domainName == null) {
            System.out.println("null url = " + domainName);
//            this.wait(1000);
//            domainName = engine.getNext();
            domainName = "http://www.imprintedstudios.com";
         }

         pre = new Date();
         DOM = Jsoup.connect(domainName).timeout(10 * 1000).get();
         post = new Date();
         setDownloadTime(pre, post);

      } catch (IOException ex) {
         System.out.println("heres the bad url: " + domainName + "\n");
         Logger.getLogger(Spider.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   public final void setDownloadTime(Date pre, Date post) {
      this.downloadTime = (post.getTime() - pre.getTime()) / 1000.0;
   }

   public double getDownloadTime() {
      return downloadTime;
   }

   public String getUrl() {
      return domainName;
   }

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

   /**
    * The report on the website
    */
   private final Report report;

   /**
    * The start time
    */
   private Date pre;

   /**
    * The end time
    */
   private Date post;

   /**
    * The start URL
    */
   private String domainName;

   /**
    *
    */
   private crawlerEngine engine;
}
