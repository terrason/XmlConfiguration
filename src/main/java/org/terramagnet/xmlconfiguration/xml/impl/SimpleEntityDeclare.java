/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.terramagnet.xmlconfiguration.xml.impl;

import org.terramagnet.xmlconfiguration.xml.Declare;
import org.terramagnet.xmlconfiguration.xml.EntityDeclare;

/**
 *
 * @author terrason
 */
public final class SimpleEntityDeclare implements EntityDeclare {

    private String value;
    private String name;
    private boolean external = true;

    /**
     * {@inheritDoc }. <p>默认为外部实体声明。</p>
     */
    @Override
    public boolean isExternal() {
        return external;
    }

    /**
     * 设置是否是外部实体. 默认为外部实体。
     *
     * @param external {@code true}表示外部实体，{@code false}表示内部实体。
     */
    public void setExternal(boolean external) {
        this.external = external;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String getType() {
        return Declare.ENTITY;
    }
}
