package com.shadow.web.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @ClassName CommandLineUtils
 * @Description 命令行工具
 * @Date 2019/10/29 14:21
 * @author szh
 **/
@Slf4j
public class CommandLineUtils {

    /**
     * @Description cmd命令maven打包
     * @author szh
     * @Date 2019/10/29 16:39
     */
    public static String mvnPackage() {
        String[] command = {"/bin/sh", "-c", "/root/shadow/package.sh"};
        Runtime runtime = Runtime.getRuntime();
        try {
            Process process = runtime.exec(command);
            if (null != process) {
                InputStream in = process.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                BufferedReader br = new BufferedReader(reader);
                String message;
                while ((message = br.readLine()) != null) {
                    System.out.println(message);
                }
                int status = process.waitFor();
                if (0 == status) {
                    return "success";
                } else {
                    return "打包失败";
                }
            } else {
                return "打包进程创建失败";
            }
        } catch (IOException | InterruptedException e) {
            log.error("cmd execute failed: {}", e.getMessage());
            return "打包失败，脚本错误";
        }

    }

}
