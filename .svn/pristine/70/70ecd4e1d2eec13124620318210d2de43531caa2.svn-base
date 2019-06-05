package com.hrst.wifi;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * UDPClient
 *
 * @author Levy
 */
public class UDPClient {

    // 主机地址
    private final String _host;
    // 端口
    private final int _port;
    // 数据大小
    private int dataSize = 2048;
    // Socket通道
    private DatagramSocket ds = null;
    private DatagramPacket dataSendPacket;

    public UDPClient(final String host, final int port) throws SocketException {
        _host = host;
        _port = port;
        ds = new DatagramSocket();
    }

    /**
     * 设置超时时间
     *
     * @param timeout
     * @throws Exception
     */
    public final void setSoTimeout(int timeout) throws Exception {
        ds.setSoTimeout(timeout);
    }

    /**
     * 获取超时时间
     *
     * @return
     * @throws Exception
     */
    public final int getSoTimeout() throws Exception {
        return ds.getSoTimeout();
    }

    /**
     * 数据发送
     *
     * @param bytes
     * @return
     * @throws Exception
     */
    public final DatagramPacket send(final byte[] bytes) {
        try {
            if (null == dataSendPacket) {
                dataSendPacket = new DatagramPacket(new byte[0], 0, InetAddress.getByName(_host), _port);
            }
            dataSendPacket.setData(bytes, 0, bytes.length);
            ds.send(dataSendPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dataSendPacket;
    }

    /**
     * 数据接收
     *
     */
    public final byte[] receive() throws Exception {
        byte[] buffer = new byte[dataSize];
        DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
        ds.receive(dp);
        return dp.getData();
    }

    /**
     * 关闭
     */
    public final void close() {
        try {
            ds.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
