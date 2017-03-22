package ch.hsr.adit.util;

import static org.apache.commons.codec.binary.Hex.*;
import static org.apache.commons.io.FileUtils.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.DecoderException;

public final class KeyStore {

  private static Properties prop = new Properties();
  private static OutputStream out = null;
  private static InputStream in = null;
  private static SecretKey secretKey = null;

  /**
   * Generates a new SecretKey thats used by jwt
   * Should only be called once since the secret is permanent
   * @throws NoSuchAlgorithmException
   */
  public static void generateKey() throws NoSuchAlgorithmException {
    KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
    keyGenerator.init(256); // 128 default; 192 and 256 also possible
    secretKey = keyGenerator.generateKey();
  }
  /**
   * Saves the generated Key
   * @throws IOException
   */
  public static void saveKey() throws IOException {
    char[] hex = encodeHex(secretKey.getEncoded());
    try {
      out = new FileOutputStream("KeyStore.properties");
      prop.setProperty("secret", String.valueOf(hex));
      prop.store(out, null);

    } catch (IOException io) {
      io.printStackTrace();
    } finally {
      if (out != null) {
        try {
          out.close();
          prop = null;
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

    }
  }
  /**
   * Loads the permanent Key from the KeyStore.properties file
   * 
   * @return the SecretKey
   * @throws IOException
   */
  public static SecretKey loadKey() throws IOException {
    byte[] encoded = null;
    try {
      in = new FileInputStream("KeyStore.properties");
      prop.load(in);
      String secretString = prop.getProperty("secret");
      encoded = decodeHex(secretString.toCharArray());
    } catch (DecoderException e) {
      e.printStackTrace();
    }
    return new SecretKeySpec(encoded, "AES");
  }


}
