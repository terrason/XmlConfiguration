/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.terramagnet.xmlconfiguration.configuration;

/**
 * 所有系统配置的接口. 
 *
 * @author LEE
 */
public interface Configuration {

    /**
     * 加载配置.
     *
     * @return 配置信息.
     * @throws ConfigureException 加载配置信息出错
     */
    public Object configure() throws ConfigureException;
}
