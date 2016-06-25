package org.test.storage;

import org.test.domain.Purchase;

import java.util.List;

/**
 * API to access {@code Purchase}s in database.
 *
 * @author Myroslav Rudnytskyi
 * @version 25.06.2016
 */
public interface PurchaseRepository {

	List<Purchase> getPurchases(int duringPeriod);

	void addPurchase(Purchase purchase, int productId);
}
