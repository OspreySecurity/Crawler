/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package standards;

import java.text.DecimalFormat;
import java.util.List;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Jared
 */
public class CheckHttps extends Standard {

   public CheckHttps(Document doc) {
      super(doc);
   }

   public double formatDecimalScore(double number) {
      if (!Double.isNaN(number)) {
         DecimalFormat df = new DecimalFormat("#.###");
         double percent = Double.parseDouble(df.format(number));
         return Math.ceil(percent * 1000) / 10;
      } else
         return 0;

   }

   @Override
   public List<String> test() {

      result.add("Percent of src and href attributes not https");
      result.add(Double.toString(checkForHttps()) + "%");

      return result;
   }

   /**
    * Checks the src and href for https or not
    *
    * @return
    */
   public double checkForHttps() {
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
         if (linkSplit.length > 1 && linkSplit[0].toLowerCase().equals("http"))
            hrefAry[count++] = false;
         else if (linkSplit.length > 1 && linkSplit[0].toLowerCase().equals(
                 "https"))
            hrefAry[count++] = true;
         else
            hrefAry[count++] = true;
      }

      count = 0;
      // Get each elements HREF
      for (Element link : src) {
         String linkSrc = link.attr("src");
         String[] linkSplit = linkSrc.split("://");
         if (linkSplit.length > 1 && linkSplit[0].toLowerCase().equals("http"))
            srcAry[count++] = false;
         else if (linkSplit.length > 1 && linkSplit[0].toLowerCase().equals(
                 "https"))
            srcAry[count++] = true;
         else
            srcAry[count++] = true;
      }

      // Count how many are http instead of https
      double isHttp = 0.0;
      for (Boolean hrefAry1 : hrefAry)
         if (hrefAry1 == false)
            isHttp++;
      for (Boolean srcAry1 : srcAry)
         if (srcAry1 == false)
            isHttp++;

      return formatDecimalScore(isHttp / (srcAry.length + hrefAry.length));
   }
}
