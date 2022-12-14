package com.cislc.shadow.manage.core.bean.comm;

/**
 * 影子中的常量字段
 */
public interface ShadowConst {

    // RePayload 影子通信回复载荷状态
    String PAYLOAD_STATUS_SUCCESS = "success";
    String PAYLOAD_STATUS_ERROR = "error";

    // ShadowDoc 影子文档时间戳key
    String DOC_KEY_TIMESTAMP = "timestamp";

    // ShadowOpsBean 操作字段
    String OPERATION_METHOD_DELETE = "delete";
    String OPERATION_METHOD_UPDATE = "update";
    String OPERATION_METHOD_GET = "get";
    String OPERATION_METHOD_BIND = "bind";
    String OPERATION_METHOD_PING = "ping";

    // ShadowReplyBean 回复操作字段
    String REPLY_METHOD_REPLY = "reply";
    String REPLY_METHOD_CONTROL = "control";
    String REPLY_METHOD_PONG = "pong";

    // 设备绑定
    String BIND_CODE = "bindCode";
    String BIND_TOPIC = "bindDevice";

    // ip
    String IP = "ip";

    // 设备id
    String DEVICE_ID = "deviceId";

}
