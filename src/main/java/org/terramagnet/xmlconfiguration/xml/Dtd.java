/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.terramagnet.xmlconfiguration.xml;

/**
 * 文档类型定义. 用来封装DTD文件内容。
 *
 * @author LEE
 */
public interface Dtd {

    /**
     * 获取某节点的声明. 找不到会返回{@code null}.
     *
     * @param node 节点名称
     * @return 节点的声明或{@code null}
     */
    public Declare getDeclare(String node);

    /**
     * 根据名称获取元素声明. 找不到会返回{@code null}.
     *
     * @param element 元素名称
     * @return 元素声明或{@code null}
     */
    public ElementDeclare getElementDeclare(String element);

    /**
     * 根据元素名称和属性名称获取属性声明. 找不到会返回{@code null}.
     *
     * @param element 元素名称
     * @param attr 属性名称
     * @return 属性声明或{@code null}
     */
    public AttributeDeclare getAttributeDeclare(String element, String attr);

    /**
     * 根据名称获取实体声明. 找不到会返回{@code null}.
     *
     * @param entity 实体名称
     * @return 实体声明或{@code null}
     */
    public EntityDeclare getEntityDeclare(String entity);
}
