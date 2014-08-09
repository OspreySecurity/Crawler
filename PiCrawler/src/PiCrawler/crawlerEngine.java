/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PiCrawler;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Jared, Chase, Matt
 */
public class crawlerEngine {

   public crawlerEngine() {
      
//      adder = new addSites();
      spider = new Spider(this);
      handler = new URLHandler(10);
      
      maxReportsSize = 5;
      totalReports = 0;
      
      reports = new HashSet();
   }

   /**
    * The driver of PiCrawler as long as its running, the crawler will crawl
    */
   public void run() {

      String site;
      Boolean crawled = false;
      
      //fill the handler with sites
      handler.fill();
      
      //It would be nice if we could stop this somehow
      while(true) {
         site = handler.getNext();
         if(site == null) {
            handler.fill();
            continue;
         }
         
         crawled = spider.crawl(site);
         reports.add(spider.getReport());
         
         if (reports.size() >= maxReportsSize) {
            //Write to the database
            
            
            //using reports.size() would be more accurate, but this is faster
            totalReports += maxReportsSize;
            
            //clear out all the reports to start over
            reports.clear();
         }
         
         
         
         
         
         
      }
   }
   
   /*
    * Gets the next website to be crawled
    */
   public synchronized String getNext() {

      return null;
   }

   /**
    *
    * @param args
    */
   public static void main(String[] args) {
      crawlerEngine engine = new crawlerEngine();
      System.out.println("started the crawler");
      engine.run();
   }

//   private final addSites adder;
   private final Spider spider;
   private final URLHandler handler;
   
   private final int maxReportsSize;
   private int totalReports;
   
//  private crawlerEngine engine;
   private final Set<Report> reports;
}
