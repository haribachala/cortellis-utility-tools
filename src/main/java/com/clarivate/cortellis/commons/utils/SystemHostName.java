package com.clarivate.cortellis.commons.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by U6015446 on 15-Nov-16.
 */
public class SystemHostName {

    String hostname = "Unknown";
    public String getHostName() {
        try
        {
            InetAddress addr;
            addr = InetAddress.getLocalHost();
            hostname = addr.getHostName();
        }
        catch(UnknownHostException ex)
        {
            System.out.println("Hostname can not be resolved");
        }
        return  hostname;
    }
}
