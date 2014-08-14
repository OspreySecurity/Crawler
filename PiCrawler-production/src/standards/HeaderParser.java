/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package standards;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.nodes.Document;

/**
 *
 * @author Jared
 */
public class HeaderParser extends Standard {

   public HeaderParser(Document doc) {
      super(doc);
   }

   @Override
   public List<String> test() {
      try {
         URL url = new URL(DOM.baseUri());
         URLConnection conn = url.openConnection();
         Map<String, List<String>> map = conn.getHeaderFields();
         String value;

         InetAddress address = InetAddress.getByName(url.getHost());

         String ip = address.getHostAddress();

         result.add("IP Address");
         result.add(ip);

         // indexOf returns -1 if it doesn't exist
         if (map.containsKey("Server")) {
            List<String> strList = map.get("Server");

            if (strList.size() > 0) {
               String serverType = strList.get(0);

               String[] list = serverType.split(" ");
               for (String str : list)
                  if (str.contains("(")) {
                     String os = str.substring(1, str.length() - 1);
                     result.add("Operating System");
                     result.add(os);
                  } else if (str.contains("/")) {
                     String[] items = str.split("/");
                     result.add(items[0] + " Version");
                     result.add(items[1]);
                  }
            }
         }
         for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            result.add(entry.getKey());
            value = "";
            for (String str : entry.getValue())
               value += str;
            result.add(value);
         }
         return result;
      } catch (MalformedURLException ex) {
         Logger.getLogger(HeaderParser.class.getName()).log(Level.SEVERE, null,
                                                            ex);
      } catch (IOException ex) {
         Logger.getLogger(HeaderParser.class.getName()).log(Level.SEVERE, null,
                                                            ex);
      }
      return null;
   }
}
