package com.raaivan.util;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.RegularExpression;

public class Base64 {
    public static boolean is_base64(String input){
        String regex = "^([0-9a-zA-Z+/]{4})*(([0-9a-zA-Z+/]{2}==)|([0-9a-zA-Z+/]{3}=))?$";
        return (new RegularExpression(regex)).matches(input.replaceAll("\\s", "+"));
    }

    public static byte[] encode(byte[] input){
        try {
            if (input == null || input.length == 0) return input;
            return org.apache.tomcat.util.codec.binary.Base64.encodeBase64(input);
        }
        catch (Exception ex){
            return new byte[0];
        }
    }

    public static String encode(String input){
        try {
            if (input == null || input.equals("")) return input;
            return new String(encode(input.getBytes()));
        }catch (Exception ex){
            return "";
        }
    }

    public static String decode(String input) {
        try {
            if (input == null || !is_base64(input)) return input;
            input = input.replaceAll("\\s", "+");
            byte[] valueDecoded = org.apache.tomcat.util.codec.binary.Base64.decodeBase64(input.getBytes());
            return new String(valueDecoded);
        } catch (Exception ex) {
            return input;
        }
    }
}
