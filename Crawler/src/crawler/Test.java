/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package crawler;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Chase Willden
 */
public class Test {
    public static void main(String[] args){
        try {
            Robots r = new Robots("http://davidwalsh.name/");
            r.getCrawlableWebpages().stream().forEach((page) -> {
                System.out.println(page);
            });
        } catch (IOException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
