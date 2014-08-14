/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PiCrawler;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Jared
 */
public class addSites {

   public addSites() {

   }

   private void add(Set<String> pages) {
      try {
         Class.forName("com.mysql.jdbc.Driver");
         Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
         Statement stmt = conn.createStatement();
         String sql;

         for (String page : pages) {

            if (page == null)
               continue;

            sql = "SELECT id from domain where domain_name='" + page + "'";
            ResultSet rs = stmt.executeQuery(sql);
            Boolean bool = rs.next();

            if (bool)
               continue;

            sql = "INSERT INTO domain(domain_name, crawl, created_on) "
                  + "VALUES ('" + page + "', 1, SYSDATE())";

            stmt.executeUpdate(sql);
         }

      } catch (ClassNotFoundException ex) {
      } catch (SQLException ex) {
      }
   }

   public void write(Document doc) {
      Document DOM = doc;
      Set<String> newSites = new HashSet<String>();
      String[] parts;
      String linkHref;
      String middle;

      Elements href = DOM.getElementsByAttribute("href");

      for (Element link : href) {
         linkHref = link.attr("href");
         if (linkHref.contains("http")) {
            parts = linkHref.split("/");
            if (parts.length < 3)
               continue;
            middle = "//";
            if (!linkHref.contains("www"))
               middle += "www.";
            linkHref = parts[0] + middle + parts[2]; // only grab the domain name
            newSites.add(linkHref);

         }
      }
      add(newSites);
   }

   // JDBC driver name and database URL
   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
   static final String DB_URL = "jdbc:mysql://localhost/OspreySecurity";

   //  Database credentials
   static final String USER = "OspreySecurity";
   static final String PASS = "osprey";

}
