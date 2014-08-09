/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PiCrawler;

import java.io.*;
import java.net.URISyntaxException;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

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
      try {
         File file = new File(getClass().getResource("websites.txt").toURI());
         BufferedReader reader = new BufferedReader(new FileReader(file));
         String url = null;
         int errorCount = 0;
         
         while(URLs.size() < size) {
            url = reader.readLine();
            if(url == null) {
               errorCount++;
               
               if(errorCount > 10) {
                  System.out.println(errorCount + " errors reading in URLs (URLHandler line:49)");
               }
               
               continue;
            }
            URLs.add(url);
         }
         
      } catch (URISyntaxException ex) {
         Logger.getLogger(URLHandler.class.getName()).log(Level.SEVERE, null, ex);
      } catch (FileNotFoundException ex) {
         Logger.getLogger(URLHandler.class.getName()).log(Level.SEVERE, null, ex);
      } catch (IOException ex) {
         Logger.getLogger(URLHandler.class.getName()).log(Level.SEVERE, null, ex);
      }
      
   }
   
   /**
    *
    * @return
    */
   public String getNext() {
      return URLs.poll();
      
   }
   
   /**
    *
    * @return
    */
   public String peekNext() {
      return URLs.peek();
      
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
