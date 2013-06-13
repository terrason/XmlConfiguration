/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.terramagnet.xmlconfiguration.xml.impl;

import org.terramagnet.xmlconfiguration.xml.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Dtd接口的简单实现. 采用3个HashMap存储声明。
 *
 * @see AttrlistDeclare
 * @author terrason
 */
public final class SimpleDtd implements Dtd {

    private Map<String, ElementDeclare> elementDeclares = new HashMap<String, ElementDeclare>();
    private Map<String, AttrlistDeclare> attrlistDeclares = new HashMap<String, AttrlistDeclare>();
    private Map<String, EntityDeclare> entityDeclares = new HashMap<String, EntityDeclare>();

    /**
     * 添加元素声明.
     *
     * @param element 元素名称
     * @param declare 元素声明
     * @return 元素原本的声明或{@code null}
     */
    public ElementDeclare putDeclare(String element, ElementDeclare declare) {
        return elementDeclares.put(element, declare);
    }

    /**
     * 添加实体声明.
     *
     * @param entity 实体名称
     * @param declare 实体声明
     * @return 实体原本的声明或{@code null}
     */
    public EntityDeclare putDeclare(String entity, EntityDeclare declare) {
        return entityDeclares.put(entity, declare);
    }

    /**
     * 添加元素属性列表声明.
     *
     * @param element 元素名称
     * @param declare 属性列表声明
     * @return 元素原本的属性列表声明或{@code null}
     */
    public AttrlistDeclare putDeclare(String element, AttrlistDeclare declare) {
        return attrlistDeclares.put(element, declare);
    }

    /**
     * 添加元素的属性声明. 若指定的元素尚未声明任何属性，会使用{@link SimpleAttrlistDeclare}创建新实例。
     *
     * @param element 元素名称
     * @param attribute 属性名称
     * @param declare 属性声明.
     * @return 元素原本所具有的属性声明或{@code null}
     * @throws IllegalArgumentException 找不到指定元素声明
     */
    public AttributeDeclare putDeclare(String element, String attribute, AttributeDeclare declare) throws IllegalArgumentException {
        if (elementDeclares.get(element) == null) {
            throw new IllegalArgumentException("找不到元素声明：" + element);
        }
        AttrlistDeclare attrlistDeclare = attrlistDeclares.get(element);
        if (attrlistDeclare == null) {
            attrlistDeclare = new SimpleAttrlistDeclare();
        }
        return attrlistDeclare.putAttributeDeclare(attribute, declare);
    }

    @Override
    public Declare getDeclare(String node) {
        Declare d = elementDeclares.get(node);
        if (d == null) {
            d = entityDeclares.get(node);
        }
        if (d == null) {
            for (Iterator<Entry<String, AttrlistDeclare>> it = attrlistDeclares.entrySet().iterator(); it.hasNext();) {
                AttrlistDeclare declare = it.next().getValue();
                d = declare.getAttributeDeclare(node);
                if (d != null) {
                    return d;
                }
            }
        }
        return d;
    }

    @Override
    public ElementDeclare getElementDeclare(String element) {
        return elementDeclares.get(element);
    }

    @Override
    public AttributeDeclare getAttributeDeclare(String element, String attr) {
        AttrlistDeclare dc = attrlistDeclares.get(element);
        return dc == null ? null : dc.getAttributeDeclare(attr);
    }

    @Override
    public EntityDeclare getEntityDeclare(String entity) {
        return entityDeclares.get(entity);
    }
}
