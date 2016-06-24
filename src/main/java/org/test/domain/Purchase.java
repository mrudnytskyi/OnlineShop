package org.test.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Purchase domain object.
 *
 * @author Myroslav Rudnytskyi
 * @version 25.06.2016
 */
public class Purchase {
	private final Product product;
	private final int productCount;
	private final BigDecimal sum;

	@JsonCreator
	public Purchase(@JsonProperty("product") Product product, @JsonProperty("productsCount") int productsCount) {
		if (product == null || productsCount < 0) throw new IllegalArgumentException();

		this.product = product;
		this.productCount = productsCount;
		sum = product.getPrice().multiply(new BigDecimal(productCount));
	}

	public Product getProduct() {
		return product;
	}

	public int getProductsCount() {
		return productCount;
	}

	public BigDecimal getSum() {
		return sum;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Purchase purchase = (Purchase) o;
		return productCount == purchase.productCount &&
				Objects.equals(product, purchase.product);
	}

	@Override
	public int hashCode() {
		return Objects.hash(product, productCount);
	}

	@Override
	public String toString() {
		return "Purchase{" + "product=" + product + ", productCount=" + productCount + '}';
	}
}
