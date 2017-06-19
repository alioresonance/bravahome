/*
 * *********************************************************
 *  Copyright (c) 2017 Brava Home Inc.  All rights reserved.
 * *********************************************************
 */
package bravahome.common.deployment;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.spec.KeySpec;
import javax.crypto.*;
import javax.crypto.spec.DESedeKeySpec;
import org.apache.commons.codec.binary.Base64;

/*
 * Created by gzoldi on 02/09/2017 08:43 AM
 */
public class Encoder {

    private static final String UNICODE_FORMAT = "UTF8";
    private static final String DESEDE_ENCRYPTION_SCHEME = "DESede";

    private KeySpec ks;
    private SecretKeyFactory skf;
    private Cipher cipher;
    private byte[] arrayBytes;
    private String myEncryptionScheme;
    private SecretKey key;

    public Encoder(String param) {
        try {
            myEncryptionScheme = DESEDE_ENCRYPTION_SCHEME;
            arrayBytes = param.getBytes(UNICODE_FORMAT);
            ks = new DESedeKeySpec(arrayBytes);
            skf = SecretKeyFactory.getInstance(myEncryptionScheme);
            cipher = Cipher.getInstance(myEncryptionScheme);
            key = skf.generateSecret(ks);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String signCertificate(String param) {
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] encryptedText = Base64.decodeBase64(param);
            byte[] plainText = cipher.doFinal(encryptedText);
            return new String(plainText);
        }
        catch (InvalidKeyException ike) {
            throw new RuntimeException(ike);
        }
        catch (BadPaddingException bpe) {
            throw new RuntimeException(bpe);
        }
        catch (IllegalBlockSizeException ibse) {
            throw new RuntimeException(ibse);
        }
    }

    public String decryptCertKey(String param) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] plainText = param.getBytes(UNICODE_FORMAT);
            byte[] encryptedText = cipher.doFinal(plainText);
            return new String(Base64.encodeBase64(encryptedText));
        }
        catch (InvalidKeyException ike) {
            throw new RuntimeException(ike);
        }
        catch (BadPaddingException bpe) {
            throw new RuntimeException(bpe);
        }
        catch (UnsupportedEncodingException uee) {
            throw new RuntimeException(uee);
        }
        catch (IllegalBlockSizeException ibse) {
            throw new RuntimeException(ibse);
        }
    }

}