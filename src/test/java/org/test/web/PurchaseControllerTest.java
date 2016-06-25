package org.test.web;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.web.client.RestTemplate;
import org.test.domain.Product;
import org.test.domain.Purchase;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.contains;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * @author Myroslav Rudnytskyi
 * @version 26.06.2016
 * @see PurchaseController
 */
public class PurchaseControllerTest {

	private final Purchase purchase = new Purchase(new Product("test product", 5), 7);
	private RestTemplate mock;
	private PurchaseController purchaseController;

	@Before
	public void setUp() throws Exception {
		mock = mock(RestTemplate.class);
		purchaseController = new PurchaseController(mock);
	}

	@Test
	public void testListPurchases() throws Exception {
		// setup
		when(mock.getForObject(contains(PurchaseController.LIST_PURCHASES_URL), eq(List.class)))
				.thenReturn(new ArrayList<Purchase>() {{
					add(purchase);
				}});
		// execute
		final List<Purchase> actualPurchases = purchaseController.listPurchases(2);
		// verify
		verify(mock).getForObject(contains(PurchaseController.LIST_PURCHASES_URL), eq(List.class));
		assertThat(actualPurchases, is(notNullValue()));
		assertThat(actualPurchases.size(), is(1));
		assertThat(actualPurchases.get(0), is(purchase));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testListPurchasesWrongParameter() throws Exception {
		purchaseController.listPurchases(-2);
	}

	@Test
	public void testAddPurchase() throws Exception {
		// setup
		final ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
		final ArgumentCaptor<Object> purchaseCaptor = ArgumentCaptor.forClass(Object.class);
		final ArgumentCaptor<Class> classCaptor = ArgumentCaptor.forClass(Class.class);
		when(mock.getForObject(contains(PurchaseController.ADD_PURCHASE_URL), anyObject(), eq(Purchase.class)))
				.then(InvocationOnMock::getArguments);
		// execute
		purchaseController.addPurchase(purchase);
		// verify
		verify(mock).postForObject(sqlCaptor.capture(), purchaseCaptor.capture(), classCaptor.capture());
		final String actualSql = sqlCaptor.getValue();
		final Object actualPurchase = purchaseCaptor.getValue();
		final Class actualClass = classCaptor.getValue();
		Assert.assertThat(actualSql, is(PurchaseController.ADD_PURCHASE_URL));
		Assert.assertThat(actualPurchase, is(purchase));
		Assert.assertThat(actualClass, is(instanceOf(Class.class)));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddPurchaseWrongParameter() throws Exception {
		purchaseController.addPurchase(null);
	}
}