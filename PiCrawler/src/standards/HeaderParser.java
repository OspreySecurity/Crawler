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

      public List<String> test(Map<String, List<String>> connFields) {
      try {
         URL url = new URL(DOM.baseUri());
//         URLConnection conn = url.openConnection();
//         Map<String, List<String>> map = conn.getHeaderFields();
         String value;

         InetAddress address = InetAddress.getByName(url.getHost());

         String ip = address.getHostAddress();

         result.add("IP Address");
         result.add(ip);

         // indexOf returns -1 if it doesn't exist
         if (connFields.containsKey("Server")) {
            List<String> strList = connFields.get("Server");
               System.out.println(strList);
            if (strList.size() > 0) {
               String serverType = strList.get(0);
               String[] list = serverType.split(" ");
               for(String str : list) {
                  if(str.contains("(")) {
                     String os = str.substring(1, str.length() -1);
                     System.out.println("Operating system " + os);
                     result.add("Operating System");
                     result.add(os);
                  } else if (str.contains("/")) {
                     String[] items = str.split("/");
                     System.out.println(items[0]+ " Version");
                     System.out.println(items[1]);
                     result.add(items[0] + " Version");
                     result.add(items[1]);
                  }
               }
               System.out.println("There is a server type!");
               for (String str : strList)
                  System.out.println(str);
            }
         }

         for (Map.Entry<String, List<String>> entry : connFields.entrySet()) {
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

   @Override
   public List<String> test() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

}
