package com.shadow.web.utils;

/**
 * @InterfaceName JavaPathConstants
 * @Description java路径常量
 * @Date 2019/12/23 16:22
 * @author szh
 **/
public interface JavaPathConstants {

    // 项目路径
    String PROJECT_PATH = "D:/lab-project/IOT/shadow";
    String MODULE_MANAGE_PATH = PROJECT_PATH + "shadow-manage/";
    String MODULE_QUEUE_PATH = PROJECT_PATH + "shadow-queue/";
    String ANDROID_PATH = PROJECT_PATH + "ShadowUtils/";
    String DEVICE_CLASS_FILE_PATH = MODULE_MANAGE_PATH + "target/classes/com/cislc/shadow/manage/device";
    String DEVICE_JAVA_FILE_PATH = MODULE_MANAGE_PATH + "src/main/java/com/cislc/shadow/manage/device/";
    String ANDROID_JAVA_FILE_PATH = ANDROID_PATH + "app/src/main/java/com/cislc/shadowutils/greenDao/entity/";
    String ANDROID_DAO_FILE_PATH = ANDROID_PATH + "app/src/main/java/com/cislc/shadowutils/greenDao/dao/";
    String MAIN_PACKAGE_NAME = "com.cislc.shadow.manage.";

    // ============================ 文件路径 ==============================
    // java实体类文件路径
    String ENTITY_FILE_PATH = DEVICE_JAVA_FILE_PATH + "entity/";
    // 数据库管理文件路径
    String REPOSITORY_FILE_PATH = DEVICE_JAVA_FILE_PATH + "repository/";
    // 初始化类文件路径
    String INIT_FILE_PATH = DEVICE_JAVA_FILE_PATH + "init/";
    // 实体名常量接口路径
    String ENTITY_NAME_FILE_PATH = DEVICE_JAVA_FILE_PATH + "names/";

    // ============================== 包名 ==================================
    // 动态类的包名
    String ENTITY_PACKAGE_NAME = MAIN_PACKAGE_NAME + "device.entity";
    // 数据库映射包名
    String REPOSITORY_PACKAGE_NAME = MAIN_PACKAGE_NAME + "device.repository";
    // 初始化类包名
    String INIT_PACKAGE_NAME = MAIN_PACKAGE_NAME + "device.init";
    // 实体名常量接口包名
    String ENTITY_NAME_PACKAGE_NAME = MAIN_PACKAGE_NAME + "device.names";

    // ============================= Android =============================
    // android动态类包名
    String ANDROID_ENTITY_PACKAGE_NAME = "com.cislc.shadowutils.greenDao.entity";
    // android相机类名
    String ANDROID_CAMERA_CLASS_NAME = "android.hardware";

    // zip包路径
    String PACKAGE_PATH = "/root/shadow/package/package.zip";

}
