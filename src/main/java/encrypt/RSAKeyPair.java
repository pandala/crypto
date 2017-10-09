package encrypt;

import java.io.Reader;
import java.io.StringReader;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;

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

    static PrivateKey readPrivateKey(Reader reader) {
        return null;
    }

    static PublicKey readPublicKey(Reader reader) {
        return null;
    }

}
