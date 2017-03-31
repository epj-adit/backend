package ch.hsr.adit.util;

public class EnvironmentUtil {

  public String getEnvVariable(String name) {
    return System.getenv(name);
  }
}
