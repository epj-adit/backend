package ch.hsr.adit.util;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.security.NoSuchAlgorithmException;

import javax.crypto.SecretKey;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class KeyStoreTest {

  private File file;
  private KeyStore keyStore;
  private int secretHash;

  @Before
  public void setUp() throws SecurityException, NoSuchFieldException,
      IllegalArgumentException, IllegalAccessException {
    file = new File("temp.properties");
    //reset Singleton so it can be tested
    Field instance = KeyStore.class.getDeclaredField("instance");
    instance.setAccessible(true);
    instance.set(null, null);
  }

  @After
  public void teardown() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
    file.deleteOnExit();
    Field instance = KeyStore.class.getDeclaredField("instance");
    instance.setAccessible(true);
    instance.set(null, null);
  }

  @Test
  public void generateNewKeyTest() throws NoSuchAlgorithmException, IOException  {
    keyStore = KeyStore.getInstance(file);
    assertTrue(keyStore.generateKey());
  }
  
  @Test
  public void saveKeyTest() throws IOException, NoSuchAlgorithmException {
    keyStore = KeyStore.getInstance(file);
    keyStore.generateKey();
    keyStore.saveKey();
    secretHash = keyStore.getSecretHash();
    assertTrue(file.exists());
  }
  
  @Test
  public void loadKeyTest() throws IOException, NoSuchAlgorithmException {  
    keyStore = KeyStore.getInstance(file);
    keyStore.generateKey();
    keyStore.saveKey();
    int secretHash2 = KeyStore.loadKey().hashCode();
    assertEquals(secretHash2, secretHash2);
  }
  
  @Test(expected = FileNotFoundException.class)
  public void getInstanceWithNoFile() throws FileNotFoundException {
    keyStore = KeyStore.getInstance();
  }
  
  @Test
  public void getInstanceWithFile() throws FileNotFoundException {
    keyStore = KeyStore.getInstance(file);
    KeyStore keyStore2 = KeyStore.getInstance();
    assertEquals(keyStore, keyStore2);
  }
  
}
