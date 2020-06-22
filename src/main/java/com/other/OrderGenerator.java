package com.other;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


/**
 *
 * @author sunwj
 *
 */

public enum OrderGenerator {

    INSTANCE;
    /**
     * 用ip地址最后几个字节标示
     */
    private long workerId;

    /**
     * 可配置在properties中,启动时加载,此处默认先写成0
     */
    private long datacenterId = 0L;

    private long sequence = 0L;

    /**
     * 节点ID长度
     */
    private long workerIdBits = 8L;

    /**
     * 序列号12位
     */
    private long sequenceBits = 12L;

    /**
     * 机器节点左移12位
     */
    private long workerIdShift = sequenceBits;

    /**
     * 数据中心节点左移14位
     */
    private long datacenterIdShift = sequenceBits + workerIdBits;

    /**
     * 为了保证绝对唯一，这里用static
     */
    private static long lastTimestamp = -1L;

    OrderGenerator() {
        workerId = 0x000000FF & getLastIP();
    }

    public synchronized String nextOrderId(String corpId) {
        //获取当前毫秒数
        long timestamp = timeGen();
        //如果服务器时间有问题(时钟后退) 报错。
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(String.format(
                    "Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        //如果是同一时间生成的，则进行毫秒内序列
        if (lastTimestamp == timestamp) {
                timestamp = tilNextMillis(lastTimestamp);
        }
        lastTimestamp = timestamp;

        long suffix = (datacenterId << datacenterIdShift) | (workerId << workerIdShift) | sequence;
        String datePrefix = convertTimestamp2Date(timestamp, "yyyyMMddHHmmssSSS");
        String tempOrderId = corpId + datePrefix + suffix;
        String orderId = tempOrderId + getRandom(32 - tempOrderId.length());
        return orderId;
    }

    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    protected long timeGen() {
        return System.currentTimeMillis();
    }

    private byte getLastIP() {
        byte lastip = 0;
        try {
            InetAddress ip = InetAddress.getLocalHost();
            byte[] ipByte = ip.getAddress();
            lastip = ipByte[ipByte.length - 1];
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return lastip;
    }

    public String getRandom(int length) {
        String val = "";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            val += String.valueOf(random.nextInt(10));
        }
        return val;
    }

    public static String convertTimestamp2Date(Long timestamp, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(new Date(timestamp));
    }


    public static void main(String[] args) {
        for (int i = 0; i < 100; i++){
            System.out.println(OrderGenerator.INSTANCE.nextOrderId("SN"));
        }
    }

}
