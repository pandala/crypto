package encrypt;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

import javax.crypto.Cipher;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSAKeyPair {
    final PrivateKey privateKey;
    final PublicKey publicKey;

    static {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }

    public RSAKeyPair(String publicKey, String privateKey) {
        this(new StringReader(publicKey), new StringReader(privateKey));
    }

    public RSAKeyPair(Reader publicKeyReader, Reader privateKeyReader) {
        this.publicKey = readPublicKey(publicKeyReader);
        this.privateKey = readPrivateKey(privateKeyReader);
    }

    static PrivateKey readPrivateKey(String text) {
        return readPrivateKey(new StringReader(text));
    }

    static PrivateKey readPrivateKey(Reader reader) {
        try (PemReader preader = new PemReader(reader)) {
            PemObject po = preader.readPemObject();
            byte[] encoded = po.getContent();
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(keySpec);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    static PublicKey readPublicKey(String text) {
        return readPublicKey(new StringReader(text));
    }

    static PublicKey readPublicKey(Reader reader) {
        try (PemReader preader = new PemReader(reader)) {
            PemObject po = preader.readPemObject();
            byte[] encoded = po.getContent();
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePublic(keySpec);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 用私钥加密
     *
     * @param message 明文
     * @return 密文
     * @throws GeneralSecurityException 加密失败
     */
    public byte[] encryptByPrivateKey(byte[] message) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, this.privateKey);
        return cipher.doFinal(message);
    }

    /**
     * 用私钥解密
     *
     * @param input 密文
     * @return 明文
     * @throws GeneralSecurityException 解密失败
     */
    public byte[] decryptByPrivateKey(byte[] input) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, this.privateKey);
        return cipher.doFinal(input);
    }

    /**
     * 用公钥解密
     *
     * @param input 密文
     * @return 明文
     * @throws GeneralSecurityException 解密失败
     */
    public byte[] decryptByPublicKey(byte[] input) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, this.publicKey);
        return cipher.doFinal(input);
    }

    /**
     * 用公钥加密
     *
     * @param message 明文
     * @return 密文
     * @throws GeneralSecurityException 加密失败
     */
    public byte[] encryptByPublicKey(byte[] message) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, this.publicKey);
        return cipher.doFinal(message);
    }

    /**
     * 用私钥进行SHA1withRSA签名
     *
     * @param message 明文
     * @return 签名
     * @throws GeneralSecurityException 签名失败
     */
    public byte[] sign(byte[] message) throws GeneralSecurityException {
        Signature signature = Signature.getInstance("SHA1withRSA");
        signature.initSign(this.privateKey);
        signature.update(message);
        return signature.sign();
    }

    /**
     * 用公钥验证SHA1withRSA签名
     *
     * @param message 明文
     * @param sign 签名
     * @return 签名是否有效，true=有效，false=无效
     * @throws GeneralSecurityException 验证失败
     */
    public boolean verify(byte[] message, byte[] sign) throws GeneralSecurityException {
        Signature signature = Signature.getInstance("SHA1withRSA");
        signature.initVerify(this.publicKey);
        signature.update(message);
        return signature.verify(sign);
    }

    public String getEncodedPublicKey() {
        StringBuilder sb = new StringBuilder(4096);
        sb.append("-----BEGIN PUBLIC KEY-----\n");
        String s = Base64.getEncoder().encodeToString(this.publicKey.getEncoded());
        while (s.length() > 76) {
            sb.append(s.substring(0, 76)).append('\n');
            s = s.substring(76);
        }
        sb.append("-----END PUBLIC KEY-----");
        return sb.toString();
    }

    public static void main(String[] args) throws GeneralSecurityException {
        String priStr = "-----BEGIN PRIVATE KEY-----\n" +
                "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQDQMELqGN4inRiO\n" +
                "SMc4DMROEt8HGulPcOrIW873uUa0t319s2ATlaXvojybA8UqYsKlE7jDstTLo2Lo\n" +
                "P5JJTpIDwsd1SUyODi+5pIICKCm5uvkCPjxFN4gtg6Uv+2UKE7YIFcffg+34Av4l\n" +
                "LVIQBGVTrFezT7S4aNrljDzX85ceR2HrZlkdtccvW1wRMQVVHEMoEeVtPvnF6tvW\n" +
                "6E8h9QJWzzyVuUxIRjCYeOBSsL+FqNtZuhKavJ4NJe1cIvTjUpS5zuln8LMzlWr3\n" +
                "cTS//Ae7qeZHTUCJS/bRbOrv+IW0lbPb9i4HLVRifCvFVZdr6WIAA1W3SrJZVw5u\n" +
                "IikC5y3pAgMBAAECggEAfQ8Aoy8dAR3tSktdoMXKhju/zbzUuevSnVjJEvYxCw3O\n" +
                "TFwcMqeKblC+pz+0iKma0x7ttmEOguU63NiTP4X7IFc5CbNmUU8kTBgOIM2PgwpV\n" +
                "2osYGVwqaWzTJeeV4l/E0UaEMjhGEt15XUvEJ+r8Ey9L32LZ5IzYMVv0kKS6+mxA\n" +
                "IVEj7TdX/1hQ4Ahj/gfmJgnJUGg89/LlaivPfCmAzBlIYSmcctKIlV4sChNxlr8N\n" +
                "ARyXFadyKwuFFtO8UNXyYUIJVk9clJ8sZVZa9mBBnjA/r1c7xYbsWGpRo6Z3z0Y6\n" +
                "dra0ui3Bdqo+ArZaBgYvxzCf/a6XLgrq9BWyaw64gQKBgQD/DImWZG0YnBqyEkLh\n" +
                "d3A3akbm7LNc+rLgRmYjLdQng0UZs2xH9alTcFVwDdXDn3GOHJyGLB655QBHMXtt\n" +
                "OHJyzZw9jTfn2pfXsB/R7e7o1eTTl8rTs5QuQrstmF+6RZcNc2E073SaVcoqxR5O\n" +
                "jr9L5zOLQSwM0UMg+3TlOuAI7QKBgQDQ9v4FRUDy43fo8KsBcAlYl1lh5tzIpajj\n" +
                "L210yNbzjWaGLZBL+ukNzEaYouoZObUDrbuJtl1VSdH/NoKS7TBUVktCxI5TW+5u\n" +
                "KFjSvbCS3VSC5kuthOkJOZhW7JZGbZpXJZ9AQMAxuS0Os9HCToTTrpSLDsLmehH/\n" +
                "CbE6aZnFbQKBgAopQrYGLD6FwZ6EkbuP2Z2rk0WR+pdiuYur4lfWdJRaa39ZoZ/A\n" +
                "Ts2vNQgw1OplSM3jn+zMhzDayccMVqGEVTLztBzTqn2HPPNv5eSkORd3phUz//Wt\n" +
                "OuMY74IihvXR3fHAPggw3hFkB5jqn4l7sG1iua/7mbitAaKnwgF79f8FAoGAbuB/\n" +
                "jKBghAB6YZlBw3OFIgK8pJvwzm5IHLPPqkXe+EAxBEUs6YPrIS0g0GCxQk1CZP9M\n" +
                "IRgokdannHl58yfzmuxm5riQpF6FNuIlrzyDcooKC6LSyPFbDVAIKydB3YDOgisu\n" +
                "QtOYp0mQRNrZE/bBt+hMXTSp6c0fu2mTJmFd/EkCgYBhcLka5pCTC7kIXxEglljr\n" +
                "FMTXjGF+zQLQMFRS87QeYag6cYwqcGWRESI/9hblhtREm5g69EQ/VgM6gLsbLHnl\n" +
                "1h3WsyR86InPODH7oMhfdD3jxNDz4Jy2X6Tp77D5TQEahTdqFC2wESZwB8fxr4qx\n" +
                "xEqwKYWoqF4li8fABrdR2Q==\n" + "-----END PRIVATE KEY-----";

        String pubStr = "-----BEGIN PUBLIC KEY-----\n" +
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA0DBC6hjeIp0YjkjHOAzE\n" +
                "ThLfBxrpT3DqyFvO97lGtLd9fbNgE5Wl76I8mwPFKmLCpRO4w7LUy6Ni6D+SSU6S\n" +
                "A8LHdUlMjg4vuaSCAigpubr5Aj48RTeILYOlL/tlChO2CBXH34Pt+AL+JS1SEARl\n" +
                "U6xXs0+0uGja5Yw81/OXHkdh62ZZHbXHL1tcETEFVRxDKBHlbT75xerb1uhPIfUC\n" +
                "Vs88lblMSEYwmHjgUrC/hajbWboSmryeDSXtXCL041KUuc7pZ/CzM5Vq93E0v/wH\n" +
                "u6nmR01AiUv20Wzq7/iFtJWz2/YuBy1UYnwrxVWXa+liAANVt0qyWVcObiIpAuct\n" +
                "6QIDAQAB\n" + "-----END PUBLIC KEY-----";

        RSAKeyPair rsaKeyPair = new RSAKeyPair(pubStr, priStr);

        String testStr = "我只是测试一下rsa！123，abc";
        byte[] orcBytes = testStr.getBytes();

        byte[] pubEnc = rsaKeyPair.encryptByPublicKey(orcBytes);
        byte[] priDec = rsaKeyPair.decryptByPrivateKey(pubEnc);
        System.out.println(new String(priDec));

        byte[] priEnc = rsaKeyPair.encryptByPrivateKey(orcBytes);
        byte[] pubDec = rsaKeyPair.decryptByPublicKey(priEnc);
        System.out.println(new String(pubDec));

        byte[] sign = rsaKeyPair.sign(orcBytes);
        boolean result = rsaKeyPair.verify(orcBytes, sign);
        System.out.println(result);
        System.out.println(new String(sign));




    }
}
