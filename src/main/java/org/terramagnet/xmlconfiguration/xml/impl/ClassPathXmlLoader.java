/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.terramagnet.xmlconfiguration.xml.impl;

import java.io.InputStream;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.terramagnet.xmlconfiguration.configuration.ConfigureException;
import org.terramagnet.xmlconfiguration.xml.*;
import org.xml.sax.EntityResolver;

/**
 * 类路径xml加载器.
 *
 * @author LEE
 * @see DefaultXml
 */
public class ClassPathXmlLoader implements XmlLoader {

    private EntityResolver entityResolver = new ClassPathEntityResolver();

    /**
     * 使用类加载器来加载指定路径的资源.
     *
     * @param classpath 配置文件的类路径
     */
    @Override
    public Document load(String classpath) {
        InputStream is = ClassPathXmlLoader.class.getResourceAsStream(classpath);
        if (is == null) {
            throw new ConfigureException("找不到指定资源：" + classpath);
        }
        return load(is);
    }

    /**
     * 使用输入流来加载配置.
     *
     * @param is 配置文件的输入流. 本方法会自动关闭该流.
     */
    @Override
    public Document load(InputStream is) {
        SAXReader reader = new SAXReader();
        reader.setEntityResolver(entityResolver);
        try {
            return reader.read(is);
        } catch (DocumentException ex) {
            throw new ConfigureException(ex);
        }
    }

    @Override
    public Xml merge(String base, String xml) {
        DefaultXml dxml = new DefaultXml();
        Document document = load(base);
        Document doc = load(xml);
        DocumentExtender ext = new DocumentExtender();
        ext.setDtd(DtdUtils.createDtd(document.getDocType()));
        ext.extend(document.getDocument(), doc.getDocument());
        dxml.setDocument(document.getDocument());
        dxml.setDtd(ext.getDtd());
        return dxml;
    }

    /**
     * 获取dtd文件加载器.
     */
    public EntityResolver getEntityResolver() {
        return entityResolver;
    }

    /**
     * 设置dtd文件加载器.
     */
    public void setEntityResolver(EntityResolver entityResolver) {
        this.entityResolver = entityResolver;
    }
}
