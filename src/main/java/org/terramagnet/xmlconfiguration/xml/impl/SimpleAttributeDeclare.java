/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.terramagnet.xmlconfiguration.xml.impl;

import org.terramagnet.xmlconfiguration.xml.AttributeDeclare;
import org.terramagnet.xmlconfiguration.xml.Declare;

/**
 *
 * @author LEE
 */
public class SimpleAttributeDeclare implements AttributeDeclare {

    private String element;
    private String pattern;
    private String name;
    private String defaultValue;
    private String valueType;

    @Override
    public String getElement() {
        return element;
    }

    @Override
    public void setElement(String element) {
        this.element = element;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    @Override
    public String getType() {
        return Declare.ATTRBUILTE;
    }
}
