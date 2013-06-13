/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.terramagnet.xmlconfiguration.xml;

import org.terramagnet.xmlconfiguration.configuration.ConfigureException;
import java.io.InputStream;
import org.dom4j.Document;

/**
 * xml配置文件载入器.
 *
 * @author LEE
 */
public interface XmlLoader {

    /**
     * 根据配置文件路径来加载配置.
     *
     * @param xml 配置文件地址
     * @return xml文档对象
     * @throws ConfigureException 配置文件加载出错
     */
    public Document load(String xml) throws ConfigureException;

    /**
     * 使用输入流来加载配置.
     *
     * @param is 配置文件的输入流. 本方法会自动关闭该流。
     * @return xml文档对象
     * @throws ConfigureException 配置文件加载出错
     */
    public Document load(InputStream is) throws ConfigureException;

    /**
     * 根据配置文件的类路径来加载配置. 会将两个配置文件的内容合并，同时对第二个xml文件启用验证。
     *
     * @param base 基础配置文件
     * @param xml 应用程序配置文件
     * @return 合并后的xml文档
     * @throws ConfigureException 配置文件加载出错
     */
    public Xml merge(String base, String xml) throws ConfigureException;
}
