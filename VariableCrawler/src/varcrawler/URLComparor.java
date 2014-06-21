/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package varcrawler;

import java.util.Comparator;

/**
 *
 * @author Jared
 */
public class URLComparor implements Comparator<String> {

  @Override
  public int compare(String o1, String o2) {
    //we will implement some fancy logic later to assure
    //urgent things get done first
    int choice = (int) (Math.random() * 2);

    if (choice > 0) {
      return 1;
    }
    if (choice < 0) {
      return -1;
    }
    
    return 0;
  }

}
