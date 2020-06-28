/*
 * Copyright (C), 2002-2019, 苏宁消费金融公司
 * FileName: AesEncryptor.java
 * Author:   Mask
 * Date:     2019/10/14 下午2:15
 * Description: //模块目的、功能描述
 * History: //修改记录
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间      版本号        描述
 */

package com.http;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.util.Random;

/**
 * Aes Encryptor
 * @author davidwang2006@aliyun.com
 */
public abstract class AesEncryptor {

	/**
	 * //"算法/模式/补码方式"
	 */
	public static final String PADDING = "AES/ECB/PKCS5Padding";

	public static Random RANDOM ;
	static {
		try{
			RANDOM = SecureRandom.getInstance("SHA1PRNG");
		}catch (NoSuchAlgorithmException ex){
			throw new RuntimeException(ex.getMessage());
		}
	}

	/**
	 * do the encrypt/decrypt operation
	 * @param mode encrypt or decrypt
	 * @param input the input message
	 * @param key 
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws InvalidAlgorithmParameterException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	static byte[] do0(int mode,byte[] input,byte[] key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException{
		SecretKeySpec spec = new SecretKeySpec(key, "AES");
		byte[] enCodeFormat = spec.getEncoded();
		SecretKeySpec seckey = new SecretKeySpec(enCodeFormat, "AES");
		Cipher cipher = Cipher.getInstance(PADDING);
		cipher.init(mode, seckey);
		return cipher.doFinal(input);
	}

	/**
	 * encrypt the message
	 * @param input the message
	 * @param key the key/password
	 * @return message encrypted
	 * @throws GeneralSecurityException
	 */
	public static byte[] encrypt(byte[] input,byte[] key) throws GeneralSecurityException{
		//加密的字节流长度必须可整除16，所以先做下面的padding操作
		try {
			return do0(Cipher.ENCRYPT_MODE, input, key);
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
				| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * decrypt the message
	 * @param input
	 * @param key
	 * @return
	 * @throws GeneralSecurityException
	 */
	public static byte[] decrypt(byte[] input,byte[] key) throws GeneralSecurityException{
		try {
			return do0(Cipher.DECRYPT_MODE, input, key);
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
				| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * generate random bytes, length 16
	 * @author davidwang2006@aliyun.com 88333695
	 * @return
	 */
	public static byte[] randomAesKey(){
		byte[] arr = new byte[16];
		RANDOM.nextBytes(arr);
		return arr;
	}
	

}
