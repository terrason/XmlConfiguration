/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.terramagnet.xmlconfiguration.xml;

import org.terramagnet.xmlconfiguration.configuration.Configuration;
import org.terramagnet.xmlconfiguration.configuration.ConfigureException;
import org.terramagnet.xmlconfiguration.configuration.Preparable;
import org.terramagnet.xmlconfiguration.configuration.util.StringUtils;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.regex.Pattern;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;

/**
 * 从xml读取配置到JavaBean.
 * 
 * <p>该类实现了{@link Configuration 配置接口}，但在载入配置前必须包含必要信息，如{@link #setDocument(org.dom4j.Document) 文档}、{@link #setDtd(org.terramagnet.xmlconfiguration.xml.Dtd) dtd}等。</p>
 *
 * @author LEE
 */
public class XmlConfiguration implements Configuration {

    private static final Collection TEST_LIST = new ArrayList();
    private static final Collection TEST_SET = new HashSet();
    private static final Map TEST_MAP = new HashMap();
    private static final Pattern pattern = Pattern.compile("(\\w+\\.)+\\p{Upper}\\w+");
    private static final String DEFAULT_VALUE_KEY = "value";
    private static final String IMPLEMENT_ATTRIBUTENAME = "instance";
    private static final Collection<Class> primaryClasses = new HashSet<Class>();

    static {
        primaryClasses.add(String.class);
        primaryClasses.add(Boolean.class);
        primaryClasses.add(Integer.class);
        primaryClasses.add(Long.class);
        primaryClasses.add(Float.class);
        primaryClasses.add(Double.class);
        primaryClasses.add(Byte.class);
        primaryClasses.add(Short.class);
        primaryClasses.add(Character.class);
    }
    //---------------------------------------------------------------------------
    protected final BeanUtilsBean beanUtils = new BeanUtilsBean();
    private Document document;
    private Dtd dtd;

    /**
     * xml文档.
     */
    public Document getDocument() {
        return document;
    }

    /**
     * 设置xml文档. 在调用{@link #configure() }前若尚未设置xml文档，会抛出{@link IllegalStateException}异常
     *
     * @param document xml文档
     */
    public void setDocument(Document document) {
        this.document = document;
    }

    /**
     * xml文档结构定义.
     */
    public Dtd getDtd() {
        return dtd;
    }

    /**
     * 定义xml文档结构.
     */
    public void setDtd(Dtd dtd) {
        this.dtd = dtd;
    }

    /**
     * 将{@link #getDocument() document}表示的xml文档转化为Java对象.
     *
     * <ol><li>若xml的根节点配置了{@code instance}属性，则使用{@code instance}属性表示的类创建配置实例，最终返回这个实例。</li>
     * <ul>
     *
     * <li>{@code instance}属性所表示的JavaBean必须与xml文档结构一致，若xml文档中某节点具有多个相同子节点，JavaBean中应使用Collection或Map.</li>
     *
     * <li>对于集合（包括Map），推荐在声明时使用泛型来规定元素类型，但元素若配置了{@code instance}属性，还是会优先使用该属性创建实例。</li>
     *
     * <li>当使用Map存储集合类元素时，默认使用元素的<strong>第一个属性</strong>作为键值。</li>
     *
     * </ul>
     *
     * <li>若xml的根节点没有配置{@code instance}属性，则必须{@link #setDtd(org.terramagnet.xmlconfiguration.xml.Dtd) 定义xml文档结构}，系统使用该定义来创建Map实例存放配置信息。</li>
     *
     * </ol>
     *
     * @return JavaBean或Map
     * @throws ConfigureException 转换失败
     * @throws IllegalStateException 尚未设置xml文档或dtd文件未设置
     *
     * @see #setDocument(org.dom4j.Document) 设置xml文档
     * @see #setDtd(org.terramagnet.xmlconfiguration.xml.Dtd) 设置dtd文件
     */
    @Override
    public Object configure() throws ConfigureException, IllegalStateException {
        if (document == null) {
            throw new IllegalStateException("尚未载入配置文件");
        }
        Element root = document.getRootElement();
        String className = root.attributeValue(IMPLEMENT_ATTRIBUTENAME);
        if (className == null) {
            if (dtd == null) {
                throw new IllegalStateException("尚未定义xml文档结构");
            }
            Map map = new HashMap();
            parse2Map(root, map);
            return map;
        } else {
            try {
                Class<?> beanClass = Class.forName(className);
                Object bean = beanClass.newInstance();
                parse2Bean(root, bean);
                return bean;
            } catch (InstantiationException ex) {
                throw new ConfigureException("无法实例化配置信息载体类", ex);
            } catch (IllegalAccessException ex) {
                throw new ConfigureException("无法实例化配置信息载体类", ex);
            } catch (ClassNotFoundException ex) {
                throw new ConfigureException("找不到配置信息载体类", ex);
            }
        }
    }

    private void parse2Map(Element element, Map map) throws ConfigureException {
        for (Iterator it = element.attributeIterator(); it.hasNext();) {
            Attribute attr = (Attribute) it.next();
            String name = attr.getName();
            String value = attr.getValue();
            map.put(name, value);
        }
        if (element.isTextOnly()) {
            String value = element.getText();
            if (StringUtils.hasText(value)) {
                map.put(DEFAULT_VALUE_KEY, value);
            }
            return;
        }
        for (Iterator it = element.elementIterator(); it.hasNext();) {
            Element e = (Element) it.next();
            String propertyName = e.getName();
            ElementDeclare elemDecl = dtd.getElementDeclare(element.getName());
            if (elemDecl != null && elemDecl.multipleAllow(propertyName)) {
                List propertyValue = (List) map.get(propertyName);
                if (propertyValue == null) {
                    propertyValue = new ArrayList();
                    map.put(propertyName, propertyValue);
                }
                if (e.isTextOnly() && e.attributeCount() == 0) {
                    propertyValue.add(e.getData());
                } else {
                    Map newMap = new HashMap();
                    propertyValue.add(newMap);
                    parse2Map(e, newMap);
                }
            } else {
                if (e.isTextOnly() && e.attributeCount() == 0) {
                    map.put(propertyName, e.getData());
                } else {
                    Map newMap = new HashMap();
                    map.put(propertyName, newMap);
                    parse2Map(e, newMap);
                }
            }
        }
    }

    private void parse2Bean(Element element, Object bean) throws ConfigureException {
        try {
            for (Iterator it = element.attributeIterator(); it.hasNext();) {
                Attribute attr = (Attribute) it.next();
                String name = attr.getName();
                Object value = attr.getData();
                String propertyName = javaName(name);
                if ("class".equals(propertyName)) {
                    propertyName = "clazz";
                }
                beanUtils.copyProperty(bean, propertyName, value);
            }
            if (element.isTextOnly()) {
                String value = element.getText();
                if (StringUtils.hasText(value)) {
                    beanUtils.copyProperty(bean, DEFAULT_VALUE_KEY, value);
                }
                return;
            }
            for (Iterator it = element.elementIterator(); it.hasNext();) {
                Element e = (Element) it.next();
                String name = e.getName();
                String propertyName = "class".equals(name) ? "clazz" : javaName(name);
                Object propertyValue = beanUtils.getPropertyUtils().getSimpleProperty(bean, propertyName);
                Field propertyField = getFieldDeeply(bean.getClass(), propertyName);
                if (propertyField != null) {
                    Class<?> propertyType = propertyField.getType();
                    Type genericType = propertyField.getGenericType();
                    if (propertyType.isInstance(TEST_LIST)) {
                        //<editor-fold defaultstate="collapsed" desc="List型处理">
                        Class elementType = getImplClass(e);
                        if (elementType == null && genericType instanceof ParameterizedType) {
                            ParameterizedType parameters = (ParameterizedType) genericType;
                            elementType = getComponentClass(parameters, 0);
                        }
                        if (elementType == null) {
                            throw new ConfigureException("无法确定节点 " + e + " 的实现类");
                        }
                        if (propertyValue == null) {
                            propertyValue = new ArrayList();
                            beanUtils.copyProperty(bean, propertyName, propertyValue);
                        }
                        parse2Collection(e, (Collection) propertyValue, elementType);
                        //</editor-fold>
                    } else if (propertyType.isInstance(TEST_SET)) {
                        //<editor-fold defaultstate="collapsed" desc="Set型处理">
                        Class elementType = getImplClass(e);
                        if (elementType == null && genericType instanceof ParameterizedType) {
                            ParameterizedType parameters = (ParameterizedType) genericType;
                            elementType = getComponentClass(parameters, 0);
                        }
                        if (elementType == null) {
                            throw new ConfigureException("无法确定节点 " + e + " 的实现类");
                        }
                        if (propertyValue == null) {
                            propertyValue = new HashSet();
                            beanUtils.copyProperty(bean, propertyName, propertyValue);
                        }
                        parse2Collection(e, (Collection) propertyValue, elementType);
                        //</editor-fold>
                    } else if (propertyType.isInstance(TEST_MAP)) {
                        //<editor-fold defaultstate="collapsed" desc="Map型处理">
                        Class elementType = getImplClass(e);
                        if (elementType == null && genericType instanceof ParameterizedType) {
                            ParameterizedType parameters = (ParameterizedType) genericType;
                            elementType = getComponentClass(parameters, 1);
                        }
                        if (elementType == null) {
                            throw new ConfigureException("无法确定节点 " + e + " 的实现类");
                        }
                        if (propertyValue == null) {
                            propertyValue = new HashMap();
                            beanUtils.copyProperty(bean, propertyName, propertyValue);
                        }
                        parse2Map(e, (Map) propertyValue, elementType);
                        //</editor-fold>
                    } else if (isSimpleClass(propertyType)) {
                        beanUtils.copyProperty(bean, propertyName, e.getData());
                    } else {
                        if (propertyValue == null) {
                            Class elementType = getImplClass(e);
                            Object newInstance = elementType == null ? propertyField.getType().newInstance() : elementType.newInstance();
                            propertyValue = newInstance;
                            beanUtils.copyProperty(bean, propertyName, propertyValue);
                        }
                        parse2Bean(e, propertyValue);
                        if (propertyValue instanceof Preparable) {
                            ((Preparable) propertyValue).prepare();
                        }
                    }
                }
            }
        } catch (SecurityException ex) {
            throw new ConfigureException(ex);
        } catch (InvocationTargetException ex) {
            throw new ConfigureException(ex);
        } catch (NoSuchMethodException ex) {
            throw new ConfigureException(ex);
        } catch (InstantiationException ex) {
            throw new ConfigureException(ex);
        } catch (IllegalAccessException ex) {
            throw new ConfigureException(ex);
        } catch (Exception ex) {
            throw new ConfigureException(ex);
        }
    }

    /**
     * 获取类的属性字段. 当找不到属性时会去查找其所有祖先类所声明的属性。
     *
     * @param bean javabean对象的实现类
     * @param fieldName 属性名称
     * @return 属性或{@code null}
     */
    private Field getFieldDeeply(Class bean, String fieldName) {
        Field[] declaredFields = bean.getDeclaredFields();
        for (int i = 0; i < declaredFields.length; i++) {
            Field declaredField = declaredFields[i];
            if (fieldName.equals(declaredField.getName())) {
                return declaredField;
            }
        }
        Class superclass = bean.getSuperclass();
        return superclass == null ? null : getFieldDeeply(superclass, fieldName);
    }

    private Class getComponentClass(ParameterizedType type, int i) {
        ParameterizedType parameters = type;
        Type t = parameters.getActualTypeArguments()[i];
        try {
            return (Class) t;
        } catch (ClassCastException ex) {
            throw new ConfigureException("不支持嵌套泛型：" + t, ex);
        }
    }

    private Object getPrimaryKey(Element element) throws ConfigureException {
        Object key;
        try {
            key = element.attribute(0).getData();
        } catch (NullPointerException ex) {
            throw new ConfigureException("节点 " + element + " 缺少键值");
        }
        if (key == null) {
            throw new ConfigureException("节点 " + element + " 缺少键值");
        }
        return key;
    }

    private Class getImplClass(Element element) throws ConfigureException {
        Attribute attribute = element.attribute(IMPLEMENT_ATTRIBUTENAME);
        if (attribute == null) {
            return null;
        }
        String key = attribute.getValue();
        if (pattern == null || !pattern.matcher(key).matches()) {
            return null;
        }
        try {
            return key == null ? null : Class.forName(key);
        } catch (ClassNotFoundException ex) {
            throw new ConfigureException("实例类找不到："+key,ex);
        }
    }

    private void parse2Collection(Element element, Collection collection, Class elementType) throws InstantiationException, IllegalAccessException {
        if (isSimpleClass(elementType)) {
            collection.add(element.getData());
        } else {
            Object newInstance = elementType.newInstance();
            collection.add(newInstance);
            parse2Bean(element, newInstance);
        }
    }

    private void parse2Map(Element element, Map map, Class valueType) throws InstantiationException, IllegalAccessException {
        if (isSimpleClass(valueType)) {
            map.put(getPrimaryKey(element), element.getData());
        } else {
            Object newInstance = valueType.newInstance();
            map.put(getPrimaryKey(element), newInstance);
            parse2Bean(element, newInstance);
        }
    }

    private String javaName(String str) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '-') {
                i++;
                sb.append(Character.toUpperCase(str.charAt(i)));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private boolean isSimpleClass(Class cls) {
        return cls.isPrimitive() ? true : primaryClasses.contains(cls);
    }
}
