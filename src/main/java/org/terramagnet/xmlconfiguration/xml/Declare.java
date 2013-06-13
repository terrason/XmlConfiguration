/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.terramagnet.xmlconfiguration.xml;

/**
 *dtd中的声明.
 * @author LEE
 */
public interface Declare {
    public static final String ELEMENT="ELEMENT";
    public static final String ATTLIST="ATTLIST";
    public static final String ATTRBUILTE="ATTRBUILTE";
    public static final String ENTITY="ENTITY";
    public static final String PCDATA="PCDATA";
    public static final String CDATA="CDATA";
    /**
     * 声明类别.
     * @return {@link #ELEMENT}、{@link #ATTLIST}、{@link ATTRBUILTE}或{@link #ENTITY}
     */
    public String getType();
    /**
     * 名称.
     */
    public String getName();
}
