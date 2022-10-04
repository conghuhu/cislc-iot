package com.cislc.shadow.queue.queue.message;

import com.cislc.shadow.manage.core.bean.comm.ShadowOpsBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName MessageSubject
 * @Description 消息被观察者
 * @Date 2019/11/26 11:18
 * @author szh
 **/
public class MessageSubject {

    private List<MessageObserver> observers = new ArrayList<>();

    /**
     * @Description 增加观察者
     * @param o 观察者
     * @author szh
     * @Date 2019/11/26 11:29
     */
    public void addObserver(MessageObserver o) {
        this.observers.add(o);
    }

    /**
     * @Description 删除观察者
     * @param o 观察者
     * @author szh
     * @Date 2019/11/26 11:29
     */
    public void delObserver(MessageObserver o) {
        this.observers.remove(o);
    }

    /**
     * @Description 通知观察者
     * @param msg 消息
     * @author szh
     * @Date 2019/11/26 11:30
     */
    protected void notifyObservers(ShadowOpsBean msg) {
        for (MessageObserver o : observers) {
            o.onMsgArrived(msg);
        }
    }

}
