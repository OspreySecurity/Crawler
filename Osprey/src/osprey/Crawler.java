/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package osprey;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author willdech
 */
public class Crawler {
    private final String url;
    private final List report = new ArrayList();
    private Document html;
    
    public Crawler(String url){
        this.url = url;
    }
    
    public void run(){
        if (this.getWebpage()){
            this.listAllTags();
        }
    }
    
    public void listAllTags(){
        Map<String, String> map = new HashMap<>();
        List elelist = new ArrayList();
        Elements eles = this.html.getAllElements();
        for (Element ele : eles){
            if (!map.containsKey(ele.nodeName())){
                map.put(ele.nodeName(), "1");
                elelist.add(ele.nodeName());
            }
            else{
                int count = Integer.parseInt(map.get(ele.nodeName()));
                map.put(ele.nodeName(), ++count + "");
            }
        }
        Collections.sort(elelist.subList(1, elelist.size()));
        this.addToReport("Html Tag Count: ");
        int len = map.size();
        while (len-- != 0){
            String e = (String) elelist.get(len);
            this.addToReport(e + ": " + map.get(e));
        }
    }
    
    public boolean getWebpage(){
        try {
            this.html = Jsoup.connect(url).get();
        } catch (IOException ex) {
            return false;
        }
        return true;
    }
    
    public List generateReport(){
        return this.report;
    }
    
    public void addToReport(String msg){
        this.report.add(msg);
    }
}
