/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.terramagnet.xmlconfiguration.xml;

/**
 * 元素声明.
 *
 * @author LEE
 */
public interface ElementDeclare extends Declare {

    public static final String TYPE_EMPTY = "EMPTY";
    public static final String TYPE_ANY = "ANY";

    /**
     * 是否允许元素.
     *
     * @param element 元素名称
     * @return 内容中允许指定名称的元素时，返回{@code true}，否则返回{@code false}。
     */
    public boolean allow(String element);

    /**
     * 是否允许多个指定的元素.
     *
     * @param element 元素名称
     * @return 内容中允许多个指定名称的元素时，返回{@code true}，否则返回{@code false}。
     */
    public boolean multipleAllow(String element);

    /**
     * 元素内容为{@link #TYPE_EMPTY}.
     *
     * @return {@code true}表示元素不含任何内容，是自关闭标签。
     */
    public boolean isEmpty();

    /**
     * 模式.
     */
    public String getPattern();
}
