package com.shadow.web.model.result;

import com.shadow.web.model.product.Product;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Auther: wangzhendong
 * @Date: 2019/10/31 14:02
 * @Description:
 */
@Data
public class ProductInfo {

    private Integer id;

    private String name;

    private String fileUrl;

    private Integer deviceId;

    private String deviceName;

    private String encryption;

    private String operateSystem;

    private Integer protocolSize;

    private Integer serverSize;

    private String description;

    private Date createTime;

    private Date updateTime;

    private Integer deleted;

    private List<String> protocolList;

    private List<String> serverList;

    public ProductInfo(){}

}
