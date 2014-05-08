/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package crawler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Class to get the robots.txt and all the webpages within
 * @author Chase Willden
 */
public final class Robots {
    
    private String robotstxt;
    private final List<String> sitemaps;
    private final List<String> webpages;
    
    public Robots(String url) throws IOException{        
        this.sitemaps = new ArrayList(); 
        setRobotstxt(url);
        populateSitemaps();
        this.webpages = new ArrayList();
        populateWebpages();
    }
    
    public List<String> getCrawlableWebpages(){
        return getWebpages();
    }
    
    private void populateWebpages() throws IOException{
        for (String map : getSitemaps() ){
            String xml = Jsoup.connect(map).ignoreContentType(true).execute().body();
            Elements locs = Jsoup.parse(xml).getElementsByTag("loc");
            locs.stream().forEach((loc) -> {
                getWebpages().add(loc.text());
            });
        }
    }
    
    private void populateSitemaps(){
        String robots = getRobotstxt();
        String[] sm = robots.split("\n");
        for (String s : sm){
            if (s.toLowerCase().contains("sitemap")){
                getSitemaps().add(s.split("map: ")[1]);
            }
        }
    }

    /**
     * @return the robotstxt
     */
    private String getRobotstxt() {
        return this.robotstxt;
    }

    /**
     * Setting the robots.txt file to work with
     * @param url
     * @throws java.io.IOException
     */
    private void setRobotstxt(String url) throws IOException {
        String robots = url + "/robots.txt";
        Response response = Jsoup.connect(robots).execute();
        if (response.statusCode() == 200){
            this.robotstxt = Jsoup.connect(robots).ignoreContentType(true).execute().body();
        }
        else{
            this.robotstxt = null;
        }
    }

    /**
     * @return the sitemaps
     */
    private List<String> getSitemaps() {
        return sitemaps;
    }

    /**
     * @return the webpages
     */
    private List<String> getWebpages() {
        return webpages;
    }
    
}
