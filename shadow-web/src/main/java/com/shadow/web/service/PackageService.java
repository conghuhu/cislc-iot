package com.shadow.web.service;

import com.shadow.web.utils.*;
import com.cislc.shadow.utils.enums.Encryption;
import com.cislc.shadow.utils.enums.Protocol;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @ClassName PackageService
 * @Description 打包service
 * @Date 2019/11/6 9:33
 * @author szh
 **/
@Slf4j
@Service
public class PackageService {

    @Autowired
    private XmlAnalysisService xmlAnalysisService;

    /**
     * @Description 打包并下载
     * @param xmlFiles xml文件
     * @param protocol 通信协议
     * @param encryption 加密算法
     * @param serverPackageName 后台jar包文件名
     * @author szh
     * @Date 2019/11/6 16:33
     */
    void packageJar(List<File> xmlFiles,
                           Protocol protocol,
                           Encryption encryption,
                           String serverPackageName,
                           HttpServletRequest request,
                           HttpServletResponse response) {
        /* 1. 删除之前的代码 */
        JavaFileUtils.deleteJavaFiles();

        /* 2. 解析xml生成代码 */
        // 读取xsd
        ClassPathResource cpr = new ClassPathResource(XmlConstants.XSD_PATH);
        File xsdFile;
        try (InputStream is = cpr.getInputStream()) {
            xsdFile = File.createTempFile("model", ".xsd");
            FileUtils.copyInputStreamToFile(is, xsdFile);
        } catch (IOException e) {
            log.error("打包失败：读xml失败", e);
            return;
        }
        // 解析xml
        for (File file : xmlFiles) {
            boolean result = xmlAnalysisService.analyseXml(file, xsdFile, protocol, encryption);
            if (!result) {
                log.error("打包失败：解析xml失败");
                JavaFileUtils.deleteJavaFiles();
                return;
            }
        }

        /* 3. maven打包 */
        long current = System.currentTimeMillis();
        log.info("maven打包开始：packageName={}, current={}", serverPackageName, current);
        String packageResult = CommandLineUtils.mvnPackage();
        if (!"success".equals(packageResult)) {
            log.error("打包失败：maven打包失败，{}", packageResult);
            JavaFileUtils.deleteJavaFiles();
            return;
        }
        log.info("maven打包结束：packageName={}, usedTime={}", serverPackageName, System.currentTimeMillis() - current);

        /* 4. 返回jar包 */
        FileUtil.downloadFile(JavaPathConstants.PACKAGE_PATH, serverPackageName, request, response);
    }

}
