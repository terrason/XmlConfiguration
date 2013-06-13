/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.terramagnet.xmlconfiguration.xml.impl;

import org.terramagnet.xmlconfiguration.xml.Dtd;
import org.terramagnet.xmlconfiguration.xml.Xml;
import org.dom4j.Document;

/**
 *
 * @author terrason
 */
public class DefaultXml implements Xml {

    private Document document;
    private Dtd dtd;

    @Override
    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    @Override
    public Dtd getDtd() {
        return dtd;
    }

    public void setDtd(Dtd dtd) {
        this.dtd = dtd;
    }
}
