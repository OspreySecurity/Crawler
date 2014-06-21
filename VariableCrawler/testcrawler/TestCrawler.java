/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testcrawler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Jared
 */
public class TestCrawler extends Thread {

  /**
   */
  @Override
  public void run() {
    String report = "\nDownload Time: " + (this.downloadTime / 1000.0) + " seconds";
    report += "\nDomain is https: " + domainHttps();
    report += "\nPercent of src and href attributes not https: " + checkHttps() + "%";
    report += "\nTotal forms with Password as GET: " + checkPassUrl();
    System.out.println(report);
  }

  // Get the website

  /**
   * dowloads and parses the website
   * @param url
   * @throws IOException
   */
    public void download(String url) throws IOException {
    Date pre = new Date();
    this.DOM = Jsoup.connect(url).timeout(10 * 1000).get();
    Date post = new Date();
    this.downloadTime = (post.getTime() - pre.getTime());
  }

  // Check Domain wither HTTP or HTTPS

  /**
   * tells if the base domain is https
   * @return
   */
    public boolean domainHttps() {
    String uri = this.DOM.baseUri();
    String[] uriSplit = uri.split("://");
    if (uriSplit[0].equals("https")) {
      return true;
    } else if (uriSplit[0].equals("http")) {
      return false;
    } else {
      return false;
    }
  }

  /**
   * cheks all href and src attributes for https
   * @return
   */
  public double checkHttps() {
    // Get Elements
    Elements src = this.DOM.getElementsByAttribute("src");
    Elements href = this.DOM.getElementsByAttribute("href");

    // create arrays
    Boolean[] srcAry = new Boolean[src.size()];
    Boolean[] hrefAry = new Boolean[href.size()];
    Integer count = 0;

    // Get each elements HREF
    for (Element link : href) {
      String linkHref = link.attr("href");
      String[] linkSplit = linkHref.split("://");
      if (linkSplit.length > 1 && linkSplit[0].toLowerCase().equals("http")) {
        hrefAry[count++] = false;
      } else if (linkSplit.length > 1 && linkSplit[0].toLowerCase().equals("https")) {
        hrefAry[count++] = true;
      } else {
        hrefAry[count++] = true;
      }
    }

    count = 0;
    // Get each elements HREF
    for (Element link : src) {
      String linkSrc = link.attr("href");
      String[] linkSplit = linkSrc.split("://");
      if (linkSplit.length > 1 && linkSplit[0].toLowerCase().equals("http")) {
        srcAry[count++] = false;
      } else if (linkSplit.length > 1 && linkSplit[0].toLowerCase().equals("https")) {
        srcAry[count++] = true;
      } else {
        srcAry[count++] = true;
      }
    }

    // Count how many are http instead of https
    double isHttp = 0.0;
    for (int i = 0; i < hrefAry.length; i++) {
      if (hrefAry[i] == false) {
        isHttp++;
      }
    }
    for (int i = 0; i < srcAry.length; i++) {
      if (srcAry[i] == false) {
        isHttp++;
      }
    }
    return formatScore(isHttp / (srcAry.length + hrefAry.length));
  }

  /**
   * formats and returns the score
   * @param number
   * @return
   */
  public double formatScore(double number) {
    DecimalFormat df = new DecimalFormat("#.###");
    double percent = Double.parseDouble(df.format(number));
    return Math.ceil(percent * 1000) / 10;
  }

  // Get forms that are method POST

  /**
   * checks if password is in url
   * @return
   */
    public int checkPassUrl() {
    // Get all forms
    Elements forms = this.DOM.getElementsByTag("form");

    int sensative = 0;
    for (Element form : forms) {
      if (form.attr("method").toUpperCase().equals("GET")) {
        Elements children = form.getAllElements();
        for (Element child : children) {
          if (child.attr("type").toLowerCase().equals("password")) {
            sensative++;
          } else if (child.attr("name").toLowerCase().contains("pass")) {
            sensative++;
          } else if (child.attr("id").toLowerCase().contains("pass")) {
            sensative++;
          }
        }
      }
    }
    return sensative;
  }

  /**
   *
   */
  protected Document DOM;

  /**
   *
   */
  protected double downloadTime;

  TestCrawler(ThreadGroup group, String thread, String pUrl) {
    super(group, thread);
    try {
      download(pUrl);
    } catch (IOException ex) {
      Logger.getLogger(TestCrawler.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

}
