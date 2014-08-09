/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PiCrawler;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * This class gets 10 URLs at a time and returns them when called.
 * @author Jared
 */
public class URLHandler{

   /**
    *
    */
   public URLHandler() {
      size = 10;
      URLs = new ArrayBlockingQueue<String>(size);
   }
   
   /**
    *
    * @param pSize
    */
   public URLHandler(int pSize) {
      size = pSize;
      URLs = new ArrayBlockingQueue<String>(size);
   }
   
   /**
    *
    */
   public void fill() {
      
   }
   
   /**
    *
    * @return
    */
   public String getNext() {
      return null;
      
   }
   
   /**
    *
    * @return
    */
   public String peekNext() {
      return null;
      
   }
   
   /**
    *
    * @return URLs.size
    */
   public int count() {
      return URLs.size();
   }
   
   private final Queue<String> URLs;
   
   private final int size;

}
