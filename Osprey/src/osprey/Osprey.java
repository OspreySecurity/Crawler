/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package osprey;

/**
 *
 * @author willdech
 */
public class Osprey {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Crawler c = new Crawler("https://www.byui.edu");
        System.out.println(c.adaIssues());
    }
}
