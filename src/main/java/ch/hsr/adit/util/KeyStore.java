package ch.hsr.adit.util;

import static org.apache.commons.codec.binary.Hex.decodeHex;
import static org.apache.commons.codec.binary.Hex.encodeHex;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

public class KeyStore {

  private static KeyStore instance = null;
  private static File file;
  private static Properties prop = new Properties();
  private OutputStream out;
  private static InputStream in;
  private static SecretKey secretKey;
  private int secretHash;

  private KeyStore(File file) {
    this.file = file;
  }

  public static KeyStore getInstance(File file) {
    if (instance == null) {

      synchronized (KeyStore.class) {
        // check twice. could be set in the meantime
        if (instance == null) {
          // allocate space only if needed
          instance = new KeyStore(file);
        }
      }
    }

    return instance;
  }
  
  /**
   * Call only when instance has been created with a file earlier
   * @return Keystore instance
   * @throws FileNotFoundException when theres no KeyStore file
   */
  public static KeyStore getInstance() throws FileNotFoundException {
    if (instance == null) {
      throw new FileNotFoundException("Instance doesnt exist yet.");
    }
    synchronized (KeyStore.class) {
      // check twice. could be set in the meantime
      if (instance == null) {
        // allocate space only if needed
        instance = new KeyStore(file);
      }
    }

    return instance;
  }

  /**
   * Saves the generated Key
   * 
   * @throws IOException
   */
  public boolean generateKey() throws NoSuchAlgorithmException, IOException {
    if (secretKey != null) {
      throw new IllegalStateException("Secret already exists!");
    }
    else {
      KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
      keyGenerator.init(256); // 128 default; 192 and 256 also possible
      secretKey = keyGenerator.generateKey();
      return true;
    }
  }

  /**
   * Saves the generated Key
   * 
   * @throws IOException
   */
  public void saveKey() throws IOException {
    char[] hex = encodeHex(secretKey.getEncoded());
    secretHash = hex.hashCode();
    try {
      out = new FileOutputStream(file);
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
    if (file == null) {
      throw new FileNotFoundException("Key file does not exist, call generateKey() first.");
    }
    byte[] encoded = null;
    try {
      in = new FileInputStream(file);
      prop.load(in);
      String secretString = prop.getProperty("secret");
      encoded = decodeHex(secretString.toCharArray());
    } catch (DecoderException e) {
      e.printStackTrace();
    }
    return new SecretKeySpec(encoded, "AES");
  }

  
  public int getSecretHash() {
    return secretHash;
  }

}
