package com.cislc.shadow.queue.queue.message;

import com.cislc.shadow.manage.core.bean.comm.ShadowOpsBean;

/**
 * @InterfaceName MessageObserver
 * @Description 消息观察者
 * @Date 2019/11/26 11:19
 * @author szh
 **/
public interface MessageObserver {

    /**
     * @Description 消息到达时执行
     * @param msg 消息
     * @author szh
     * @Date 2019/11/26 13:33
     */
    void onMsgArrived(ShadowOpsBean msg);

}
