package com.autobid.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class HostUtil {
    public static String getLocalHost() throws UnknownHostException {
        InetAddress addr = InetAddress.getLocalHost();
//        String ip=addr.getHostAddress().toString(); //��ȡ����ip
        String hostName=addr.getHostName().toString(); //��ȡ�������������
        //System.out.println("hostname:" + hostName);
        return hostName;
    }
    public static String getConfHost() throws IOException {
        String confHost = ConfUtil.getLocalProperty("host_name");
        return confHost;
    }
}
