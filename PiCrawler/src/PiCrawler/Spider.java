/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PiCrawler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import standards.*;

/**
 *
 * @author Jared, Chase, Matt
 */
public class Spider {

   Spider(crawlerEngine engine) {
      adder = new addSites();
   }

   /**
    * @param domainName
    * @return
    */
   public Boolean crawl(String domainName) {
      report = new Report();
      float score = 100.00f;

      assert (domainName != null);

      System.out.println("");
      System.out.println("=============================================");
      System.out.println("***** Started crawling " + domainName + " *****");
      System.out.println("=============================================");
      System.out.println("");

      try {
         //Try to get this down to a single connection
         Date pre = new Date();
         Document DOM = Jsoup.connect(domainName).timeout(10 * 1000).get();
         Date post = new Date();
         System.out.println("* Connected via JSOUP");

         URL url = new URL(domainName);
         URLConnection conn = url.openConnection();
         Map<String, List<String>> connFields = conn.getHeaderFields();
         System.out.println("* Connected via URL");
         
         //Add new sites to database
         adder.write(DOM);


         

         domainName = url.getHost();

         report.add("Domain Name", domainName);
         report.add("Download Time", Double.
                 toString(getDownloadTime(pre, post)));
         report.add("Default port", Integer.toString(url.getDefaultPort()));
         report.add("Host Name", url.getHost());
         report.add("Protocol", url.getProtocol());

         System.out.println("* Started test suite");

         DomainHttps https = new DomainHttps(DOM);
         LoginForm login = new LoginForm(DOM);
         CheckHttps checkHttps = new CheckHttps(DOM);
         CheckPassUrl checkPassUrl = new CheckPassUrl(DOM);
//         addSites adder = new addSites(DOM);
         HeaderParser parser = new HeaderParser(DOM);

         // eventually we will make these dynamically loaded, then this will be epic
         report.add(https.test());
         report.add(checkHttps.test());
         report.add(checkPassUrl.test());
         if (login.hasLoginForm()){
            report.add(login.test());
         }
         report.add(parser.test(connFields));
         System.out.println("* Finished test suite");

      } catch (MalformedURLException ex) {

         removeDomain(domainName);
         System.out.println("<<<<<< MalformedURLException: " + domainName);

//         System.out.println("Heres the bad url : " + domainName);
//         Logger.getLogger(Spider.class.getName()).log(Level.SEVERE, null, ex);
      } catch (IOException ex) {

         removeDomain(domainName);
         System.out.println("<<<<<< IOException: " + domainName);
//         Logger.getLogger(Spider.class.getName()).log(Level.SEVERE, null, ex);
      } catch (IllegalArgumentException ex) {
         removeDomain(domainName);
         System.out.println("<<<<<< IllegalArgumentException: " + domainName);
      }

      System.out.println("");
      System.out.println("=============================================");
      System.out.println("***** Finished Crawling " + domainName + " *****");
      System.out.println("=============================================");
      System.out.println("");
      return null;
   }

   /**
    * @param pre
    * @param post
    */
   private double getDownloadTime(Date pre, Date post) {
      return (post.getTime() - pre.getTime()) / 1000.0;
   }

   public Report getReport() {
      return report;
   }

   public void removeDomain(String domainName) {
      Connection conn = null;
      Statement stmt = null;
      String page = "This is not a page";
      try {
         //STEP 2: Register JDBC driver
         Class.forName("com.mysql.jdbc.Driver");

         //STEP 3: Open a connection
         System.out.println("Connecting to database...");
         conn = DriverManager.getConnection(DB_URL, USER, PASS);

         //STEP 4: Execute a query
//         System.out.println("Creating insert statement...");
         stmt = conn.createStatement();
         String sql;

         sql = "UPDATE domain SET crawl=0 where domain_name='"
               + domainName + "'";

         stmt.executeUpdate(sql);

      } catch (ClassNotFoundException ex) {
         Logger.getLogger(Spider.class.getName()).log(Level.SEVERE, null, ex);
      } catch (SQLException ex) {
         Logger.getLogger(Spider.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   // JDBC driver name and database URL
   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
   static final String DB_URL = "jdbc:mysql://localhost/OspreySecurity";

   //  Database credentials
   static final String USER = "OspreySecurity";
   static final String PASS = "osprey";

   private Report report;
   private final addSites adder;

}
