/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.terramagnet.xmlconfiguration.xml;

/**
 * 元素属性列表声明.
 *
 * @author LEE
 */
public interface AttrlistDeclare extends Declare {

    /**
     * 所属元素名称.
     */
    @Override
    public String getName();

    /**
     * 获取单个属性的声明.
     *
     * @return 属性声明
     */
    public AttributeDeclare getAttributeDeclare(String attr);

    /**
     * 添加属性声明. <p>本方法会设置属性声明参数的所属元素。</p>
     *
     * @param attr 属性名称
     * @param declare 属性声明.
     * @return 原属性声明或{@code null}
     * @see AttributeDeclare#setElement(java.lang.String) 设置属性声明所属元素
     */
    public AttributeDeclare putAttributeDeclare(String attr, AttributeDeclare declare);

    /**
     * 声明的属性数量.
     *
     * @return 属性声明个数
     */
    public int count();
}
