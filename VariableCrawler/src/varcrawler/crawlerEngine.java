/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package varcrawler;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jared, Chase, Matt
 */
public class crawlerEngine {

   public crawlerEngine() {
      maxSpiders = 10;
      minSpiders = 2;
      maxHandlers = 3;

      spiders = new ThreadGroup("spiders");
      handlers = new ThreadGroup("handlers");

      queue = new ArrayDeque<String>();

      startHandlers();

      // wait for the queue to get big enough before you release 
      // the spiders
      try {
         while (queue.size() > 40)
            synchronized (this) {
               this.wait(50);
            }
      } catch (InterruptedException ex) {
         Logger.getLogger(crawlerEngine.class.getName()).log(Level.SEVERE, null,
                                                             ex);
      }

      startSpiders();
   }

   /*
    * Starts the number of spider threads specified by maxSpiders
    */
   private void startSpiders() {
      for (int i = 0; i < maxSpiders / 2; ++i)
//         Spider spider = new Spider(this);
//         spider.run();
         addSpider();

   }

   /*
    * Starts the number of handlers threads specified by maxHandlers
    */
   private void startHandlers() {
      for (int i = 0; i < maxHandlers / 2; ++i)
//         URLHandler handler = new URLHandler(this, "websites.txt");
//         handler.run();
         addHandler();
   }

   public synchronized boolean add(String url) {
      queue.add(url);

      System.out.println("\n\n\n******************************************\n"
                         + "NumSpiders = " + spiders.activeCount() + "\n"
                         + "******************************************\n\n\n");
      System.out.println("\n\n\n******************************************\n"
                         + "NumHandlers = " + handlers.activeCount() + "\n"
                         + "******************************************\n\n\n");
      System.out.println("\n\n\n******************************************\n"
                         + "QueueSize = " + queue.size() + "\n"
                         + "******************************************\n\n\n");

      // if there are to many sites in the queue
      if (queue.size() >= maxQueueSize || spiders.activeCount() <= 0)
         addSpider();

      // if we need to kill a handler
      if (killHandler > 0) {

         // this is also checked in get next...
         // shouldn't ever happen, but just in case
         // you never want 0 handlers
         if (handlers.activeCount() <= 0) {
            killHandler = 0;
//            numHandlers = 1;
            // keep the handler alive
            return true;
         }

         // would a reset to 0 work better?
         --killHandler;

         assert killHandler >= 0;

//         subHandler();
         // kill the handler
         return false;
      }

      // keep the handler alive
      return true;
//      else if (queue.size() < minQueueSize)
//         subSpider();

   }

   public synchronized void addSpider() {
      if (spiders.activeCount() > maxSpiders)
         ++killHandler;

      Thread t = new Thread(spiders, new Spider(this));
      t.start();
//      ++numSpiders;

      System.out.println("\n\n\n******************************************\n"
                         + "Added a Spider\n"
                         + "******************************************\n\n\n");

   }

//   private void subSpider() {
//      --numSpiders;
//   }
   private synchronized void addHandler() {
      // there can never be more handlers than the max
      if (handlers.activeCount() >= maxHandlers)
         return;

      Thread t = new Thread(handlers, new URLHandler(this, "websites.txt"));
      t.start();
//      ++numHandlers;

      System.out.println("\n\n\n******************************************\n"
                         + "Added a Handler\n"
                         + "******************************************\n\n\n");

   }

//   private void subHandler() {
//      --numHandlers;
//   }
   public synchronized Boolean keepHandlerAlive() {
      if (handlers.activeCount() > 1)
         return false;
      
      System.out.println("######### Kept Handler alive #########");
      
      return true;
   }

   /*
    *
    */
   public synchronized String getNext() {
      // if there are to few spiders
      if (queue.size() > maxQueueSize) {
         addSpider();

         if (handlers.activeCount() <= 0) {
            addHandler();
            return "http://www.imprintedstudios.com";
         }
      }

      // this will kill a spider
      if (queue.size() <= minQueueSize) {

         // if the number of spiders gets low add a handler
         if (spiders.activeCount() <= minSpiders)
            addHandler();

         // kill it
//         subSpider();
         return null;
      }
      // otherwise get the next url
      return queue.poll();

//      String url = queue.poll();
//      return (url != null) ? url:"http://www.imprintedstudios.com";
      // this should kill a spider if there are to many
//      if (queue.size() < minQueueSize) {
//         // if there are to few handlers
//         if (numHandlers <= 0) {
//            addHandler();
//            return "http://www.imprintedstudios.com";
//         }
//
//         //if there are to many spiders
//         --numSpiders;
//         return null;
//      } else //normal case
//         return queue.poll();
   }

   /**
    *
    * @param args
    */
   public static void main(String[] args) {
      crawlerEngine engine = new crawlerEngine();
   }

   /*
    * if Java could do a pound define i'd do it, but this is the next best
    * thing.
    */
   private final int maxSpiders;
   private final int minSpiders;
   private final int maxHandlers;

//   private int numSpiders;
//   private int numHandlers;
   private int killHandler;

   private static final int maxQueueSize = 75;
   private static final int minQueueSize = 25;

   private final ThreadGroup spiders;
   private final ThreadGroup handlers;

//  private crawlerEngine engine;
   private final Queue<String> queue;
}
