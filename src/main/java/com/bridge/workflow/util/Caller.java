package com.bridge.workflow.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Caller {
    private final static String JAVA_ID_PATTERN = "(\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*)\\.*";
    private final static Pattern FQCN = Pattern.compile(JAVA_ID_PATTERN + "(\\." + JAVA_ID_PATTERN + ")*");

    public static String shortClassName(String clazz) {
        Matcher matcher = FQCN.matcher(clazz);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            if (matcher.hitEnd()) {
                sb.append(matcher.group(1));
                break;
            } else {
                sb.append(matcher.group(1).charAt(0));
                sb.append('.');
            }
        }
        return sb.toString();
    }
}
