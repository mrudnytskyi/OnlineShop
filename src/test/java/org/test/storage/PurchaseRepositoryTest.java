package org.test.storage;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.test.domain.Product;
import org.test.domain.Purchase;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * @author Myroslav Rudnytskyi
 * @version 25.06.2016
 * @see PurchaseRepositoryJDBC
 */
public class PurchaseRepositoryTest {

	private JdbcTemplate mock;
	private PurchaseRepository purchaseRepository;
	private Purchase expectedPurchase;

	@Before
	public void setUp() throws Exception {
		mock = mock(JdbcTemplate.class);
		purchaseRepository = new PurchaseRepositoryJDBC(mock);
		expectedPurchase = new Purchase(new Product("product", 7), 3);
	}

	@Test
	public void testGetPurchases() throws Exception {
		// setup
		final ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
		final ArgumentCaptor<Object[]> argsCaptor = ArgumentCaptor.forClass(Object[].class);
		when(mock.query(anyString(), (RowMapper) anyObject())).thenAnswer(InvocationOnMock::getArguments);
		// execute
		purchaseRepository.getPurchases(1);
		// verify
		verify(mock).query(sqlCaptor.capture(), argsCaptor.capture(), (RowMapper) anyObject());
		final String actualSql = sqlCaptor.getValue();
		Object[] actualArgs = argsCaptor.getValue();
		assertThat(actualArgs, is(notNullValue()));
		assertThat(actualArgs.length, is(2));
		java.sql.Date actualFromDate = (java.sql.Date) actualArgs[0];
		java.sql.Date actualToDate = (java.sql.Date) actualArgs[1];
		assertThat(actualSql, is(PurchaseRepositoryJDBC.GET_PURCHASES_QUERY));
		// strings to avoid millis comparing
		assertThat(actualFromDate.toString(), is(java.sql.Date.valueOf(LocalDate.now().minusMonths(1)).toString()));
		// strings to avoid millis comparing
		assertThat(actualToDate.toString(), is(java.sql.Date.valueOf(LocalDate.now()).toString()));
	}

	@Test
	public void testGetPurchasesResult() throws Exception {
		// setup
		List<Purchase> expectedPurchases = new ArrayList<Purchase>() {{
			add(expectedPurchase);

		}};
		when(mock.query(anyString(), (Object[]) anyObject(), (RowMapper) anyObject())).thenReturn(expectedPurchases);
		// execute
		final List<Purchase> actualPurchases = purchaseRepository.getPurchases(1);
		// verify
		verify(mock).query(anyString(), (Object[]) anyObject(), (RowMapper) anyObject());
		assertThat(actualPurchases, is(notNullValue()));
		assertThat(actualPurchases.size(), is(1));
		assertThat(actualPurchases.get(0), is(expectedPurchase));
	}

	@Test
	public void testAddPurchase() throws Exception {
		// setup
		final ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
		final ArgumentCaptor<Object> argsCaptor = ArgumentCaptor.forClass(Object.class);
		when(mock.update(anyString())).thenAnswer(InvocationOnMock::getArguments);
		// execute
		purchaseRepository.addPurchase(expectedPurchase, 42);
		// verify
		verify(mock).update(sqlCaptor.capture(), argsCaptor.capture());
		final String actualSql = sqlCaptor.getValue();
		List<Object> actualArgs = argsCaptor.getAllValues();
		assertThat(actualArgs, is(notNullValue()));
		assertThat(actualArgs.size(), is(3));
		Integer actualProductId = (Integer) actualArgs.get(0);
		Integer actualProductsCount = (Integer) actualArgs.get(1);
		Date actualPurchaseDate = (Date) actualArgs.get(2);
		assertThat(actualSql, is(PurchaseRepositoryJDBC.ADD_PURCHASE_QUERY));
		assertThat(actualProductId, is(42));
		assertThat(actualProductsCount, is(expectedPurchase.getProductsCount()));
		// strings to avoid millis comparing
		assertThat(actualPurchaseDate.toString(), is(Date.from(Instant.now()).toString()));
	}
}