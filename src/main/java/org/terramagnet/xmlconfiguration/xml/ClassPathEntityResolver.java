/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.terramagnet.xmlconfiguration.xml;

import java.io.IOException;
import java.io.InputStream;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * 基本xml dtd 查找器.
 *
 * <p>能从{@code systemId}URL中解析出DOCTYPE声明dtd文件的实际地址。
 *
 * 若{@code xml doctype}声明中{@code systemId}为
 * {@code "http://www.yourcompany.com/dtds/ldap.dtd"}， 则dtd实体文件 的查找路径为
 * {@code "/com/yourcompany/dtds/ldap.dtd".
 * </p>
 *
 * @author LEE
 */
public class ClassPathEntityResolver implements EntityResolver {

    private static final char slash = '/';

    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        InputSource source = null; // returning null triggers default behavior
        if (systemId != null) {
            int i0 = systemId.indexOf("://") + 3;
            int i1 = systemId.indexOf('/', i0);
            String[] hosts;
            try {
                hosts = systemId.substring(i0, i1).split("\\.");
            } catch (IndexOutOfBoundsException e) {
                return null;
            }
            StringBuilder classpathBuilder = new StringBuilder();
            for (int i = 1; i <= hosts.length && i<3;i++) {
                classpathBuilder.append(slash).append(hosts[hosts.length-i]);
            }
            classpathBuilder.append(slash).append(systemId.substring(i1 + 1));
            InputStream dtdStream = this.getClass().getResourceAsStream(classpathBuilder.toString());
            if (dtdStream != null) {
                source = new InputSource(dtdStream);
                source.setPublicId(publicId);
                source.setSystemId(systemId);
            }
        }
        return source;
    }
}
