/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PiCrawler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
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
      report = new Report();
   }

   /**
    * @param domainName
    * @return
    */
   public Boolean crawl(String domainName) {
      assert (domainName != null);

      try {
         Date pre = new Date();
         Document DOM = Jsoup.connect(domainName).timeout(10 * 1000).get();
         Date post = new Date();

         URL url = new URL(domainName);
         url.openConnection();

         domainName = url.getHost();

         report.add("Domain Name", domainName);
         report.
                 add("Download Time", Double.
                         toString(getDownloadTime(pre, post)));
         report.add("Default port", Integer.toString(url.getDefaultPort()));
         report.add("Host Name", url.getHost());
         report.add("Protocol", url.getProtocol());

         DomainHttps https = new DomainHttps(DOM);
         LoginForm login = new LoginForm(DOM);
         CheckHttps checkHttps = new CheckHttps(DOM);
         CheckPassUrl checkPassUrl = new CheckPassUrl(DOM);
         addSites adder = new addSites(DOM);
         HeaderParser parser = new HeaderParser(DOM);

         // eventually we will make these dynamically loaded, then this will be epic
         report.add(https.test());
         report.add(checkHttps.test());
         report.add(checkPassUrl.test());
         if (login.hasLoginForm())
            report.add(login.test());

         report.add(parser.test());

      } catch (MalformedURLException ex) {
         System.out.println("Heres the bad url : " + domainName);
         Logger.getLogger(Spider.class.getName()).log(Level.SEVERE, null, ex);
      } catch (IOException ex) {
         Logger.getLogger(Spider.class.getName()).log(Level.SEVERE, null, ex);
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

   private Report report;

}
