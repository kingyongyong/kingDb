package com.sqy.kingdb.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KingDbUtil {
    //去除所有空格
    public static String replaceAllBlank(String str) {
        String s = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            s = m.replaceAll("");
        }
        return s;
    }
}
