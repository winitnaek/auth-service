/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author igorV
 */
public class CryptUtils {

    private static final Logger log = LoggerFactory.getLogger(CryptUtils.class);

    public static final String ALGORITHM = "AES";
    public static final String SECURE_RANDOM_ALGORITHM = "SHA1PRNG";
    public static final String SECRET_KEY_ALGORITHM = "PBKDF2WithHmacSHA1";
    public static final String TRANSFORMATION_AES_CBC_ISO10126 = "AES/CBC/ISO10126Padding";
    public static final String TRANSFORMATION_AES_CBC_PKCS5 = "AES/CBC/PKCS5Padding";
    // Must be 16 bytes for AES (128 bit).
    public static final String AES_ENCRYPT_DECRYPT_KEY = "ComplianceFactor";
    // Must be 16 bytes for AES.
    public static final String AES_ENCRYPT_DECRYPT_IV = "0123456789abcdef";
    public static final String DES_ALGORITHM = "DESede";
    public static final String TRANSFORMATION_DES_CFB_NOPAD = "DESede/CFB8/NoPadding";
    public static final String ALL_ZERO_IV = "00000000";

    /**
     * @param is
     * @param os
     * @param encrKey
     * @return
     */
    public static boolean encryptFile(InputStream is, OutputStream os,
            String encrKey) {
        byte[] _key = null;
        byte[] _iv = null;

        try {
            byte[] rawKeyIv = Base64.getDecoder().decode(
                    encrKey.getBytes("UTF-8"));
            _iv = ArrayUtils.subarray(rawKeyIv, 0, 16);
            _key = ArrayUtils.subarray(rawKeyIv, 16, 32);
        } catch (Exception e) {

            if (log.isErrorEnabled()) {
                log.error(" Failed to encrypt File! Encryption tokens are missing! ", e);
            }
            return false;
        }

        return encryptDecrypt(is, os, ALGORITHM, TRANSFORMATION_AES_CBC_PKCS5,
                _key, _iv, Cipher.ENCRYPT_MODE);
    }

    /**
     * @param original
     * @return
     */
    public static String aesEncrypt(String original, String transformation) {
        try {
            // Generate the secret key specs.
            byte[] keyAsBytes = AES_ENCRYPT_DECRYPT_KEY.getBytes();
            SecretKeySpec keySpec = new SecretKeySpec(keyAsBytes, ALGORITHM);
            IvParameterSpec ivParamSpec = new IvParameterSpec(
                    AES_ENCRYPT_DECRYPT_IV.getBytes());

            // Instantiate the cipher
            Cipher cipher = Cipher.getInstance(transformation);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParamSpec);

            if (original != null) {
                byte[] encrypted = cipher.doFinal(original.getBytes());
                return asHex(encrypted);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    /**
     * @param val
     * @return
     */
    public static String aesDecrypt(String encrypted, String transformation) {
        try {
            // Generate the secret key specs.
            byte[] keyAsBytes = AES_ENCRYPT_DECRYPT_KEY.getBytes();
            SecretKeySpec keySpec = new SecretKeySpec(keyAsBytes, ALGORITHM);
            IvParameterSpec ivParamSpec = new IvParameterSpec(
                    AES_ENCRYPT_DECRYPT_IV.getBytes());

            // Instantiate the cipher
            Cipher cipher = Cipher.getInstance(transformation);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParamSpec);

            if (encrypted != null) {
                byte[] original = cipher.doFinal(asByteArray(encrypted));
                String originalString = new String(original);
                return originalString;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

	/**
	 * Must agree with CF code.
	 * 
	 * @param ssn
	 * @return
	 */
    public static String ssnEncrypt(String ssn) {
        try {
            String pre = ssn.substring(2, 3) + ssn.substring(4, 5)
                    + ssn.substring(6, 7);
            String post = ssn.substring(7, 8) + ssn.substring(5, 6)
                    + ssn.substring(3, 4) + ssn.substring(1, 2) + "";
            String obfuscatedSsn = pre + StringUtils.rightPad(ssn, 9, ' ')
                    + post;
            Rijndael rijndael = new Rijndael();
            rijndael.makeKey(AES_ENCRYPT_DECRYPT_KEY.getBytes(), 128,
                    Rijndael.DIR_ENCRYPT);
            byte[] encryptedSsnBytes = new byte[16];
            rijndael.encrypt(obfuscatedSsn.getBytes(), encryptedSsnBytes);
            return asHex(encryptedSsnBytes);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return ssn;
        }
    }
    
    /**
     * Turns array of bytes into string
     *
     * @param buf Array of bytes to convert to hex string
     * @return Generated hex string
     */
    public static String asHex(byte buf[]) {
        StringBuffer strbuf = new StringBuffer(buf.length * 2);
        int i;

        for (i = 0; i < buf.length; i++) {
            if (((int) buf[i] & 0xff) < 0x10) {
                strbuf.append("0");
            }

            strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
        }

        return strbuf.toString();
    }

    /**
     * Turns hex string into array of bytes
     *
     * @param hexStr
     * @return
     */
    public static byte[] asByteArray(String hex) {
        byte[] bts = new byte[hex.length() / 2];

        for (int i = 0; i < bts.length; i++) {
            bts[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2),
                    16);
        }

        return bts;
    }

    /**
     * @return
     */
    public static String generateKey() {
        byte[] randomPsswdBytes = new byte[256];
        char[] randomPsswd = null;
        byte[] randomSalt = new byte[8];

        try {
            SecureRandom.getInstance(SECURE_RANDOM_ALGORITHM).nextBytes(
                    randomPsswdBytes);
            randomPsswd = new String(randomPsswdBytes, "UTF-8").toCharArray();
            SecureRandom.getInstance(SECURE_RANDOM_ALGORITHM).nextBytes(
                    randomSalt);
            SecretKeyFactory factory = SecretKeyFactory
                    .getInstance(SECRET_KEY_ALGORITHM);
            KeySpec spec = new PBEKeySpec(randomPsswd, randomSalt, 65536, 128);
            SecretKey tmpSecret = factory.generateSecret(spec);
            byte[] tmpKey = tmpSecret.getEncoded();
            SecretKey secret = new SecretKeySpec(tmpKey, ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION_AES_CBC_PKCS5);
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(tmpKey,
                    ALGORITHM));
            byte[] iv = cipher.getParameters()
                    .getParameterSpec(IvParameterSpec.class).getIV();
            ByteArrayOutputStream keyOut = new ByteArrayOutputStream();
            keyOut.write(ArrayUtils.addAll(iv, secret.getEncoded()));
            return new String(Base64.getEncoder().encodeToString(
                    keyOut.toByteArray()));
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("Failed to generate encryption key! " + e.getMessage());
            }
            return null;
        }
    }

    /**
     * @param is
     * @param os
     * @param algorithm
     * @param transformation
     * @param key
     * @param iv
     * @param encryptMode
     * @return
     */
    private static boolean encryptDecrypt(InputStream is, OutputStream os,
            String algorithm, String transformation, byte[] key, byte[] iv,
            int mode) {
        // Generate the secret key specs.
        SecretKeySpec keySpec = new SecretKeySpec(key, algorithm);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        try {
            // Instantiate the cipher
            Cipher cipher = Cipher.getInstance(transformation);
            cipher.init(mode, keySpec, ivSpec);

            if (mode == Cipher.ENCRYPT_MODE) {
                CipherInputStream cis = new CipherInputStream(is, cipher);
                IOUtils.copy(cis, os);
                IOUtils.closeQuietly(cis);
            } else if (mode == Cipher.DECRYPT_MODE) {
                CipherOutputStream cos = new CipherOutputStream(os, cipher);
                IOUtils.copy(is, cos);
                cos.flush();
                IOUtils.closeQuietly(cos);
            }

            return true;
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("Failed to encrypt! " + e.getMessage());
            }
            return false;
        }
    }
    
    /**
	 * Must agree with the server code.
	 * 
	 * @param encryptedSsn
	 * @return
	 */
	public static String ssnDecrypt(String encryptedSsn) {
		Rijndael rijndael = new Rijndael();
		rijndael.makeKey(AES_ENCRYPT_DECRYPT_KEY.getBytes(), 128,
				Rijndael.DIR_DECRYPT);

		byte[] origSsnBytes = new byte[16];
		rijndael.decrypt(asByteArray(encryptedSsn), origSsnBytes);

		String originalSsn = new String(origSsnBytes);		
		return originalSsn.substring(3, originalSsn.length() - 4);
	}
}
