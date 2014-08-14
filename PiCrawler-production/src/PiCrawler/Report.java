/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PiCrawler;

import com.mysql.jdbc.MysqlDataTruncation;
import java.sql.*;
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
   private String removeQuotes(String str) {
      str = str.replace("\"", "");
      str = str.replace("\'", "");

      return str;
   }

   private String removeColons(String str) {
      str = str.replace(";", "");
      str = str.replace(":", "");

      return str;
   }

   private String sanitize(String str) {
      str = removeQuotes(str);
      str = removeColons(str);

      return str;
   }

   public synchronized void writeReport() {
      Connection conn = null;
      Statement stmt = null;
      String page = "This is not a page";
      try {
         //STEP 2: Register JDBC driver
         Class.forName("com.mysql.jdbc.Driver");

         //STEP 3: Open a connection
         conn = DriverManager.getConnection(DB_URL, USER, PASS);

         //STEP 4: Execute a query
//         System.out.println("Creating insert statement...");
         stmt = conn.createStatement();
         String sql;

         String header = "Domain Name";
         page = report.get(header);
         report.remove(header);

//         System.out.println("domain_name should be: " + page);
         if (page == null)
            return;

//         sql = "INSERT INTO domain(domain_name, crawl, created_on) "
//               + "VALUES ('" + page + "', 1, SYSDATE())";
//         stmt.executeUpdate(sql);
         sql = "SELECT id FROM domain WHERE domain_name LIKE 'http%://" + page
               + "'";
         ResultSet rs = stmt.executeQuery(sql);

         rs.next();
         int id;
         if (rs.getInt("id") != 0)
            id = rs.getInt("id");
         else
            return;

         Set<String> keys = report.keySet();

         String val;
         for (String str : keys) {
            if (str != null)
               str = sanitize(str);

            val = report.get(str);

            if (val != null)
               val = sanitize(val);

            sql
            = "REPLACE INTO info(domain_id, header_field, value, created_on)"
              + "VALUES (" + id + ", '" + str + "', '" + val + "', SYSDATE())";
            stmt.executeUpdate(sql);
         }

         sql = "UPDATE domain SET last_crawled_date=CURRENT_TIMESTAMP where id="
               + id;

         stmt.executeUpdate(sql);

         //STEP 6: Clean-up environment
         rs.close();
         stmt.close();
         conn.close();
      } catch (MysqlDataTruncation ex) {

      } catch (SQLException se) {

      } catch (Exception e) {

      } finally {
         //finally block used to close resources
         try {
            if (stmt != null)
               stmt.close();
         } catch (SQLException se2) {
         }// nothing we can do
         try {
            if (conn != null)
               conn.close();
         } catch (SQLException se) {
            se.printStackTrace();
         }//end finally try
      }//end try
   }

   /*
    * this merely clears out the list, it doesn't dealocate it meaning if there
    * is ever a huge report the memory will never be freed. On the other hand,
    * it is faster because it doesn't have to reallocate memory
    */
   public void clear() {
      report.clear();
   }

   // JDBC driver name and database URL
   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
   static final String DB_URL = "jdbc:mysql://localhost/OspreySecurity";

   //  Database credentials
   static final String USER = "OspreySecurity";
   static final String PASS = "osprey";

   private final Map<String, String> report;

}
