package com.shizy.utils.auth;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * 安全工具类。包括加密、解密等各个算法。
 * 字符与字节的转换统一使用UTF-8。
 *
 * @author ningjh
 * @since 2016-08-10
 */

public final class SecurityUtil {
    /**
     * AES密钥长度
     */
    public final static int AES_KEY_LENGTH = 16;

    /**
     * RSA算法每次加密的字节不能超过117，超过需要分段加密
     */
    private final static int RSA_MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA算法每次解密的字节不能超过128，超过需要分段解密
     */
    private final static int RSA_MAX_DECRYPT_BLOCK = 128;


    /**
     * AES算法对称密钥生成器，线程安全
     */
    private static KeyGenerator keyGenForAES;

    static {
        try {
            // 预先初始化密钥生成器，会大大提高加密和解密的过程
            keyGenForAES = KeyGenerator.getInstance("AES");
            keyGenForAES.init(128);
        } catch (NoSuchAlgorithmException e) {
//            LogUtil.error("初始化AES密钥生成器失败", e);
        }
    }

    /**
     * 生成非对称密钥对
     *
     * @return 返回密钥对的map<br>
     * 私钥=map.get("privateKey")<br>
     * 公钥=map.get("publicKey")
     */
    public static Map<String, String> genRSAKeyPair() throws Exception {

        Map<String, String> result = new HashMap<String, String>();

        // 初始化RSA算法
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(1024);

        // 生成密钥对
        KeyPair keyPair = generator.generateKeyPair();

        // 获取密钥对字符串表示形式
        result.put("privateKey", encryptBASE64(keyPair.getPrivate().getEncoded()));
        result.put("publicKey", encryptBASE64(keyPair.getPublic().getEncoded()));

        return result;


    }

    /**
     * RSA每次加密数据不能超过117字节，超过要分段处理
     *
     * @param cipher
     * @param input
     * @return
     * @throws Exception
     */
    private static byte[] encryptRSA(Cipher cipher, byte[] input) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(RSA_MAX_ENCRYPT_BLOCK);
        int offset = 0;
        int len = 0;

        while (input.length - offset > 0) {
            if (input.length - offset >= RSA_MAX_ENCRYPT_BLOCK) {
                len = RSA_MAX_ENCRYPT_BLOCK;
            } else {
                len = input.length - offset;
            }

            bos.write(cipher.doFinal(input, offset, len));

            offset = offset + len;
        }

        return bos.toByteArray();
    }

    /**
     * RSA每次解密数据不能超过128字节，超过要分段处理
     *
     * @param cipher
     * @param input
     * @return
     * @throws Exception
     */
    private static byte[] decryptRSA(Cipher cipher, byte[] input) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(RSA_MAX_DECRYPT_BLOCK);
        int offset = 0;
        int len = 0;

        while (input.length - offset > 0) {
            if (input.length - offset >= RSA_MAX_DECRYPT_BLOCK) {
                len = RSA_MAX_DECRYPT_BLOCK;
            } else {
                len = input.length - offset;
            }

            bos.write(cipher.doFinal(input, offset, len));

            offset = offset + len;
        }

        return bos.toByteArray();
    }

    /**
     * 使用私钥对数据加密
     *
     * @param data 需要加密的数据
     * @return 加密后的字节流使用BASE64再次加密
     */
    public static String encryptRSAByPrivateKey(String data, String rsaPrivateKey) throws Exception {

        // 获取私钥的字节表示
        byte[] encodedKey = decryptBASE64(rsaPrivateKey);

        // 初始化私钥
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encodedKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        Key privateKey = keyFactory.generatePrivate(keySpec);

        // 使用私钥加密数据
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);

        // 私钥加密后的字节流，用BASE64再次加密
        return encryptBASE64(encryptRSA(cipher, toUTF8Bytes(data)));

    }

    /**
     * 使用私钥对数据解密
     *
     * @param data 源数据使用公钥加密后，再用BASE64加密
     * @return 返回明文数据
     */
    public static String decryptRSAByPrivateKey(String data, String rsaPrivateKey) throws Exception {

        // 获取私钥的字节表示
        byte[] encodedKey = decryptBASE64(rsaPrivateKey);

        // 初始化私钥
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encodedKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        Key privateKey = keyFactory.generatePrivate(keySpec);

        // 使用私钥解密数据
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        // 使用BASE64先解密，然后用私钥再次解密
        return toUTF8String(decryptRSA(cipher, decryptBASE64(data)));

    }

    /**
     * 使用公钥对数据加密
     *
     * @param data 需要加密的数据
     * @return 加密后的字节流使用BASE64再次加密
     */
    public static String encryptRSAByPublicKey(String data, String rsaPublicKey) throws Exception {

        // 获取公钥的字节表示
        byte[] encodedKey = decryptBASE64(rsaPublicKey);

        // 初始化公钥
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encodedKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        Key publicKey = keyFactory.generatePublic(keySpec);

        // 使用公钥加密数据
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        // 使用公钥加密后，再用BASE64加密
        return encryptBASE64(encryptRSA(cipher, toUTF8Bytes(data)));

    }

    /**
     * 使用公钥对数据解密
     *
     * @param data 源数据使用私钥钥加密后，再用BASE64加密
     * @return 返回明文数据
     */
    public static String decryptRSAByPublicKey(String data, String rsaPublicKey) throws Exception {

        // 获取公钥的字节表示
        byte[] encodedKey = decryptBASE64(rsaPublicKey);

        // 初始化公钥
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encodedKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        Key publicKey = keyFactory.generatePublic(keySpec);

        // 使用公钥解密数据
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);

        // 使用BASE64先解密，然后用公钥再次解密
        return toUTF8String(decryptRSA(cipher, decryptBASE64(data)));

    }

    /**
     * 生成128位对称密钥
     *
     * @return 返回经过BASE64编码的密钥
     */
    public static String genAESKey() throws Exception {

        // 生成AES密钥
        SecretKey secretKey = keyGenForAES.generateKey();

        // 将密钥用BASE64编码
        return encryptBASE64(secretKey.getEncoded());

    }

    /**
     * 对称算法AES加密数据
     *
     * @param data 明文数据
     * @param key  BASE64加密后的对称密钥
     * @return 加密后的数据，用BASE64再次加密返回
     */
    public static String encryptAES(String data, String key) throws Exception {

        // 128位密钥
        byte[] encodedKey = decryptBASE64(key);

        // 初始化加密算法
        SecretKeySpec keySpec = new SecretKeySpec(encodedKey, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);

        // 加密后，再用BASE64加密
        return encryptBASE64(cipher.doFinal(toUTF8Bytes(data)));

    }

    /**
     * 对称算法AES解密数据
     *
     * @param data 源数据使用AES加密后，再用BASE64加密
     * @param key  BASE64加密后的对称密钥
     * @return
     */
    public static String decryptAES(String data, String key) throws Exception {

        // 128位密钥
        byte[] encodedKey = decryptBASE64(key);

        // 初始化解密算法
        SecretKeySpec keySpec = new SecretKeySpec(encodedKey, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);

        // 使用BASE64先解密，然后用对称密钥再次解密
        return toUTF8String(cipher.doFinal(decryptBASE64(data)));

    }

    /**
     * BASE64加密算法
     *
     * @param data 需要加密的数据
     * @return 返回加密后的字符串
     */
    public static String encryptBASE64(byte[] data) {
        return new Base64().encodeToString(data);
    }

    /**
     * BASE64解密算法
     *
     * @param data 需要解密的字符串
     * @return 返回解密后的字节数组
     */
    public static byte[] decryptBASE64(String data) {
        return new Base64().decode(data);
    }

    /**
     * 把字符串转换成UTF8编码的字节数组
     *
     * @param data
     * @return
     */
    public static byte[] toUTF8Bytes(String data) throws Exception {

        return data.getBytes(StandardCharsets.UTF_8);

    }

    /**
     * 把UTF8编码的字节数组转换为字符串
     *
     * @param data
     * @return
     */
    public static String toUTF8String(byte[] data) throws Exception {

        return new String(data, StandardCharsets.UTF_8);

    }

    /**
     * SHA1加密
     *
     * @param data
     * @return
     */
    public static String encryptSHA1(String data) throws Exception {

        MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
        messageDigest.update(toUTF8Bytes(data));
        return byteToHex(messageDigest.digest());

    }


    public static String encryptMD5(String data) throws Exception {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(toUTF8Bytes(data));
        return byteToHex(messageDigest.digest());
    }

    /**
     * 字节数组转换为大写十六进制字符串
     *
     * @param data
     * @return
     */
    public static String byteToHex(byte[] data) {
        StringBuilder hexString = new StringBuilder();

        for (int i = 0; i < data.length; i++) {
            String hex = Integer.toHexString(data[i] & 0xFF);

            if (hex.length() < 2) {
                hexString.append('0');
            }

            hexString.append(hex);
        }

        return hexString.toString().toUpperCase();
    }

    /**
     * 验证SHA1签名
     *
     * @param data      源数据
     * @param signature SHA1签名
     * @return true：签名一致 <br> false：签名不一致
     */
    public static boolean verifySHA1Signature(String data, String signature) throws Exception {
        if (data == null || signature == null) {
            return false;
        }

        return signature.equals(encryptSHA1(data));

    }
}
