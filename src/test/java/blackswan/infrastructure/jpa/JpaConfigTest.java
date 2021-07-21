package blackswan.infrastructure.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.util.Map;
import javax.sql.DataSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.transaction.jta.JtaTransactionManager;

/**
 * Test class for JpaConfig class.
 *
 * @author David Molnar
 */
@RunWith(MockitoJUnitRunner.class)
public class JpaConfigTest {

  @Mock
  private DataSource dataSource;
  @Mock
  private JpaProperties properties;
  @Mock
  private ObjectProvider<JtaTransactionManager> jtaTransactionManager;
  @Mock
  private ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers;
  @InjectMocks
  private JpaConfig testedObject;

  @Test
  public void createJpaVendorAdapter() {
    assertNotNull(testedObject.createJpaVendorAdapter());
  }

  @Test
  public void getVendorProperties() {
    Map<String, Object> result = testedObject.getVendorProperties();

    assertEquals(1, result.size());
    assertSame(dataSource, result.get("javax.persistence.nonJtaDataSource"));
  }
}
