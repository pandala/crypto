package encrypt;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AesUtil {
  static final String CIPHER_NAME = "AES/CBC/PKCS5Padding";
  static final SecureRandom secureRandom;

  static {
    secureRandom = new SecureRandom();
  }

  /**
   * AES加密
   * @param key AES key，32 字节
   * @param iv AES IV， 16 字节
   * @param input 明文输入
   * @return 密文输出
   * @throws GeneralSecurityException
   */
  public static byte[] encrypt(byte[] key, byte[] iv, byte[] input) throws GeneralSecurityException {
    Cipher cipher = Cipher.getInstance(CIPHER_NAME);
    SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
    IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
    cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

    return cipher.doFinal(input);
  }

  /**
   * AES解密
   * @param key AES key， 32 字节
   * @param iv AES IV， 16 字节
   * @param input 密文输入
   * @return 明文输出
   * @throws GeneralSecurityException
   */
  public static byte[] decrypt(byte[] key, byte[] iv, byte[] input) throws GeneralSecurityException {
    Cipher cipher = Cipher.getInstance(CIPHER_NAME);
    SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
    IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
    cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
    return cipher.doFinal(input);
  }

  /**
   * 生成32位的密钥
   * @return
   */
  public static byte[] randomKey() {
    return randomBytes(32);
  }

  /**
   * 生成16字节的CBC初始向量
   * @return
   */
  public static byte[] randomIv() {
    return randomBytes(16);
  }

  public static byte[] randomBytes(int size) {
    byte[] buffer = new byte[size];
    secureRandom.nextBytes(buffer);
    return buffer;
  }

  public static void main(String[] args) throws UnsupportedEncodingException, GeneralSecurityException {
    String testStr = "我只是test一下crypto! is ok?";

    byte[] key = randomKey();
    byte[] iv = randomIv();

    byte[] plainText = testStr.getBytes();
    byte[] crypto = encrypt(key, iv, plainText);
    System.out.println(String.valueOf(crypto));
    byte[] decrypt = decrypt(key, iv, crypto);
    System.out.println(new String(decrypt));
  }
}
