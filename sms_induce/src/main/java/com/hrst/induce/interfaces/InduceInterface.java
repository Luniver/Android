package com.hrst.induce.interfaces;

import com.hrst.induce.InduceTask;
import com.hrst.induce.sms.HrstSmsMessage;

/**
 * 诱发接口
 * Created by Lin on 2018/6/4.
 */

public interface InduceInterface {

    /**
     * 开始诱发
     * @param  induceTask 诱发任务
     */
    void startInduce(InduceTask induceTask);

    /**
     * 结束诱发
     */
    void stopInduce();

    /**
     * 是否正在诱发
     * @return
     */
    boolean isInducing();

}
