package org.test.storage;

import org.test.domain.Product;

/**
 * API to access {@code Product}s in database.
 *
 * @author Myroslav Rudnytskyi
 * @version 25.06.2016
 */
public interface ProductRepository {

	void addProduct(Product product);

	Integer findProduct(Product product);
}
