package com.ucomputersa.common.utils;

public class StringUtil {
    public static String replaceIndex(int start, int end, String target, String replacement) {
        StringBuilder stringBuilder = new StringBuilder(target);
        stringBuilder.replace(start, end, replacement);
        return stringBuilder.toString();
    }
}
