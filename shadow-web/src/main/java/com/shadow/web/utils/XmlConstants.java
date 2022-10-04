package com.shadow.web.utils;

import java.io.File;

/**
 * @author szh
 * @InterfaceName XmlConstants
 * @Description xml常量
 * @Date 2019/10/21 10:02
 **/
public interface XmlConstants {

    // xsd路径
    String XSD_PATH = "xmlData" + File.separator + "model.xsd";

    // xml属性字段
    String ATTR_NAME = "name";
    String ATTR_DEVICE = "device";
    String ATTR_TABLE = "table";
    String ATTR_COLUMN = "column";
    String ATTR_TYPE = "type";
    String ATTR_KEY = "key";
    String ATTR_VALUE = "value";

    // xml元素字段
    String ELEMENT_ID = "id";
    String ELEMENT_FIELD = "field";
    String ELEMENT_LIST = "list";
    String ELEMENT_MAP = "map";
    String ELEMENT_VIDEO = "video";

    String BOOLEAN_TRUE = "true";

}
