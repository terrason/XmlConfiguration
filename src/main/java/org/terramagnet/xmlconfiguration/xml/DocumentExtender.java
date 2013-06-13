/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.terramagnet.xmlconfiguration.xml;

import java.util.Iterator;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;

public class DocumentExtender {

    private Dtd dtd;

    public Dtd getDtd() {
        return dtd;
    }

    public void setDtd(Dtd dtd) {
        this.dtd = dtd;
    }

    /**
     * 扩充XmlDocument. <p>当两文档的根元素不一样时，不执行任何操作！</p>
     *
     * @param base 基xml，此方法执行后内容会改变。
     * @param doc 需要扩充的内容
     */
    public void extend(Document base, Document doc) {
        Element docRoot = doc.getRootElement();
        Element baseRoot = base.getRootElement();
        String rootName = baseRoot.getName();
        if (rootName.equals(docRoot.getName())) {
            extendElement(baseRoot, docRoot);
        }
    }

    private void extendElement(Element base, Element elem) {
        for (Iterator it = elem.attributeIterator(); it.hasNext();) {
            Attribute attr = (Attribute) it.next();
            String name = attr.getName();
            String value = attr.getValue();
            Attribute baseAttr = base.attribute(name);
            if (baseAttr == null) {
                base.addAttribute(name, value);
            } else {
                baseAttr.setValue(value);
            }
        }
        if (elem.isTextOnly()) {
            base.setText(elem.getText());
        } else {
            for (Iterator it = elem.elementIterator(); it.hasNext();) {
                Element e = (Element) it.next();
                String name = e.getName();
                if (dtd != null) {
                    ElementDeclare elemDecl = dtd.getElementDeclare(base.getName());
                    if (elemDecl != null && elemDecl.multipleAllow(name)) {
                        base.add(e.createCopy());
                        continue;
                    }
                }
                Element baseElement = base.element(name);
                if (baseElement == null) {
                    baseElement = e.createCopy();
                    base.add(baseElement);
                }
                extendElement(baseElement, e);
            }
        }
    }
}
