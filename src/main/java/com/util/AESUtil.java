package com.util;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Date: 2019/7/22 17:16
 * <p>
 * Description: AES加解密
 * @author sunwj
 */


public class AESUtil {

    private static byte[] iv = "0000000000000000".getBytes();

    /**
     * 解密AES加密过的字符串
     *
     * @param content
     *            AES加密过过的内容
     * @param password
     *            加密时的密码
     * @return 明文
     */
    public static String decrypt(String content, String password) {
        try {
            IvParameterSpec ivspec = new IvParameterSpec(iv);
            SecretKeySpec secretKey = new SecretKeySpec(password.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
            byte[] contentByte = ByteFormat.hexToBytes(content);
            byte[] result = cipher.doFinal(contentByte);
            return new String(result);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 报错java.security.InvalidKeyException: Illegal key size
    // 原因：Illegal key size or default parameters 是指密钥长度受限制，
    //
    // java运行时环境读到的是受限的policy文件。
    //
    // policy文件位于${java_home}/jre/lib/security 目录下。
    //
    // 这种限制是因为美国对软件出口的控制。
    //
    // 解决办法：去除该限制只需下载 Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files，
    //
    // 覆盖上述目录下的对应jar文件(local_policy.jar, US_export_policy.jar)即可。
    // JDK7 http://www.oracle.com/technetwork/java/javase/downloads/jce-7-download-432124.html
    // JDK8 http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html
    public static void main(String[] args) {
        String de = decrypt("d8de5f60137aba90a1e3ec13b12ec473", "32139f31fc733647b1e865ebaab277c2");
        System.out.println(de);
    }
}
