package com.example.commblib.config;

/**
 * @创建者: xuanwen
 * @创建日期: 2017/8/8
 * @描述: 同意通讯地址类
 */

public class HttpAddress {
    private static String HOST_TEST_ADDRESS = "http://www.lejurobot.com/client/interface.php?func=";
    private static String HOST_ADDRESS = HOST_TEST_ADDRESS;

    public static String getHostAddress() {
        return HOST_ADDRESS;
    }
}
