package com.hrst.common.protocol;

import android.util.Log;

import com.hrst.common.protocol.crc.CrcUtil;
import com.hrst.common.util.ArrayUtils;
import com.orhanobut.logger.Logger;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 数传mcu 协议解析
 * <p>
 * Created by Sophimp on 2018/4/25.
 * <p>
 * 433mcu通信协议解析
 * modified by Sophimp on 2019/1/9.
 */
public class PacketImpl implements IPacket {

    private static final String TAG = "goo-PacketImpl";
    /**
     * 最小数据数据包大小
     */
    private final static int MIN_PACKET_SIZE = 10;

    /**
     * 一帧数据最大长度
     */
    private final static int MAX_FRAME_SIZE = 2048;
    /**
     * Header 大小
     */
    private static final int HEADER_LENGTH = 4;

    /**
     * 数据丢弃命令
     */
    private static final short CMD_DISCARD = 0x7fff;

    /**
     * 同步头
     */
    private final byte[] synHeadData = new byte[]{(byte) 0xA5, (byte) 0x5A, (byte) 0xCD, (byte) 0xDC};

    /**
     * 头缓冲
     */
    private byte[] headerBuffer = new byte[4];

    /**
     * 原始数据缓冲
     */
    private Queue<Byte> rawDataBuffers;

    /**
     * 操作缓冲区
     */
    private ByteBuffer operateBuffers;

    /**
     * 默认从头开始解析
     */
    private State curState = State.IDLE;

    /**
     * 当前数据包总长度
     * 公共头(4byte) + 长度(2byte) + parseLength
     */
    private int curFrameLength;
    /**
     * 当前帧命令字, 一字节的无符号数, 所以要用两个字节接收
     * 最后再用 char 强转
     */
    private short curFrameCmd;

    /**
     * 当前帧属性字
     */
    private byte curFrameProperty;
    /**
     * 解析得到的数据长度
     * 属性(1byte) + cmd(1字节) + content.length + crc(2字节)
     */
    private int parseLength;
    /**
     * 内容区
     */
    private byte[] contentData;

    /**
     * 缓存crc 验证数据
     * 属性(1byte) + cmd(1byte) + content.length
     */
    private byte[] crcBuffers;

    private OnDataReceiverListener dataReceiverListener;
    private final CrcUtil crcUtil;

    /**
     * 用来记录未解析出来, 或解析出错的字长
     */
    private int parseRecord = 0;

    /**
     * 解析数据存在可能情况:
     * 1. 丢包, crc校验不通过, 数据都丢
     * 2. 未丢, crc校验不通过, 也丢
     */
    private enum State {
        IDLE,           // 已解析出完整的包
        HEADER_PARSING,     // 解析头
        LENGTH_PARSING,     // 长度解析
        PROPERTY_PARSING,   // 属性解析
        CMD_PARSING,         // 命令解析
        CONTENT_PARSING,     // 内容解析
        CRC_PARSING,         // CRC解析
    }

    public PacketImpl() {
        crcUtil = new CrcUtil();
        rawDataBuffers = new LinkedBlockingDeque<>();
        operateBuffers = ByteBuffer.allocateDirect(MAX_FRAME_SIZE);
    }

    @Override
    public void fillRawData(byte rawData) {
        // 先缓存数据
        rawDataBuffers.add(rawData);
        parseData();
    }

    private void parseData() {
        byte data = rawDataBuffers.poll();
//        Log.d(TAG, "curState: " + curState.name());
        switch (curState) {
            case IDLE:
                parseRecord++;
                findHeader(data);
                break;
            case HEADER_PARSING:
                parsingHeader(data);
                break;
            case LENGTH_PARSING:
                parsingLength(data);
                break;
            case PROPERTY_PARSING:
                curFrameProperty = data;
                curState = State.CMD_PARSING;
                crcBuffers[0] = data;
                break;
            case CMD_PARSING:
                // 命令字是无符号号8字节, 只是正的, &0xff 是取正数的反码(正数本身)
                curFrameCmd = (short) (data & 0xff);
                crcBuffers[1] = data;
                contentData = new byte[parseLength - 4];
                if (parseLength == 4) {
                    // 无内容
                    curState = State.CRC_PARSING;
                } else {
                    // 有内容
                    curState = State.CONTENT_PARSING;
                }
                break;
            case CONTENT_PARSING:
                contentParsing(data);
                break;
            case CRC_PARSING:
                parsingCrc(data);
                break;
        }
    }

    @Override
    public void fillRawData(byte[] rawDatas) {
        for (int i = 0; i < rawDatas.length; i++) {
            fillRawData(rawDatas[i]);
        }
    }

    private void parsingCrc(byte data) {
        operateBuffers.put(data);
        if (operateBuffers.position() == 2) {
            operateBuffers.flip();
            short s0 = (short) (operateBuffers.get() & 0xff);
            short s1 = (short) ((operateBuffers.get() & 0xff) << 8);
            int parsedCrc = s0 | s1;
//            short parsedCrc = operateBuffers.getShort();
            //  一帧数据解析完了,开始验证
            int curcmd = 0xff & curFrameCmd;
//            if (curFrameProperty == 0x1a) {
//            if (!(curcmd == 0x1 || curcmd == 0x60)) {
//                Log.d(TAG, "cmd: " + Integer.toHexString(curcmd) + " - curFrameData:" + ArrayUtils.toHexString(contentData));
//                Log.d(TAG, "curFrameData:" + ArrayUtils.toHexString(headerBuffer)
//                        + Integer.toHexString(0xffff & parseLength) + ArrayUtils.toHexString(crcBuffers));
//            }
            int calCrc = crcUtil.getCrc(crcBuffers);
//            if (curFrameProperty == 0x1a) {
//                Logger.d("goo parsingCrc: " + Integer.toHexString(parsedCrc) + " calcCrc:" + Integer.toHexString(calCrc));
//            }
            if (parsedCrc == calCrc) {
//                if (!(curcmd == 0x1 || curcmd == 0x60)){
//                if (curFrameProperty == 0x1a) {
//                    Logger.d("goo parse crc curFrameData:" + ArrayUtils.toHexString(headerBuffer)
//                            + Integer.toHexString(0xffff & parseLength) + ArrayUtils.toHexString(crcBuffers));
//                }
                // 数据合法
                if (null != dataReceiverListener) {
                    dataReceiverListener.onDataReceive(curFrameCmd, curFrameProperty, contentData);
                }
            } else {
                if (null != dataReceiverListener) {
                    dataReceiverListener.onDataReceive(CMD_DISCARD, curFrameProperty, String.format("命令字 %d 解析错误", curFrameCmd).getBytes());
                }
            }
            // 不管合不合法, 到这一步了, 都要开始下一帧数据
            resetParse();
        }
    }

    /**
     * 数据内容解析
     */
    private void contentParsing(byte data) {
        operateBuffers.put(data);
        if (operateBuffers.position() == contentData.length) {
            curState = State.CRC_PARSING;
            // 内容收完了
            operateBuffers.flip();
            for (int i = 0; i < contentData.length; i++) {
                contentData[i] = operateBuffers.get();
                crcBuffers[i + 2] = contentData[i];
            }
            operateBuffers.clear();
        }
    }

    /**
     * 解析长度
     * 判断数据长度(设备属性，命令字，数据，校验码)
     *
     * @param data
     */
    private void parsingLength(byte data) {
        operateBuffers.put(data);
        if (operateBuffers.position() == 2) {
            operateBuffers.flip();
            // 先解析出两个字节, 再取低8位, 得到真实长度
//            byte[] lens = new byte[2];
//            lens[0] = operateBuffers.get();
//            lens[1] = operateBuffers.get();
            // 这里也是个坑, 此方法转的short 跟 getShort的值不一样
            short s0 = (short) (operateBuffers.get() & 0xff);
            short s1 = (short) ((operateBuffers.get() & 0xff) << 8);
//            Log.d(TAG, "parsingLength: low: " + s0 + "- high:" + s1);
            parseLength = (short) (s0 | s1);
//            parseLength = (char) s;
//            parseLength = (char) operateBuffers.getShort();
//            Log.d(TAG, "parsingLength: " + parseLength);
            if (parseLength < 4 || parseLength > MAX_FRAME_SIZE) {
                // 长度不合法, 进行下一帧数据解析
                curState = State.IDLE;
            } else {
                // 长度合法
                crcBuffers = new byte[parseLength - 2];
                curState = State.PROPERTY_PARSING;
            }
            operateBuffers.clear();
        }
    }

    /**
     * 将byte转为short
     *
     * @return short
     */
    public static short byteToShort() {
        short s = 0;
        return s;
    }

    /**
     * 验证头
     *
     * @param data
     */
    private void parsingHeader(byte data) {
        operateBuffers.put(data);
        if (operateBuffers.position() == HEADER_LENGTH) {
            operateBuffers.flip();
            operateBuffers.get(headerBuffer, 0, headerBuffer.length);
            if (Arrays.equals(headerBuffer, synHeadData)) {
                // 合法头
                curState = State.LENGTH_PARSING;
                operateBuffers.clear();
                // 回报丢弃的数据长度
                if ((parseRecord > 4)) {
                    dataReceiverListener.onDataReceive(CMD_DISCARD, curFrameProperty, String.format("丢弃 %d 字节", parseRecord).getBytes());
                    parseRecord = 0;
                }
            } else {
                // 扔掉第一个数据, 进行下一步验证
                operateBuffers.position(1);
                operateBuffers.compact();
            }
        }
    }

    /**
     * 查找头
     *
     * @param data
     */
    private void findHeader(byte data) {
        if (data == synHeadData[0]) {
            operateBuffers.put(data);
            curState = State.HEADER_PARSING;
        }
    }

    /**
     * 重置解析参数
     */
    private void resetParse() {
        parseRecord = 0;
        curFrameLength = 0;
        curFrameCmd = 0;
        curFrameProperty = 0;
        curState = State.IDLE;
        operateBuffers.clear();
    }

    /**
     * 监听
     */
    @Override
    public void setDataReceiverListener(OnDataReceiverListener listener) {
        this.dataReceiverListener = listener;
    }

}
