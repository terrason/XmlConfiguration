/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.terramagnet.xmlconfiguration.xml;

import org.dom4j.Document;
import org.terramagnet.xmlconfiguration.configuration.ConfigureException;
import org.terramagnet.xmlconfiguration.xml.impl.ClassPathXmlLoader;
import org.xml.sax.EntityResolver;

/**
 * 配置加载器.
 *
 * <ul>
 *
 * <li>能解析受DTD约束的xml文件。 dtd资源默认采用{@link ClassPathEntityResolver ClassPathEntityResolver}解析，也可{@link #setEntityResolver(org.xml.sax.EntityResolver) 自定义解析器}。</li>
 *
 * <li>能自动从文件加载xml文档，可以指定基xml作为默认配置，应用程序的配置中缺少的节点会从默认配置中继承。</li>
 *
 * <li>xml文件路径必须是类路径表示法，如：{@code /ldap.xml}、{@code /com/yourcompany/myapp/resource/appconfig.xml}.</li>
 *
 * <li>能自动将xml文档反射成JavaBean，具体参见{@link XmlConfiguration#configure() }。</li>
 *
 * </ul>
 *
 * @author terrason
 */
public final class ClassPathXmlConfiguration extends XmlConfiguration {

    private ClassPathXmlLoader loader = new ClassPathXmlLoader();
    private String xml;
    private String baseXml;

    public ClassPathXmlConfiguration() {
    }
    public ClassPathXmlConfiguration(String xml){
        this.xml=xml;
    }
    public ClassPathXmlConfiguration(String baseXml, String xml) {
        this.xml = xml;
        this.baseXml = baseXml;
    }

    @Override
    public Object configure() throws ConfigureException, IllegalStateException {
        if (xml == null) {
            throw new IllegalStateException("尚未设置xml的路径");
        }
        Document document;
        Dtd dtd = null;
        if (baseXml == null) {
            document = loader.load(xml);
        } else {
            Xml x = loader.merge(baseXml, xml);
            document=x.getDocument();
            dtd=x.getDtd();
        }
        setDocument(document);
        setDtd(dtd);

        return super.configure();
    }

    /**
     * 获取默认配置的xml文件位置.
     *
     * @see #setBaseXml(java.lang.String) 设置基础配置
     */
    public String getBaseXml() {
        return baseXml;
    }

    /**
     * 设置默认配置的xml文件位置.
     *
     * <p>基础配置是<em>组件</em>在开发阶段形成的xml文件，内置于jar包中，用于提供默认配置。因此在载入<em>应用程序配置</em>时，
     *
     * 无论是否{@link #setValidate(boolean) 启用验证}，都不会（也没必要）对基础配置文件进行验证。</p>
     *
     * <p>当需要使用<em>默认配置+应用级配置</em>模式时，对应用级配置<strong>必须启用验证</strong>才能知晓具体继承逻辑。
     * 如覆盖、增加（list）等。</p>
     *
     * @param baseXml 默认配置的xml文件类路径
     */
    public void setBaseXml(String baseXml) {
        this.baseXml = baseXml;
    }

    /**
     * 获取xml文件类路径.
     *
     * @return xml文件类路径
     * @see #setXml(java.lang.String) 设置xml文档类路径
     */
    public String getXml() {
        return xml;
    }

    /**
     * 设置xml文档所在路径. 会使用类加载器进行加载，必须使用绝对类路径形式。如{@code /ldap.xml}、{@code /com/suncreate/myapp/resource/appconfig.xml}.
     *
     * @param xml xml文件类路径
     */
    public void setXml(String xml) {
        this.xml = xml;
    }
    /**
     * 获取dtd资源解析器.
     */
    public EntityResolver getEntityResolver() {
        return loader.getEntityResolver();
    }
    /**
     * 设置dtd资源解析器.
     */
    public void setEntityResolver(EntityResolver entityResolver) {
        loader.setEntityResolver(entityResolver);
    }

}
