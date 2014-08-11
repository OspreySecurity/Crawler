/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PiCrawler;

import java.sql.*;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * This class gets 10 URLs at a time and returns them when called.
 *
 * @author Jared
 */
public class URLHandler {

   /**
    *
    */
   public URLHandler() {
      size = 10;
      comparor = new DomainComparor();
      URLs = new PriorityQueue<Domain>(size, comparor);
   }

   /**
    *
    * @param pSize
    */
   public URLHandler(int pSize) {
      size = pSize;
      comparor = new DomainComparor();
      URLs = new PriorityQueue<Domain>(size, comparor);
   }

   public void fill() {
      System.out.println("");
      System.out.println("=============================================");
      System.out.println("***** Stared Filling the Handler *****");
      System.out.println("=============================================");
      System.out.println("");

      getURLs();
      

   }

   private void getURLs() {
      try {
         //STEP 2: Register JDBC driver
         Class.forName("com.mysql.jdbc.Driver");

         //STEP 3: Open a connection
         System.out.println("Connecting to database...");
         Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);

         //STEP 4: Execute a query
         System.out.println("Creating insert statement...");
         Statement stmt = conn.createStatement();
         String sql;

         sql
         = "SELECT domain_name, times_visited, last_crawled_date FROM domain WHERE crawl=1";
         ResultSet rs = stmt.executeQuery(sql);

         while (rs.next()) {
            URLs.add(new Domain(rs.getString("domain_name"),
                                       rs.getInt("times_visited"),
                                       rs.getTimestamp("last_crawled_date")));
         }

      } catch (ClassNotFoundException | SQLException ex) {
         System.out.println("Couldn't open database");
      }
   }
   
   private void trim() {
      
   }

   /**
    *
    */
//   public void fillFromFile() {
//      try {
//         System.out.println("");
//         System.out.println("=============================================");
//         System.out.println("***** Stared Filling the Handler *****");
//         System.out.println("=============================================");
//         System.out.println("");
//
//         File file = new File(getClass().getResource("websites.txt").toURI());
//         BufferedReader reader = new BufferedReader(new FileReader(file));
//         String url = null;
//         int errorCount = 0;
//
//         while (URLs.size() < size) {
//            url = reader.readLine();
//            if (url == null) {
//               errorCount++;
//
//               if (errorCount > 10)
//                  System.out.println(errorCount
//                                     + " errors reading in URLs (URLHandler line:49)");
//
//               continue;
//            }
//
//            System.out.println("* Added " + url + " to handler");
//
//            URLs.add(url);
//         }
//
//      } catch (URISyntaxException ex) {
//         Logger.getLogger(URLHandler.class.getName()).
//                 log(Level.SEVERE, null, ex);
//      } catch (FileNotFoundException ex) {
//         Logger.getLogger(URLHandler.class.getName()).
//                 log(Level.SEVERE, null, ex);
//      } catch (IOException ex) {
//         Logger.getLogger(URLHandler.class.getName()).
//                 log(Level.SEVERE, null, ex);
//      }
//
//      System.out.println("");
//      System.out.println("=============================================");
//      System.out.println("***** Finished Filling the Handler *****");
//      System.out.println("=============================================");
//      System.out.println("");
//
//   }

   /**
    *
    * @return
    */
   public String getNext() {
      return URLs.poll().getURL();

   }

   /**
    *
    * @return
    */
   public String peekNext() {
      return URLs.peek().getURL();

   }

   /**
    *
    * @return URLs.size
    */
   public int count() {
      return URLs.size();
   }

   // JDBC driver name and database URL
   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
   static final String DB_URL = "jdbc:mysql://localhost/OspreySecurity";

   //  Database credentials
   static final String USER = "OspreySecurity";
   static final String PASS = "osprey";

   private final Queue<Domain> URLs;

   private DomainComparor comparor;
   private final int size;

}
