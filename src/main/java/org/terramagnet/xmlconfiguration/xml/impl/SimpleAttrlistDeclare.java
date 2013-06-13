/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.terramagnet.xmlconfiguration.xml.impl;

import org.terramagnet.xmlconfiguration.xml.AttributeDeclare;
import org.terramagnet.xmlconfiguration.xml.AttrlistDeclare;
import org.terramagnet.xmlconfiguration.xml.Declare;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author LEE
 */
public final class SimpleAttrlistDeclare implements AttrlistDeclare {

    private String name;
    private Map<String, AttributeDeclare> declares = new HashMap<String, AttributeDeclare>();

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public AttributeDeclare getAttributeDeclare(String attr) {
        return declares.get(attr);
    }

    @Override
    public AttributeDeclare putAttributeDeclare(String attr, AttributeDeclare declare) throws IllegalStateException {
        declare.setElement(name);
        return declares.put(attr, declare);
    }

    @Override
    public int count() {
        return declares.size();
    }

    @Override
    public String getType() {
        return Declare.ATTLIST;
    }
}
