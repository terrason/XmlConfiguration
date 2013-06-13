/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.terramagnet.xmlconfiguration.xml;

/**
 * 单个属性声明.
 *
 * @author terrason
 */
public interface AttributeDeclare extends Declare {

    public static final String REQUIRED = "#REQUIRED";
    public static final String IMPLIED = "#IMPLIED";
    public static final String FIXED = "#FIXED";

    /**
     * 设置属性所属元素名称.
     *
     * @param element 所属元素名称
     */
    public void setElement(String element);

    /**
     * 属性所属元素名称.
     */
    public String getElement();

    /**
     * 属性声明值的模式.
     */
    public String getPattern();

    /**
     * 属性默认值.
     */
    public String getDefaultValue();

    /**
     * 属性值说明.
     *
     * @return {@link #REQUIRED}、{@link #IMPLIED}或{@link #FIXED}.
     */
    public String getValueType();
}
