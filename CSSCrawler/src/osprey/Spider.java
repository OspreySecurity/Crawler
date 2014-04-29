/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package osprey;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Chase Willden
 */
public class Spider {
    private final String baseurl;
    private final Document html;
    private final Document report;
    
    public Spider(String url) throws IOException{
        this.report = Jsoup.parse("<html><body></body></html>"); 
        if (!url.isEmpty()){
            this.baseurl = url;
            this.html = Jsoup.connect(baseurl).get();                      
        }
        else{
            this.baseurl = "";
            this.html = null;
        }
    }
    
    public String formatUrl(String url){
        if (!url.contains("http://")){
            url = "http://" + url;
        }
        if (!url.contains("www.")){
            String[] split = url.split("//");
            url = split[0] + "//" + "www." + split[1];
        }
        return url;
    }
    
    public Elements cssQuery(String query){
        return this.html.select(query);
    }
    
    public Elements queryText(String query){
        return this.html.getElementsContainingOwnText(query);
    }
    
    public void ada(){
        //http://www.techrepublic.com/blog/web-designer/creating-an-ada-compliant-website/#.
        this.report.getElementsByTag("body").append("<div id='ada'><h2>Ada Report</h2><pre></pre></div>");
        adaAltAttr();
    }

    public void adaAddToReport(Elements eles){
        String h = "";
        for (Element ele : eles){            
            h += ele.outerHtml();
        }
        this.report.getElementById("ada").getElementsByTag("pre").append(h);
    }
        
    public void adaAltAttr(){        
        Elements alts = cssQuery("img:not([alt]):not([alt='']), video:not([alt]):not([alt='']), audio:not([alt]):not([alt='']), area:not([alt]):not([alt=''])");
        adaAddToReport(alts);        
    }
    
    public Document generateReport(){
        return this.report;
    }
}
