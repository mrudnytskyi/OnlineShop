package org.test.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.test.domain.Product;
import org.test.domain.Purchase;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

/**
 * Class provides access to the {@link Purchase}s storage using JDBC.
 *
 * @author Myroslav Rudnytskyi
 * @version 25.06.2016
 */
@Repository
public class PurchaseRepositoryJDBC implements PurchaseRepository {

	public static final String GET_PURCHASES_QUERY = "SELECT * FROM purchases LEFT JOIN products " +
			"ON purchases.product = products.id WHERE purchase_date BETWEEN ? AND ?";
	public static final String ADD_PURCHASE_QUERY =
			"INSERT INTO purchases (product, quantity, purchase_date) VALUES (?, ?, ?)";

	private final JdbcTemplate template;

	@Autowired
	public PurchaseRepositoryJDBC(JdbcTemplate template) {
		this.template = template;
	}

	@Override
	public List<Purchase> getPurchases(int duringPeriod) {
		final LocalDate now = LocalDate.now();
		final Date toDate = Date.valueOf(now);
		final Date fromDate = Date.valueOf(now.minusMonths(duringPeriod));

		return template.query(GET_PURCHASES_QUERY, new Object[]{fromDate, toDate}, (rs, rowNum) ->
				new Purchase(new Product(rs.getString("name"), rs.getInt("price")), rs.getInt("quantity")));
	}

	@Override
	public void addPurchase(Purchase purchase, int productId) {
		template.update(ADD_PURCHASE_QUERY, productId, purchase.getProductsCount(), Date.from(Instant.now()));
	}
}
