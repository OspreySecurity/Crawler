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
         
         
//         Set<String> set = map.keySet();
         for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            result.add(entry.getKey());
            value = "";
            for (String str : entry.getValue())
               value += str;
            result.add(value);
         }

//         result.add(map);
//         for (Map.Entry<String, List<String>> entry : map.entrySet())
//            System.out.println("Key : " + entry.getKey()
//                               + " ,Value : " + entry.getValue());
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
