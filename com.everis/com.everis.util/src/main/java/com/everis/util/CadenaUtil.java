package com.everis.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CadenaUtil {

    public CadenaUtil() {
        super();
    }
    
    public static int match(String value, String pattern) {
        int i = 0;
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(value);
        while(m.find()){
            i++;
        }
        
        return i;
    }
    
    public static String replace(String text, String[] invalid, String[] valid) {
        String result = text;
        
        if(result != null && !result.isEmpty()) {
	        for(int i = 0; i < invalid.length; i++) {
	        	result = result.replaceAll(invalid[i], valid[i]);
	        }
        }
        
        return result;
    }
    
}
