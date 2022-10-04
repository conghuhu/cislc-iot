package com.shadow.web.service;

import com.shadow.web.model.xml.ShadowCode;
import com.shadow.web.utils.JavaFileUtils;
import com.shadow.web.utils.JavaPathConstants;
import com.shadow.web.utils.ParseXMLUtils;
import com.cislc.shadow.utils.enums.Encryption;
import com.cislc.shadow.utils.enums.Protocol;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * @ClassName XmlAnalysisService
 * @Description xml解析service
 * @Date 2019/11/4 17:12
 * @author szh
 **/
@Slf4j
@Service
public class XmlAnalysisService {

    /**
     * @Description 解析xml生成代码并编译
     * @param xmlFile xml文件
     * @param xsdFile xsd文件
     * @param protocol 通信协议
     * @param encryption 加密算法
     * @author szh
     * @Date 2019/10/22 13:57
     */
    boolean analyseXml(File xmlFile, File xsdFile, Protocol protocol, Encryption encryption) {
        /* 1. 校验xml */
        boolean validateSuccess = ParseXMLUtils.domValidate(xmlFile, xsdFile);
        if (validateSuccess) {
            /* 2. 解析生成代码 */
            ShadowCode code = ParseXMLUtils.xml2ClassCode(xmlFile, protocol, encryption);
            if (null != code) {
                /* 3. 写入java文件 */
                JavaFileUtils.newJavaFiles(code.getEntityCode(), JavaPathConstants.ENTITY_FILE_PATH);
                JavaFileUtils.newJavaFiles(code.getRepositoryCode(), JavaPathConstants.REPOSITORY_FILE_PATH);
                JavaFileUtils.newJavaFiles(code.getInitCode(), JavaPathConstants.INIT_FILE_PATH);
                JavaFileUtils.newJavaFiles(code.getEntityNameCode(), JavaPathConstants.ENTITY_NAME_FILE_PATH);
                JavaFileUtils.newJavaFiles(code.getAndroidEntityCode(), JavaPathConstants.ANDROID_JAVA_FILE_PATH);
                JavaFileUtils.newJavaFiles(code.getAndroidEntityNameCode(), JavaPathConstants.ANDROID_JAVA_FILE_PATH);
                return true;
            } else {
                // 解析失败删除所有代码
                log.error("解析xml生成代码并编译失败：解析生成代码失败");
                JavaFileUtils.deleteJavaFiles();
                return false;
            }
        } else {
            log.error("解析xml生成代码并编译失败：校验xml失败");
            return false;
        }
    }

}
