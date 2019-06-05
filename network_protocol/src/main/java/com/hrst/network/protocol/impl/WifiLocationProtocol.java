package com.hrst.network.protocol.impl;

import android.support.annotation.NonNull;

/**
 * wifi 定位 协议
 * Created by Sophimp on 2018/8/22.
 */

public class WifiLocationProtocol extends ProtocolImpl {

    /**
     * 同步头
     */
    private final byte[] synHeadData = new byte[]{(byte) 0xA5, (byte) 0x5A, (byte) 0xCD, (byte) 0xDC};

    @NonNull
    @Override
    public byte[] headers() {
        return synHeadData;
    }

    @Override
    public byte property() {
        return 0x19;
    }
}
