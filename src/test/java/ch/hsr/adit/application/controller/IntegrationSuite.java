package ch.hsr.adit.application.controller;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import ch.hsr.adit.application.app.App;
import spark.Spark;

@RunWith(Suite.class)
@Suite.SuiteClasses({ AdvertisementControllerIT.class, UserControllerIT.class,
    TagControllerIT.class, CategoryControllerIT.class })
public class IntegrationSuite {

  @BeforeClass
  public static void setupSuite() {
    App.main(new String[] {});
  }

  @AfterClass
  public static void tearDownSuite() {
    Spark.stop();
  }
}
