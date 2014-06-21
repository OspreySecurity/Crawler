/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package standards;

import java.util.List;
import org.jsoup.nodes.Document;

/**
 * A class to test the cookies within the website
 * @author Chase Willden
 */
public class Cookies extends Standard{
    public Cookies(Document doc){
        super(doc);
    }

    @Override
    public List<String> test() {
        return null;
    }
}
