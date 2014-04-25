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
        this.baseurl = url;
        this.html = Jsoup.connect(baseurl).get();
        this.report = Jsoup.parse("<html><body></body></html>");
    }
    
    public Elements query(String query){
        return this.html.select(query);
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
        Elements alts = query("img:not([alt]):not([alt='']), video:not([alt]):not([alt='']), audio:not([alt]):not([alt='']), area:not([alt]):not([alt=''])");
        adaAddToReport(alts);        
    }
    
    public Document generateReport(){
        return this.report;
    }
}
