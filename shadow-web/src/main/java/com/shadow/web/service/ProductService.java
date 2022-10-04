package com.shadow.web.service;

import com.google.common.base.CaseFormat;
import com.shadow.web.mapper.device.DeviceMapper;
import com.shadow.web.mapper.product.ProductListMapper;
import com.shadow.web.mapper.product.ProductMapper;
import com.shadow.web.mapper.product.ProductProtocolMapper;
import com.shadow.web.mapper.product.ProductServerMapper;
import com.shadow.web.model.device.Device;
import com.shadow.web.model.device.DeviceProp;
import com.shadow.web.model.device.DevicePropChild;
import com.shadow.web.model.enums.DevicePropEnum;
import com.shadow.web.model.product.*;
import com.shadow.web.model.result.ProductInfo;
import com.shadow.web.model.result.Result;
import com.cislc.shadow.utils.enums.Encryption;
import com.cislc.shadow.utils.enums.Protocol;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.*;

/**
 * @author wangzhendong
 * @since 2019/10/28 12:33
 */
@Slf4j
@Service
public class ProductService {

    @Resource
    private ProductMapper productMapper;
    @Resource
    private ProductListMapper productListMapper;
    @Resource
    private ProductProtocolMapper productProtocolMapper;
    @Resource
    private ProductServerMapper productServerMapper;
    @Resource
    private DeviceMapper deviceMapper;
    @Autowired
    private PackageService packageService;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private FileService fileService;

    //增
    @Transactional(rollbackFor = Exception.class)
    public Result<?> insertProduct(Product product, List<String> protocolList, List<String> serverList) {
        int ret = productMapper.insertSelective(product);
        if (ret == -1) {
            return Result.returnError("新增产品失败");
        }
        for (String protocol : protocolList) {
            ProductProtocol productProtocol = new ProductProtocol(product.getId(), protocol);
            int protocolRet = productProtocolMapper.insert(productProtocol);
            if (protocolRet == -1) {
                return Result.returnError("新增产品失败");
            }
        }
        for (String server : serverList) {
            ProductServer productServer = new ProductServer(product.getId(), server);
            int serverRet = productServerMapper.insert(productServer);
            if (serverRet == -1) {
                return Result.returnError("新增产品失败");
            }
        }
        return Result.returnSuccess();
    }

    //删
    @Transactional(rollbackFor = Exception.class)
    public Result<?> deleteProduct(Integer id) {
        //删除中间表的协议
        ProductProtocolExample productProtocolExample = new ProductProtocolExample();
        productProtocolExample.createCriteria().andProductIdEqualTo(id);
        int protocolRet = productProtocolMapper.deleteByExample(productProtocolExample);
        //公共服务信息
        ProductServerExample productServerExample = new ProductServerExample();
        productServerExample.createCriteria().andProductIdEqualTo(id);
        int serverRet = productServerMapper.deleteByExample(productServerExample);
        //
        int ret = productMapper.deleteByPrimaryKey(id);
        if (ret == -1 || protocolRet == -1 || serverRet == -1) {
            return Result.returnError("删除产品失败");
        }
        return Result.returnSuccess();
    }

    //改
    @Transactional(rollbackFor = Exception.class)
    public Result<?> updateProduct(Product product, List<String> protocolList, List<String> serverList) {
        //删
        ProductProtocolExample productProtocolExample = new ProductProtocolExample();
        productProtocolExample.createCriteria().andProductIdEqualTo(product.getId());
        productProtocolMapper.deleteByExample(productProtocolExample);
        //公共服务信息
        ProductServerExample productServerExample = new ProductServerExample();
        productServerExample.createCriteria().andProductIdEqualTo(product.getId());
        productServerMapper.deleteByExample(productServerExample);
        //增
        for (String protocol : protocolList) {
            ProductProtocol productProtocol = new ProductProtocol(product.getId(), protocol);
            productProtocolMapper.insert(productProtocol);
        }
        for (String server : serverList) {
            ProductServer productServer = new ProductServer(product.getId(), server);
            productServerMapper.insert(productServer);
        }
        //改
        int ret = productMapper.updateByPrimaryKeySelective(product);
        if (ret == -1) {
            return Result.returnError("更新产品失败");
        }
        return Result.returnSuccess();
    }

    //查
    public Result<List<ProductInfo>> selectProduct() {
        return Result.returnSuccess(productListMapper.select(null));
    }

    /**
     * @Description 产品打包
     * @param productId 产品id
     * @author szh
     * @Date 2019/11/8 9:00
     */
    public void packageProduct(int productId,
                               HttpServletRequest request,
                               HttpServletResponse response) {
        // 查询产品
        List<ProductInfo> productInfoList = productListMapper.select(productId);
        if (productInfoList.isEmpty()) {
            return;
        }
        ProductInfo product = productInfoList.get(0);
        // 协议
        Protocol protocol = Protocol.getByName(product.getProtocolList().get(0));
        // 加密算法
        Encryption encryption = Encryption.getEncryption(product.getEncryption());
        // jar包名
        String jarName = product.getName() + ".zip";
        // xml文件
        File xmlFile = getXmlFile(product);
        if (xmlFile == null) {
            log.error("打包失败：xml解析失败，jarName={}，protocol={}", jarName, product);
            return;
        }

        long current = System.currentTimeMillis();
        log.info("打包开始：jarName={}, protocol={}, start={}", jarName, product, current);
        packageService.packageJar(Collections.singletonList(xmlFile), protocol, encryption, jarName, request, response);
        log.info("打包结束：用时={}", System.currentTimeMillis() - current);
    }

    /**
     * 获取xml文件
     */
    public File getXmlFile(ProductInfo product) {
        String fileUrl;
        // xml是否已经存在
        if (StringUtils.isBlank(product.getFileUrl())) {
            log.info("获取xml文件: 构建产品xml, productId={}", product.getId());
            // 构建并写入xml
            String xmlStr = buildXmlProductStr(product.getId());
            String xmlName = System.currentTimeMillis() + ".xml";
            fileUrl = fileService.saveFile(xmlStr, xmlName);
            if (StringUtils.isEmpty(fileUrl)) {
                return null;
            }

            // 更新xml路径
            Product p = new Product();
            p.setId(product.getId());
            p.setFileUrl(fileUrl);
            productMapper.updateByPrimaryKeySelective(p);
        } else {
            fileUrl = product.getFileUrl();
        }

        log.info("获取xml文件: fileUrl={}", fileUrl);
        File xmlFile = new File(fileUrl);
        return xmlFile.exists() ? xmlFile : null;
    }

    /**
     * 创建产品xml内容
     * @param productId 产品id
     * @return xml内容
     */
    public String buildXmlProductStr(int productId) {
        List<ProductInfo> productInfoList = this.productListMapper.select(productId);
        if (productInfoList.isEmpty()) {
            return null;
        }

        ProductInfo product = productInfoList.get(0);
        Device device = deviceMapper.selectByPrimaryKey(product.getDeviceId());
        Result<List<DeviceProp>> propRes = this.deviceService.selectDeviceProp(product.getDeviceId());
        List<DeviceProp> deviceProps = propRes.value();

        Map<String, String> propMap = new HashMap<>(deviceProps.size());
        List<DeviceProp> structProp = new ArrayList<>();
        List<DeviceProp> videoProp = new ArrayList<>();

        for (DeviceProp deviceProp : deviceProps) {
            if ("struct".equals(deviceProp.getConstruction())) {
                structProp.add(deviceProp);
            } else if ("video".equals(deviceProp.getConstruction())) {
                videoProp.add(deviceProp);
            } else {
                DevicePropEnum propEnum = DevicePropEnum.getEnum(deviceProp.getConstruction());
                propMap.put(deviceProp.getStructName(), propEnum.getXmlConstruction());
            }
        }

        // 构建xml
        String tableName = this.getTableName(device.getDeviceTag());
        StringBuffer xmlSb = new StringBuffer();
        xmlSb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
                .append("<classes>\n").append("\t<class name=\"")
                .append(product.getName())
                .append("\" table=\"")
                .append(tableName)
                .append("\" device=\"true\">\n");

        // 普通属性
        buildXmlProps(xmlSb, propMap, structProp, videoProp, tableName);
        xmlSb.append("\t</class>\n");

        // 结构体属性
        buildXmlStruct(xmlSb, structProp);

        xmlSb.append("</classes>\n");

        return xmlSb.toString();
    }

    /**
     * 构建xml属性
     */
    private void buildXmlProps(StringBuffer xmlSb, Map<String, String> propMap, List<DeviceProp> structProp, List<DeviceProp> videoProp, String tableName) {
        // 普通属性
        for (Map.Entry<String, String> entry : propMap.entrySet()) {
            xmlSb.append(
                    String.format("\t\t<field type=\"%s\" table=\"%s\" column=\"%s\">%s</field>\n",
                            entry.getValue(), tableName, entry.getKey(), entry.getKey())
            );
        }

        // 结构体属性
        for (DeviceProp deviceProp : structProp) {
            xmlSb.append(
                    String.format("\t\t<list type=\"%s\" table=\"%s\">%s</list>\n",
                            getEntityName(deviceProp.getStructName()),
                            getTableName(deviceProp.getStructName()),
                            deviceProp.getStructName() + "List")
            );
        }

        // 视频属性
        for (DeviceProp deviceProp : videoProp) {
            xmlSb.append("\t\t<video>").append(deviceProp.getStructName()).append("</video>\n");
        }

    }

    /**
     * 构建结构体
     */
    private void buildXmlStruct(StringBuffer xmlSb, List<DeviceProp> structProp) {
        for (DeviceProp deviceProp : structProp) {
            String tableName = getTableName(deviceProp.getStructName());

            xmlSb.append("\t<class name=\"")
                    .append(this.getEntityName(deviceProp.getStructName()))
                    .append("\" table=\"")
                    .append(tableName)
                    .append("\">\n");

            Result<List<DevicePropChild>> propChildRe = deviceService.selectDevicePropChild(deviceProp.getId());
            List<DevicePropChild> propChildren = propChildRe.value();

            for (DevicePropChild propChild : propChildren) {
                DevicePropEnum propEnum = DevicePropEnum.getEnum(propChild.getConstruction());
                xmlSb.append(
                        String.format("\t\t<field type=\"%s\" table=\"%s\" column=\"%s\">%s</field>\n",
                                propEnum.getXmlConstruction(), tableName, propChild.getTag(), propChild.getTag())
                );
            }

            xmlSb.append("\t</class>\n");
        }

    }

    private String getEntityName(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    private String getTableName(String name) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, this.getEntityName(name));
    }

}
