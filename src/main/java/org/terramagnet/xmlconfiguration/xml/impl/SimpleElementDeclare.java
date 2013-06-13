/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.terramagnet.xmlconfiguration.xml.impl;

import org.terramagnet.xmlconfiguration.xml.Declare;
import org.terramagnet.xmlconfiguration.xml.ElementDeclare;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author terrason
 */
public class SimpleElementDeclare implements ElementDeclare {

    private static final Pattern sp = Pattern.compile("[\\w-]+(?=\\*)|(?<=[(|])[\\w-]+(?=(\\|[\\w-]+)*\\)\\*)");
    private Pattern regex;
    private Pattern multipleRegex;
    private String name;
    private String pattern;
    private boolean empty;

    /**
     * 设置元素名称.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 设置元素值的模式.
     */
    public void setPattern(String pattern) {
        this.pattern = pattern;
        if (TYPE_ANY.equals(pattern)) {
            regex = Pattern.compile("[\\w\\-]*");
            multipleRegex = regex;
        } else if (TYPE_EMPTY.equals(pattern) || "(#PCDATA)".equals(pattern)) {
        } else {
            String p = pattern.replaceAll(",", "|").replaceAll("\\+|\\*|\\?", "");
            regex = Pattern.compile(p);
            multipleRegex = getMultiplePattern(pattern);
        }
        empty = TYPE_EMPTY.equals(pattern);
    }

    private Pattern getMultiplePattern(String pattern) {
        StringBuilder builder = new StringBuilder();
        Matcher matcher = sp.matcher(pattern);
        while (matcher.find()) {
            builder.append(matcher.group()).append("|");
        }
        if (builder.length() == 0) {
            return null;
        } else {
            builder.deleteCharAt(builder.length() - 1);
            return Pattern.compile(builder.toString());
        }
    }

    @Override
    public boolean allow(String element) {
        return regex == null ? false : regex.matcher(element).matches();
    }

    @Override
    public boolean multipleAllow(String element) {
        return multipleRegex == null ? false : multipleRegex.matcher(element).matches();
    }

    @Override
    public boolean isEmpty() {
        return empty;
    }

    @Override
    public String getPattern() {
        return pattern;
    }

    @Override
    public String getType() {
        return Declare.ELEMENT;
    }

    @Override
    public String getName() {
        return name;
    }
}