package org.test.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.test.domain.Product;

import java.util.List;

/**
 * Class provides access to the {@link Product}s storage using JDBC.
 *
 * @author Myroslav Rudnytskyi
 * @version 25.06.2016
 */
@Repository
public class ProductRepositoryJDBC implements ProductRepository {

	public static final String ADD_PRODUCT_QUERY = "INSERT INTO products (name, price) VALUES (?, ?)";
	public static final String FIND_PRODUCT_QUERY = "SELECT id FROM products WHERE name = ? AND price = ?";
	private final JdbcTemplate template;

	@Autowired
	public ProductRepositoryJDBC(JdbcTemplate template) {
		this.template = template;
	}

	@Override
	public void addProduct(Product product) {
		template.update(ADD_PRODUCT_QUERY, product.getName(), product.getPrice());
	}

	@Override
	public Integer findProduct(Product product) {
		List<Integer> productsIds = template.query(FIND_PRODUCT_QUERY, new Object[]{product.getName(),
				product.getPrice().intValue()}, (rs, rowNum) -> rs.getInt("id"));
		return productsIds.isEmpty() ? null : productsIds.get(0);
	}
}
