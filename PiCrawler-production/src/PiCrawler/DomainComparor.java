/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package PiCrawler;

import java.util.Comparator;

/**
 *
 * @author Jared
 */
public class DomainComparor implements Comparator<Domain>{

   DomainComparor() {
      
   }
   
   @Override
  public int compare(Domain p1, Domain p2) {
     
     int dateDiff = p1.getLastTimeCrawled().compareTo(p2.getLastTimeCrawled());
     int crawledDiff = p1.getTimesVisited() - p2.getTimesVisited();
     
     double diff = dateDiff * .9 + crawledDiff * .1;

    if (diff > 0) {
      return 1;
    } else if (diff < 0) {
      return -1;
    } else
      return 0;
  }
   
}
