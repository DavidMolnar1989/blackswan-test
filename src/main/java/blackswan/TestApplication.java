package blackswan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableScheduling
public class TestApplication {

  /**
   * Facilitates running the Black Swan Test Service from the included embedded container.
   *
   * @param args CLI arguments
   */
  public static void main(String[] args) {
    SpringApplication.run(TestApplication.class, args);
  }

}
