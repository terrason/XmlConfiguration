/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.terramagnet.xmlconfiguration.xml;

/**
 * 实体声明.
 *
 * @author terrason
 */
public interface EntityDeclare extends Declare {

    /**
     * 实体的值.
     * <p>若实体是外部的，返回{@code publicId|systemId}.</p>
     *
     * @return 实体值
     */
    public String getValue();

    /**
     * 是否是外部实体声明.
     */
    public boolean isExternal();
}
