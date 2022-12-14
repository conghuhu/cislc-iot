package com.communication.utils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * @Author: bin
 * @Date: 2019/11/19 13:19
 * @Description:
 */
public class IpUtil {

    public static String INTRANET_IP = getIntranetIp(); // 内网IP
    public static String INTERNET_IP = getInternetIp(); // 外网IP
    /**
     * 获得外网IP
     * @return 外网IP
     */
    private static String getInternetIp() {
        try{
            Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            Enumeration<InetAddress> addrs;
            while (networks.hasMoreElements())
            {
                addrs = networks.nextElement().getInetAddresses();
                while (addrs.hasMoreElements())
                {
                    ip = addrs.nextElement();
                    if (ip != null
                            && ip instanceof Inet4Address
                            && ip.isSiteLocalAddress()
                            && !ip.getHostAddress().equals(INTRANET_IP))
                    {
                        return ip.getHostAddress();
                    }
                }
            }

            // 如果没有外网IP，就返回内网IP
            return null;
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * 获得内网IP
     * @return 内网IP
     */
    private static String getIntranetIp(){
        try{
            return InetAddress.getLocalHost().getHostAddress();
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }

}
