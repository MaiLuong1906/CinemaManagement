/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

/**
 *
 * @author LENOVO
 */
import java.security.MessageDigest;
import java.util.Base64;

/**
 *
 * @author LENOVO
 */
public class Encoding {

    public static String toSHA1(String str) {
        String salt = "hdkajfbc skjoahddjaodn;ASc";
        String res = null;
        str = str + salt;
        try {
            byte[] dataBytes = str.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("SHA-1"); 
            byte[] hashBytes = md.digest(dataBytes); 
            res = Base64.getEncoder().encodeToString(hashBytes); 
        } catch (Exception e) {
            System.out.println(e);
        }
        return res;
    }
}
