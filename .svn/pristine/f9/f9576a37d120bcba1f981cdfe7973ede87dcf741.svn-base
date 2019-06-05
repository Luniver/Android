package com.hrst.wifi;

import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;

/**
 * UDPSocket
 *
 * @author Levy
 */
public class UDPSocket {
    // 主机地址
    private final String _host;
    // 端口
    private final int _port;

    // Socket通道
    private DatagramSocket ds = null;
    // 接收数据包
    private DatagramPacket packet = null;
    // 通道地址
    private InetSocketAddress socketAddress = null;
    private DatagramPacket dataSendPacket;

    /**
     * 构造函数
     */
    public UDPSocket(final String host, final int port){
        _host = host;
        _port = port;
        byte[] receiveBuffer = new byte[2048];

        packet = new DatagramPacket(receiveBuffer, receiveBuffer.length);

        socketAddress = new InetSocketAddress(_host, _port);
    }

    /**
     * 作为服务端
     */
    public void asServer(){
        Logger.d("address: " + socketAddress.getHostString());
        try {
            ds = new DatagramSocket(socketAddress);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    /**
     * 作为客户端
     */
    public void asClient(){
        try {
            ds = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
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
     * 转发的方法
     */
    public final DatagramPacket send(final byte[] bytes) {
        try {
            if (null == dataSendPacket) {
                dataSendPacket = new DatagramPacket(bytes, bytes.length, InetAddress.getByName(_host), _port);
            }
            dataSendPacket.setData(bytes);
            ds.send(dataSendPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataSendPacket;
    }

    /**
     * 接收方法
     */
    public final byte[] receive() throws IOException {
        ds.receive(packet);
        return packet.getData();
    }

    /**
     * 关闭
     */
    public final void close() {
        try {
            if (ds != null){
                ds.disconnect();
                ds.close();
            }
            ds = null;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
