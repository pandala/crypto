package encrypt;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AesUtil {
  static final String CIPHER_NAME = "AES/CBC/PKCS5Padding";
  static final SecureRandom secureRandom;

  static {
    secureRandom = new SecureRandom();
  }

  public static byte[] encrypt(byte[] key, byte[] iv, byte[] input) throws GeneralSecurityException {
    Cipher cipher = Cipher.getInstance(CIPHER_NAME);
    SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
    IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
    cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

    return cipher.doFinal(input);
  }

  public static void main(String[] args) throws UnsupportedEncodingException {
  }



}
