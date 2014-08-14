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
      getURLs();
   }

   private void getURLs() {
      try {
         Class.forName("com.mysql.jdbc.Driver");
         Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
         Statement stmt = conn.createStatement();
         String sql;

         sql
         = "SELECT domain_name, times_visited, last_crawled_date FROM domain WHERE crawl=1 ORDER BY last_crawled_date";
         ResultSet rs = stmt.executeQuery(sql);

         while (rs.next())
            URLs.add(new Domain(rs.getString("domain_name"),
                                rs.getInt("times_visited"),
                                rs.getTimestamp("last_crawled_date")));

      } catch (ClassNotFoundException | SQLException ex) {
      }
   }

   public String getNext() {
      if (peekNext() != null)
         return URLs.poll().getURL();

      fill();
      return getNext();
   }

   public String peekNext() {
      if (URLs.size() > 0)
         return URLs.peek().getURL();
      else
         return null;

   }

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

   private final DomainComparor comparor;
   private final int size;

}
