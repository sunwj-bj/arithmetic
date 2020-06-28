/*
 * Copyright (C), 2002-2019, 苏宁消费金融公司
 * FileName: RSACoder.java
 * Author:   Mask
 * Date:     2019/10/14 下午2:18
 * Description: //模块目的、功能描述
 * History: //修改记录
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间      版本号        描述
 */
package com.http;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.interfaces.RSAKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public abstract class RSACoder {

    public static final String KEY_ALGORITHM = "RSA";
    public static final String SIGNATURE_ALGORITHM_SHA256 = "SHA256withRSA";

    /**
     * sign with SHA256WithRSA
     * @param data
     * @param privateKey
     * @return
     */
    public static String signSha256(byte[] data, String privateKey)
            throws InvalidKeySpecException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        byte[] keyBytes = Base64.decodeBase64(privateKey);

        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

            PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);

            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM_SHA256);
            signature.initSign(priKey);
            signature.update(data);

            return Base64.encodeBase64URLSafeString(signature.sign());
        } catch (GeneralSecurityException e) {
            throw e;
        }
    }


    /**
     * verify with SHA256RSA
     * @param data
     * @param publicKey
     * @param sign
     * @return
     */
    public static boolean verifySha256(byte[] data, String publicKey, String sign) {
        try {
            byte[] keyBytes = Base64.decodeBase64(publicKey);

            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);

            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

            PublicKey pubKey = keyFactory.generatePublic(keySpec);

            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM_SHA256);
            signature.initVerify(pubKey);
            signature.update(data);

            return signature.verify(Base64.decodeBase64(sign));
        } catch (GeneralSecurityException e) {
        }
        return false;
    }

    /**
     * 加密
     *
     * @param data 待加密内容
     * @param publicKey  密钥
     * @return
     */
    public static String encrypt(byte[] data, String publicKey) {

        try {
            byte[] keyBytes = Base64.decodeBase64(publicKey);

            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            PublicKey key = keyFactory.generatePublic(x509KeySpec);
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);

            int inputLen = data.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            // 对数据分段加密
            int maxEncryptBlock = calcMaxEncryptBlock((RSAKey) key);
            processBySegment(data, cipher, inputLen, out, maxEncryptBlock);

            return Base64.encodeBase64URLSafeString(out.toByteArray());
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
        return null;

    }

    private static void processBySegment(byte[] data, Cipher cipher, int inputLen, ByteArrayOutputStream out, int maxEncryptBlock) throws Exception {
        byte[] cache;
        int offSet = 0;
        int i = 0;
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > maxEncryptBlock) {
                cache = cipher.doFinal(data, offSet, maxEncryptBlock);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * maxEncryptBlock;
        }
    }

    /**
     * 解密
     *
     * @param encryptedContent 待解密内容
     * @param privateKey 密钥
     * @return
     */
    public static byte[] decrypt(String encryptedContent, String privateKey){

        try {
            byte[] keyBytes = Base64.decodeBase64(privateKey);

            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            PrivateKey key = keyFactory.generatePrivate(pkcs8KeySpec);

            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);

            byte[] encryptedData = Base64.decodeBase64(encryptedContent);

            int inputLen = encryptedData.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            // 对数据分段解密
            int maxDecryptBlock = calcMaxDecryptBlock((RSAKey) key);
            processBySegment(encryptedData, cipher, inputLen, out,maxDecryptBlock);

            return out.toByteArray();
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
        return null;

    }

    /**
     * 计算加密分块最大长度
     */
    private static int calcMaxEncryptBlock(RSAKey key) {
        return key.getModulus().bitLength() / 8 - 11;
    }

    /**
     * 计算解密分块最大长度
     * @param key
     * @return
     */
    private static int calcMaxDecryptBlock(RSAKey key) {
        return key.getModulus().bitLength() / 8;
    }
}
