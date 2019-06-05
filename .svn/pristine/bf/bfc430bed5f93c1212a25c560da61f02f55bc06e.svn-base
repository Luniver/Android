package com.hrst.induce.sms;

import com.hrst.induce.annotation.SendMethod;
import com.hrst.induce.annotation.SmsPduMode;
import com.hrst.induce.annotation.TargetFormat;
import com.hrst.induce.utils.StrUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 诱发短信消息
 * Created by Lin on 2018/6/4.
 */

public class HrstSmsMessage {

    //普通短信内容
    private String message;
    //长短信内容
    private List<String> messages;
    //是否发送普通短信
    private boolean sendSortMsg = true;
    //制式
    private String targetFormat;
    //发送方式:电信卡诱发、非电信卡诱发、常规捕获、新捕获、精准捕获
    private String sendMethod;
    //格式
    private int smsPduMode;
    //是否是开关机检测
    private boolean isSwitchDetection = false;
    //回执
    private boolean hasReceive;
    //国家码
    private String countryCode;

    private HrstSmsMessage(Builder builder) {
        this.message = builder.message;
        this.messages = builder.messages;
        this.sendSortMsg = builder.sendSortMsg;
        this.targetFormat = builder.targetFormat;
        this.sendMethod = builder.sendMethod;
        this.smsPduMode = builder.smsPduMode;
        this.hasReceive = builder.hasReceive;
        this.countryCode = builder.countryCode;
    }

    /**
     * 开关机检测设置
     * @param switchDetection
     */
    public void setSwitchDetection(boolean switchDetection) {
        this.isSwitchDetection = switchDetection;
    }

    /**
     * 是否为开关机检测
     * @return
     */
    public boolean isSwitchDetection() {
        return isSwitchDetection;
    }

    /**
     * 是否是电信卡诱发
     * @return
     */
    public boolean isCdmaSendSms(){
        if(SendMethod.METHOD_CDMA_SEND.equals(sendMethod)){
            //制式
            return true;
        }
        return false;
    }

    /**
     * 获取安全短信内容
     * @return
     */
    public List<String> getSafeContents(){
        List<String> contents = new ArrayList<>();
        if(sendSortMsg){
            //添加普通短信
            if(message == null){
                contents.add(buildSafeConent(getDefaultContent(), 0));
            }else{
                contents.add(buildSafeConent(message, 0));
            }
        }
        //添加长短信
        if(messages != null && messages.size() > 0){
            for(int i=0; i<messages.size(); i++){
                contents.add(buildSafeConent(messages.get(i), i + 1));
            }
        }
        return contents;
    }

    private String buildSafeConent(String content, int smsIndex){
        StringBuilder contentBuilder = new StringBuilder();
        contentBuilder.append("SafeSms");
        if(TargetFormat.TARGET_FORMAT_CDMA.equals(targetFormat) && SendMethod.METHOD_CDMA_SEND.equals(sendMethod)){
            //CDMA制式且电信卡诱发（电信卡诱发电信目标）
            contentBuilder.append(",2");
        }else{
            contentBuilder.append(",1");
        }
        //制式
        contentBuilder.append("," + getTargetFormatCode());

        //开关机检测
        contentBuilder.append(isSwitchDetection ? ",3" : ",2");

        //CDMA不用添加回执标志
        if(!TargetFormat.TARGET_FORMAT_CDMA.equals(targetFormat)){
            //回执
            contentBuilder.append(hasReceive ? ",true" : ",false");
        }

        //电信卡诱发电信不用添加内容和格式
        if(TargetFormat.TARGET_FORMAT_CDMA.equals(targetFormat) && SendMethod.METHOD_CDMA_SEND.equals(sendMethod)){
            //内容
            contentBuilder.append(",");
        }else{
            //内容
            contentBuilder.append("," + content);
            //格式
            if(TargetFormat.TARGET_FORMAT_CDMA.equals(targetFormat)){
                contentBuilder.append("," + (smsPduMode + 1));
            }else{
                contentBuilder.append("," + smsPduMode);
            }

        }

        //国家码
        contentBuilder.append("," + countryCode);
        contentBuilder.append("," + smsIndex);
        return contentBuilder.toString();
    }

    /**
     * 获取制式内容
     * @return
     */
    private String getTargetFormatCode(){
        String code = "";
        switch (targetFormat){
            case TargetFormat.TARGET_FORMAT_GSM:
                code = "1";
                break;
            case TargetFormat.TARGET_FORMAT_CDMA:
                code = "2";
                break;
            case TargetFormat.TARGET_FORMAT_WCDMA:
                code = "3";
                break;
            case TargetFormat.TARGET_FORMAT_TDSCDMA:
                code = "4";
                break;
            case TargetFormat.TARGET_FORMAT_LTE:
                code = "5";
                break;
        }
        return code;
    }

    /**
     * 获取发送内容
     * @return
     */
    private String getDefaultContent(){
        if(isSwitchDetection){
            return "";
        }
        String content = "";
        if(TargetFormat.TARGET_FORMAT_GSM.equals(targetFormat)){
            content = String.format("%s品晶畾瞐品晶畾瞐%s", StrUtil.getEnglishStringRandom(4),
                    StrUtil.getEnglishStringRandom(4));
        }else if(TargetFormat.TARGET_FORMAT_CDMA.equals(targetFormat)){
            content = "晶晶晶";
        }else if(TargetFormat.TARGET_FORMAT_LTE.equals(targetFormat)){
            if(SendMethod.METHOD_LTE_NORMAL.equals(sendMethod)){
                content = StrUtil.getEnglishStringRandom(4);
            }
        }
        return content;
    }

    /**
     * 是否有回执
     * @return
     */
    public boolean hasReceive() {
        return hasReceive;
    }

    @Override
    public String toString() {
        return "HrstSmsMessage{" +
                "message='" + message + '\'' +
                ", messages=" + messages +
                ", sendSortMsg=" + sendSortMsg +
                ", targetFormat='" + targetFormat + '\'' +
                ", sendMethod='" + sendMethod + '\'' +
                ", smsPduMode=" + smsPduMode +
                ", isSwitchDetection=" + isSwitchDetection +
                ", hasReceive=" + hasReceive +
                ", countryCode='" + countryCode + '\'' +
                '}';
    }

    public static class Builder{
        //普通短信内容
        private String message;
        //长短信内容
        private List<String> messages;
        //是否发送普通短信
        private boolean sendSortMsg = true;
        //制式
        private String targetFormat;
        //发送方式:电信卡诱发、非电信卡诱发、常规捕获、新捕获、精准捕获
        private String sendMethod = "";
        //格式
        private int smsPduMode;
        //回执
        private boolean hasReceive = false;
        //国家码
        private String countryCode = "";

        public Builder(){

        }

        /**
         * 设置普通短信，未设置则使用默认短信内容
         * @param message
         * @return
         */
        public Builder setMessage(String message){
            this.message = message;
            return this;
        }

        /**
         * 设置长短信
         * @param messages  长短信内容
         * @param sendSortMsg 是否同时发送普通短信
         * @return
         */
        public Builder setLongMessage(List<String> messages, boolean sendSortMsg){
            this.messages = messages;
            this.sendSortMsg = sendSortMsg;
            return this;
        }

        /**
         * * 设置长短信
         * @param messages  长短信内容（同时发送普通短信）
         * @return
         */
        public Builder setLongMessage(List<String> messages){
            return setLongMessage(messages, true);
        }

        /**
         * 设置诱发制式
         * @param targetFormat
         */
        public Builder setTargetFormat(@TargetFormat String targetFormat) {
            this.targetFormat = targetFormat;
            return this;
        }

        /**
         * 设置发送方式(LTE或制式时调用)
         * @param sendMethod
         */
        public Builder setSendMethod(@SendMethod String sendMethod) {
            this.sendMethod = sendMethod;
            return this;
        }

        /**
         * 设置诱发格式
         * @param smsPduMode
         */
        public Builder setSmsPduMode(@SmsPduMode int smsPduMode) {
            this.smsPduMode = smsPduMode;
            return this;
        }

        /**
         * 是否需要回执
         * 默认false
         * @param hasReceive
         */
        public Builder setHasReceive(boolean hasReceive) {
            this.hasReceive = hasReceive;
            return this;
        }

        /**
         * 设置国家码
         * @param countryCode
         */
        public Builder setCountryCode(String countryCode) {
            this.countryCode = countryCode;
            return this;
        }

        public HrstSmsMessage build(){
            return new HrstSmsMessage(this);
        }

        @Override
        public String toString() {
            return "Builder{" +
                    "message='" + message + '\'' +
                    ", messages=" + messages +
                    ", sendSortMsg=" + sendSortMsg +
                    ", targetFormat='" + targetFormat + '\'' +
                    ", sendMethod='" + sendMethod + '\'' +
                    ", smsPduMode=" + smsPduMode +
                    ", hasReceive=" + hasReceive +
                    ", countryCode='" + countryCode + '\'' +
                    '}';
        }
    }
}
