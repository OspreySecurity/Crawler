/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package PiCrawler;

import java.sql.Timestamp;

/**
 *
 * @author Jared
 */
public class Domain {
   
   public Domain(String pURL, int pTimesVisited, Timestamp pLastTimeCrawled) {
      URL = pURL;
      timesVisited = pTimesVisited;
      lastTimeCrawled = pLastTimeCrawled;
   }
   
   public String getURL() {
      return URL;
   }
   
   public int getTimesVisited() {
      return timesVisited;
   }
   
   public Timestamp getLastTimeCrawled() {
      return lastTimeCrawled;
   }
   
   private final String URL;
   private final int timesVisited;
   private final Timestamp lastTimeCrawled;
   
}