package com.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import static com.util.ByteFormat.bytesToHexString;

/**
 * Author: y
 * Date: 2019/7/22 15:58
 * <p>
 * Description: HmacSHA256加密工具
 */
public class HmacSHA256 {
    /**
     * sha256_HMAC加密
     * @param message 消息
     * @param secret  秘钥
     * @return 加密后字符串
     */
    public static String sha256_HMAC(String message, String secret) {
        String hash = "";
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] bytes = sha256_HMAC.doFinal(message.getBytes());
            hash = bytesToHexString(bytes);
        } catch (Exception e) {
            System.out.println("Error HmacSHA256 ===========" + e.getMessage());
        }
        return hash;
    }

    public static void main(String[] args) {
        // 密钥
        String appKey = "32139f31fc733647b1e865ebaab277c2";
        // 请求参数
        StringBuffer sb = new StringBuffer();
        String timeStamp = String.valueOf(System.currentTimeMillis());
        sb.append("97639e105c9d4db9a9d9ac3787870168").append("&&").append(timeStamp);
        String param = sb.toString();
        String result = HmacSHA256.sha256_HMAC(param, appKey);
        System.out.println("timeStamp：" + timeStamp);
        System.out.println(result);
    }
}
