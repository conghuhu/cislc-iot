package com.shadow.web.model.xml;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName EntityDetail
 * @Description 影子实体属性
 * @Date 2019/6/17 10:30
 * @author szh
 **/
@Data
public class EntityDetail {

    private String tableName;
    private Map<String, String> propertyMap = new HashMap<>();   // 类属性
    private Map<String, DatabaseField> databaseFieldMap = new HashMap<>();  // 类属性与数据库字段映射关系

    public EntityDetail() {
    }

    public EntityDetail(String tableName, Map<String, String> propertyMap, Map<String, DatabaseField> databaseFieldMap) {
        this.tableName = tableName;
        this.propertyMap = propertyMap;
        this.databaseFieldMap = databaseFieldMap;
    }

}
