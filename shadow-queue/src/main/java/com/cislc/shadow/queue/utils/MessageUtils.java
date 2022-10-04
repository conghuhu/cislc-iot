package com.cislc.shadow.queue.utils;

import com.cislc.shadow.queue.queue.QueueExecutor;
import com.cislc.shadow.queue.queue.message.MessageObserver;

/**
 * @ClassName MessageUtils
 * @Description 消息处理工具
 * @Date 2019/11/26 13:34
 * @author szh
 **/
public class MessageUtils {

    /**
     * @Description 设备数据增加观察者
     * @param observer 观察者
     * @author szh
     * @Date 2019/11/26 13:39
     */
    public static void addMsgObserver(MessageObserver observer) {
        QueueExecutor executor = ApplicationContextUtil.getBean(QueueExecutor.class);
        executor.addObserver(observer);
    }

    /**
     * @Description 设备数据删除观察者
     * @param observer 观察者
     * @author szh
     * @Date 2019/11/26 13:39
     */
    public static void delMsgObserver(MessageObserver observer) {
        QueueExecutor executor = ApplicationContextUtil.getBean(QueueExecutor.class);
        executor.delObserver(observer);
    }

}
