/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.terramagnet.xmlconfiguration.xml;

import org.dom4j.Document;

/**
 *
 * @author LEE
 */
public interface Xml {

    public Document getDocument();

    public Dtd getDtd();
}
