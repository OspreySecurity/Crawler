/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package osprey;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author willdech
 */
public final class Crawler {
    private final String url;
    private Document html;
    
    public Crawler(String url){
        this.url = url;
        if (!this.getWebpage()){
            System.out.println("Webpage Unavailable");
        }
    }
    
    /**
     * CSS query to select elements
     * @param query
     * @return 
     */
    public Elements query(String query){
        // Example "class=header && tag=a"
        return this.html.select(query);
    }
    
    public Map adaIssues(){  
        // Check for alt tags on Images
        Map noAlts = noAlts();
        return noAlts;
    }
    
    public Map noAlts(){
        Elements eles = query("img");
        Map map = new HashMap();
        for (Element ele : eles){
            if (!ele.hasAttr("alt")){                
                map.put("No Alt Tag", ele.toString());
            }
        }
        
        return map;
    }
    
    /**
     * Get the webpage as an HTML document
     * @return 
     */
    public boolean getWebpage(){
        try {
            this.html = Jsoup.connect(url).get();
        } catch (IOException ex) {
            return false;
        }
        return true;
    }
}
