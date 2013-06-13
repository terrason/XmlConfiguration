/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.terramagnet.xmlconfiguration.xml;

import org.terramagnet.xmlconfiguration.xml.impl.SimpleAttributeDeclare;
import org.terramagnet.xmlconfiguration.xml.impl.SimpleDtd;
import org.terramagnet.xmlconfiguration.xml.impl.SimpleElementDeclare;
import org.terramagnet.xmlconfiguration.xml.impl.SimpleEntityDeclare;
import java.util.Iterator;
import java.util.List;
import org.dom4j.DocumentType;
import org.dom4j.dtd.AttributeDecl;
import org.dom4j.dtd.ElementDecl;
import org.dom4j.dtd.ExternalEntityDecl;
import org.dom4j.dtd.InternalEntityDecl;

/**
 *
 * @author terrason
 */
public final class DtdUtils {

    public static SimpleDtd createDtd(DocumentType docType) {
        SimpleDtd dtd = new SimpleDtd();
        List internalDeclarations = docType.getInternalDeclarations();
        if (internalDeclarations != null) {
            fillDtd(dtd, internalDeclarations);
        }
        List externalDeclarations = docType.getExternalDeclarations();
        if (externalDeclarations != null) {
            fillDtd(dtd, externalDeclarations);
        }
        return dtd;
    }

    private static void fillDtd(SimpleDtd dtd, List declarations) {
        for (Iterator it = declarations.iterator(); it.hasNext();) {
            Object o = it.next();
            if (o instanceof ElementDecl) {
                ElementDecl declare = (ElementDecl) o;
                SimpleElementDeclare d = new SimpleElementDeclare();
                d.setName(declare.getName());
                d.setPattern(declare.getModel());
                dtd.putDeclare(d.getName(), d);
            } else if (o instanceof AttributeDecl) {
                AttributeDecl declare = (AttributeDecl) o;
                SimpleAttributeDeclare d = new SimpleAttributeDeclare();
                d.setElement(declare.getElementName());
                d.setName(declare.getAttributeName());
                d.setPattern(declare.getType());
                d.setDefaultValue(declare.getValue());
                d.setValueType(declare.getValueDefault());
                if (d.getValueType() != null && d.getValueType().startsWith(AttributeDeclare.FIXED)) {
                    d.setDefaultValue(d.getValueType().substring(AttributeDeclare.FIXED.length() + 1));
                    d.setValueType(AttributeDeclare.FIXED);
                }
                dtd.putDeclare(d.getElement(), d.getName(), d);
            } else if (o instanceof ExternalEntityDecl) {
                ExternalEntityDecl declare = (ExternalEntityDecl) o;
                SimpleEntityDeclare d = new SimpleEntityDeclare();
                d.setExternal(true);
                d.setName(declare.getName());
                d.setValue(declare.getPublicID() + "|" + declare.getSystemID());
                dtd.putDeclare(d.getName(), d);
            } else if (o instanceof InternalEntityDecl) {
                InternalEntityDecl declare = (InternalEntityDecl) o;
                SimpleEntityDeclare d = new SimpleEntityDeclare();
                d.setExternal(false);
                d.setName(declare.getName());
                d.setValue(declare.getValue());
                dtd.putDeclare(d.getName(), d);
            }
        }
    }
}
