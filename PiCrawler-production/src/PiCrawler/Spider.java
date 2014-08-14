/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PiCrawler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
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

      assert (domainName != null);

      try {
         Date pre = new Date();
         Document DOM = Jsoup.connect(domainName).timeout(10 * 1000).get();
         Date post = new Date();

         //Add new sites to database
         adder.write(DOM);

         URL url = new URL(domainName);
         url.openConnection();

         domainName = url.getHost();

         report.add("Domain Name", domainName);
         report.add("Download Time", Double.
                 toString(getDownloadTime(pre, post)));
         report.add("Default port", Integer.toString(url.getDefaultPort()));
         report.add("Host Name", url.getHost());
         report.add("Protocol", url.getProtocol());

         DomainHttps https = new DomainHttps(DOM);
         LoginForm login = new LoginForm(DOM);
         CheckHttps checkHttps = new CheckHttps(DOM);
         CheckPassUrl checkPassUrl = new CheckPassUrl(DOM);
         HeaderParser parser = new HeaderParser(DOM);

         report.add(https.test());
         report.add(checkHttps.test());
         report.add(checkPassUrl.test());
         if (login.hasLoginForm())
            report.add(login.test());

         report.add(parser.test());

      } catch (MalformedURLException ex) {
         removeDomain(domainName);
      } catch (IOException ex) {
         removeDomain(domainName);
      } catch (IllegalArgumentException ex) {
         removeDomain(domainName);
      }
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
         Class.forName("com.mysql.jdbc.Driver");
         conn = DriverManager.getConnection(DB_URL, USER, PASS);
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
