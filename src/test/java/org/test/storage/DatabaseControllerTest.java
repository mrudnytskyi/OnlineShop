package org.test.storage;

import org.junit.BeforeClass;
import org.junit.Test;
import org.test.domain.Product;
import org.test.domain.Purchase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;

/**
 * @author Myroslav Rudnytskyi
 * @version 25.06.2016
 * @see DatabaseController
 */
public class DatabaseControllerTest {

	private static final ProductRepository productMock = mock(ProductRepository.class);
	private static final PurchaseRepository purchaseMock = mock(PurchaseRepository.class);
	private static final DatabaseController databaseController = new DatabaseController(productMock, purchaseMock);

	private static final Purchase expectedPurchase = new Purchase(new Product("test", 3), 3);
	private static final Product productInDB = new Product("product", 7);
	private static final Product productNotInDB = new Product("test product", 3);

	@BeforeClass
	public static void setUp() throws Exception {
		when(purchaseMock.getPurchases(1)).thenReturn(new ArrayList<Purchase>() {{
			add(expectedPurchase);
		}});
		when(productMock.findProduct(productInDB)).thenReturn(42);
		when(productMock.findProduct(productNotInDB)).thenReturn(null).thenReturn(43);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testListPurchasesWrongParameter() throws Exception {
		databaseController.listPurchases(-1);
	}

	@Test
	public void testListPurchasesEmpty() throws Exception {
		// execute
		final List<Purchase> actualPurchases = databaseController.listPurchases(0);
		// verify
		assertThat(actualPurchases, is(notNullValue()));
		assertThat(actualPurchases, is(Collections.emptyList()));
	}

	@Test
	public void testListPurchases() throws Exception {
		// execute
		final List<Purchase> actualPurchases = databaseController.listPurchases(1);
		// verify
		assertThat(actualPurchases, is(notNullValue()));
		assertThat(actualPurchases.size(), is(1));
		assertThat(actualPurchases.get(0), is(expectedPurchase));
	}

	@Test
	public void testAddPurchaseProductInDatabase() throws Exception {
		// execute
		databaseController.addPurchase(new Purchase(productInDB, 7));
		// verify
		verify(productMock).findProduct(eq(productInDB));
		verify(purchaseMock).addPurchase(anyObject(), eq(42));
	}

	@Test
	public void testAddPurchaseProductNotInDatabase() throws Exception {
		// execute
		databaseController.addPurchase(new Purchase(productNotInDB, 5));
		// verify
		verify(productMock, times(2)).findProduct(eq(productNotInDB));
		verify(productMock).addProduct(eq(productNotInDB));
		verify(purchaseMock).addPurchase(anyObject(), eq(43));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddPurchaseWrongParameter() throws Exception {
		databaseController.addPurchase(null);
	}
}