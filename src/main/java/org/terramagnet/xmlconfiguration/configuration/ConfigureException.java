/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.terramagnet.xmlconfiguration.configuration;

/**
 * 加载配置信息时出现的异常. 包括资源文件异常、Java反射异常以及各种自定义的异常。
 * @author LEE
 */
public class ConfigureException extends RuntimeException {

    private static final String msg = "系统配置信息载入失败";

    public ConfigureException(Throwable thrwbl) {
        super(msg, thrwbl);
    }

    public ConfigureException(String string, Throwable thrwbl) {
        super(msg + "——" + string, thrwbl);
    }

    public ConfigureException(String string) {
        super(msg + "——" + string);
    }

    public ConfigureException() {
        super(msg);
    }
}
