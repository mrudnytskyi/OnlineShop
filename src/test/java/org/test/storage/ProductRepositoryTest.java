package org.test.storage;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.test.domain.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * @author Myroslav Rudnytskyi
 * @version 25.06.2016
 * @see ProductRepositoryJDBC
 */
public class ProductRepositoryTest {

	private JdbcTemplate mock;
	private ProductRepository productRepository;
	private Product expectedProduct;

	@Before
	public void setUp() throws Exception {
		mock = mock(JdbcTemplate.class);
		productRepository = new ProductRepositoryJDBC(mock);
		expectedProduct = new Product("test product", 13);
	}

	@Test
	public void testAddProduct() throws Exception {
		// setup
		final ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
		final ArgumentCaptor<Object> argsCaptor = ArgumentCaptor.forClass(Object.class);
		when(mock.update(anyString())).thenAnswer(InvocationOnMock::getArguments);
		// execute
		productRepository.addProduct(expectedProduct);
		// verify
		verify(mock).update(sqlCaptor.capture(), argsCaptor.capture());
		final String actualSql = sqlCaptor.getValue();
		List<Object> actualArgs = argsCaptor.getAllValues();
		assertThat(actualArgs, is(notNullValue()));
		assertThat(actualArgs.size(), is(2));
		String actualProductName = String.valueOf(actualArgs.get(0));
		BigDecimal actualProductPrice = (BigDecimal) actualArgs.get(1);
		assertThat(actualSql, is(ProductRepositoryJDBC.ADD_PRODUCT_QUERY));
		assertThat(actualProductName, is(expectedProduct.getName()));
		assertThat(actualProductPrice, is(expectedProduct.getPrice()));
	}

	@Test
	public void testFindProduct() throws Exception {
		// setup
		final ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
		final ArgumentCaptor<Object[]> argsCaptor = ArgumentCaptor.forClass(Object[].class);
		when(mock.query(anyString(), (RowMapper) anyObject())).thenAnswer(InvocationOnMock::getArguments);
		// execute
		productRepository.findProduct(expectedProduct);
		// verify
		verify(mock).query(sqlCaptor.capture(), argsCaptor.capture(), (RowMapper) anyObject());
		final String actualSql = sqlCaptor.getValue();
		Object[] actualArgs = argsCaptor.getValue();
		assertThat(actualArgs, is(notNullValue()));
		assertThat(actualArgs.length, is(2));
		String actualProductName = String.valueOf(actualArgs[0]);
		BigDecimal actualProductPrice = new BigDecimal((Integer) actualArgs[1]);
		assertThat(actualSql, is(ProductRepositoryJDBC.FIND_PRODUCT_QUERY));
		assertThat(actualProductName, is(expectedProduct.getName()));
		assertThat(actualProductPrice, is(expectedProduct.getPrice()));
	}

	@Test
	public void testFindProductNotFound() throws Exception {
		// setup
		final List methodCall = mock.query(anyString(), (Object[]) anyObject(), (RowMapper) anyObject());
		when(methodCall).thenReturn(Collections.emptyList()).thenAnswer(InvocationOnMock::getArguments);
		// execute
		final Integer actualProductId = productRepository.findProduct(expectedProduct);
		// verify
		verify(mock).query(anyString(), (Object[]) anyObject(), (RowMapper) anyObject());
		assertThat(actualProductId, is(nullValue()));
	}

	@Test
	public void testFindProductFound() throws Exception {
		// setup
		final List<Integer> expectedIds = new ArrayList<Integer>() {{
			add(42);
		}};
		final List methodCall = mock.query(anyString(), (Object[]) anyObject(), (RowMapper) anyObject());
		when(methodCall).thenReturn(expectedIds).thenAnswer(InvocationOnMock::getArguments);
		// execute
		final Integer actualProductId = productRepository.findProduct(expectedProduct);
		// verify
		verify(mock).query(anyString(), (Object[]) anyObject(), (RowMapper) anyObject());
		assertThat(actualProductId, is(expectedIds.get(0)));
	}
}