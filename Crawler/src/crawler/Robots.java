/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package crawler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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
    private String baseUrl;
    private int totalWebpages = 0;
    private String fileLoc;
    
    public Robots(String url) throws IOException{        
        this.sitemaps = new ArrayList(); 
        this.baseUrl = url;
        setRobotstxt(url);
        populateSitemaps();
        this.webpages = new ArrayList();
        populateWebpages();
    }
    
    public List<String> getCrawlableWebpages(){
        return getWebpages();
    }
    
    private void getXmlPage(String url) throws IOException{
        Response response = Jsoup.connect(url).ignoreContentType(true).execute();
        if (response.statusCode() == 200){
            String xml = response.body();
            scanAndParseXmlString(xml);
        }        
    }
    
    /**
     * Write contents to a file
     * @return absolute file path
     * @throws IOException 
     */
    private String toFile() throws IOException{
        String fileName = this.baseUrl.split("//")[1].split("/")[0];
        File file = new File(fileName + ".txt");
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        try (BufferedWriter bw = new BufferedWriter(fw)) {
            String content = "";
            content = this.getWebpages().stream().map((line) -> line + "\n").reduce(content, String::concat);
            bw.write(content);
        }
        return file.getAbsolutePath();
    }
    
    private void scanAndParseXmlString(String xml){
        Document x = Jsoup.parse(xml);
        Elements locs = x.getElementsByTag("loc");
        if (locs.isEmpty()){
            System.out.println("Populating Webpages in Robots Class not working");
        }
        else{
            locs.stream().forEach((Element loc) -> {
                if (loc.text().contains(".xml")){
                    try {
                        getXmlPage(loc.text());
                    } catch (IOException ex) {
                        Logger.getLogger(Robots.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                else{
                    this.totalWebpages++;
                    System.out.println("Total webpages retrieved: " + this.totalWebpages);
                    getWebpages().add(loc.text());
                }
            });
        }
    }
    
    public String getFileLocation(){
        return this.fileLoc;
    }
    
    private void populateWebpages() throws IOException{
        System.out.println("Populating Webpages");
        for (String map : getSitemaps() ){            
            String xml = Jsoup.connect(map).ignoreContentType(true).execute().body();
            scanAndParseXmlString(xml);            
        }
        this.fileLoc = toFile();
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
