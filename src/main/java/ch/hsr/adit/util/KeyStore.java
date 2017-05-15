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
import org.apache.log4j.Logger;

public class KeyStore {

  private static final Logger LOGGER = Logger.getLogger(KeyStore.class);

  private static volatile KeyStore instance;

  private File file;
  private Properties prop = new Properties();

  private KeyStore() {}

  public static KeyStore getInstance() {
    if (instance == null) {
      synchronized (KeyStore.class) {
        if (instance == null) {
          instance = new KeyStore();
        }
      }
    }
    return instance;
  }

  public boolean generateKey(File file) throws FileNotFoundException, NoSuchAlgorithmException {
    if (file == null) {
      throw new FileNotFoundException("Please provide a file for the keystore");
    }

    this.file = file;

    if (!file.exists()) {
      KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
      keyGenerator.init(256); // 128 default; 192 and 256 also possible
      SecretKey secretKey = keyGenerator.generateKey();

      saveKey(secretKey, file);
    }

    return true;
  }

  private void saveKey(SecretKey secretKey, File file) {
    char[] hex = encodeHex(secretKey.getEncoded());
    try (OutputStream out = new FileOutputStream(file)) {
      prop.setProperty("secret", String.valueOf(hex));
      prop.store(out, null);
    } catch (IOException e) {
      LOGGER.error("Failed to store secretKey in file: " + file.getAbsolutePath() + ". Message: "
          + e.getMessage());
    }
  }

  public SecretKey loadKey() throws IOException {
    if (file == null) {
      throw new FileNotFoundException("Key file does not exist, call generateKey() first");
    }
    byte[] encoded = null;
    try (InputStream in = new FileInputStream(file)) {
      prop.load(in);
      String secretString = prop.getProperty("secret");
      encoded = decodeHex(secretString.toCharArray());
    } catch (DecoderException e) {
      LOGGER.error("Cannot load and decode given secret " + file.getAbsolutePath() + ". Message: "
          + e.getMessage());
    }
    return new SecretKeySpec(encoded, "AES");
  }

}
