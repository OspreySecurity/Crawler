/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PiCrawler;

import java.util.*;

/**
 *
 * @author Jared, Chase, Matt
 */
public class Report {

   /**
    *
    * @param map
    */
   public Report(Map<String, String> map) {
      report = map;
   }

   Report() {
      report = new HashMap<String, String>();
   }

   public void add(String key, String value) {
      report.put(key, value);
   }

   public void add(List<String> list) {

      Iterator<String> iter = list.iterator();

      while (iter.hasNext())
         report.put(iter.next(), iter.next());
   }

   public void add(Map<String, String> map) {
      report.putAll(map);
   }

   /**
    * makes the report and prints it out for now. Later it will make changes to
    * the database
    */
   public synchronized void printReport() {
      String header = "Domain Name";
      String page = report.get(header);
      report.remove(header);
      page = header + " : " + page;

      Set<String> set = report.keySet();

      System.out.println("=======================================");

      System.out.println(page);

      for (String str : set) {
         System.out.print(str + " : ");
         System.out.println(report.get(str));
      }

      System.out.println("=======================================");
   }

   /*
    * this merely clears out the list, it doesn't dealocate it meaning if there
    * is ever a huge report the memory will never be freed. On the other hand,
    * it is faster because it doesn't have to reallocate memory
    */
   public void clear() {
      report.clear();
   }

   private final Map<String, String> report;

}
