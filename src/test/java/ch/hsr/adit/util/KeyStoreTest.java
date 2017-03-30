package ch.hsr.adit.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

  @Before
  public void setUp() throws SecurityException, NoSuchFieldException, IllegalArgumentException,
      IllegalAccessException {
    file = new File("temp.properties");

    // reset Singleton so it can be tested
    Field instance = KeyStore.class.getDeclaredField("instance");
    instance.setAccessible(true);
    instance.set(null, null);
  }

  @After
  public void teardown() throws NoSuchFieldException, SecurityException, IllegalArgumentException,
      IllegalAccessException {
    file.deleteOnExit();
    Field instance = KeyStore.class.getDeclaredField("instance");
    instance.setAccessible(true);
    instance.set(null, null);
  }

  @Test
  public void generateNewKeyTest() throws NoSuchAlgorithmException, IOException {
    keyStore = KeyStore.getInstance();
    assertTrue(keyStore.generateKey(file));
    assertTrue(file.isFile());
  }

  @Test(expected = FileNotFoundException.class)
  public void generateKeyNoFileTest() throws FileNotFoundException, NoSuchAlgorithmException {
    keyStore = KeyStore.getInstance();
    keyStore.generateKey(null);
  }

  @Test
  public void generateKeyTwiceTest() throws IOException, NoSuchAlgorithmException {
    // arrange
    keyStore = KeyStore.getInstance();
    keyStore.generateKey(file);
    SecretKey firstKey = keyStore.loadKey();

    // act
    keyStore.generateKey(file);

    // assert
    assertEquals(firstKey, keyStore.loadKey());
  }
  
  @Test(expected = FileNotFoundException.class)
  public void loadKeyWithoutGenerateTest() throws IOException {
    keyStore = KeyStore.getInstance();
    keyStore.loadKey();
  }

    assertTrue(file.exists());
  }
}
