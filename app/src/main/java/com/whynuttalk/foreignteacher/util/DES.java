package com.whynuttalk.foreignteacher.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import cn.sinata.util.Base64DES;

/**
 * Created by cz on 3/30/18.
 */

public class DES {

	/*
     * 不要写成：Cipher cipher = Cipher.getInstance("DES");
	 * 或：Cipher cipher = Cipher.getInstance("DES/CBC/NoPadding");
	 * 原因是Cipher cipher = Cipher.getInstance("DES");
	 * 与Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
	 * 等同，填充方式错误，加密的时候会得到16长度的字节数组。
	 * 应该写成
	 * Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
	 */


    private static final String KEY = "ForUApp=";

    private static byte[] iv = {12, 22, 32, 43, 51, 64, 57, 98};

    /**
     * 指定密钥加密
     *
     * @param encryptString 原文
     * @param encryptKey    密钥
     * @return 密文
     * @throws Exception
     */
    public static String encryptDES(String encryptString, String encryptKey)
            throws Exception {

        IvParameterSpec zeroIv = new IvParameterSpec(iv);

        SecretKeySpec key = new SecretKeySpec(encryptKey.getBytes(), "DES");

        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

        cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);

        byte[] encryptedData = cipher.doFinal(encryptString.getBytes());

        return Base64DES.encode(encryptedData);
    }

    /**
     * 指定密钥进行解密
     *
     * @param decryptString 密文
     * @param decryptKey    密钥
     * @return 原文
     * @throws Exception
     */
    public static String decryptDES(String decryptString, String decryptKey)
            throws Exception {

        byte[] byteMi = Base64DES.decode(decryptString);

        IvParameterSpec zeroIv = new IvParameterSpec(iv);

        SecretKeySpec key = new SecretKeySpec(decryptKey.getBytes(), "DES");

        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

        cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);

        byte decryptedData[] = cipher.doFinal(byteMi);

        return new String(decryptedData);

    }

    /**
     * 默认密钥加密
     *
     * @param encryptString 原文
     * @return 密文
     */
    public static String encryptDES(String encryptString) {


        IvParameterSpec zeroIv = new IvParameterSpec(iv);

        SecretKeySpec key = new SecretKeySpec(KEY.getBytes(), "DES");

        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
            byte[] encryptedData = cipher.doFinal(encryptString.getBytes());
            String k = Base64DES.encode(encryptedData);
//            k = k.replace("%", java.net.URLEncoder.encode("%", "UTF-8"));
//            k = k.replace("+", java.net.URLEncoder.encode("+", "UTF-8"));
//            k = k.replace(" ", java.net.URLEncoder.encode(" ", "UTF-8"));
//            k = k.replace("/", java.net.URLEncoder.encode("/", "UTF-8"));
//            k = k.replace("?", java.net.URLEncoder.encode("?", "UTF-8"));
//            k = k.replace("#", java.net.URLEncoder.encode("#", "UTF-8"));
//            k = k.replace("&", java.net.URLEncoder.encode("&", "UTF-8"));
//            k = k.replace("=", java.net.URLEncoder.encode("=", "UTF-8"));
            return k;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 默认密钥解密
     *
     * @param decryptString 密文
     * @return 解密得到的原文
     * @throws Exception
     */
    public static String decryptDES(String decryptString)
            throws Exception {

        byte[] byteMi = Base64DES.decode(decryptString);

        IvParameterSpec zeroIv = new IvParameterSpec(iv);

        SecretKeySpec key = new SecretKeySpec(KEY.getBytes(), "DES");

        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

        cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);

        byte decryptedData[] = cipher.doFinal(byteMi);

        return new String(decryptedData, "UTF-8");

    }

    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

}
