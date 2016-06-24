package org.test.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Product domain object.
 *
 * @author Myroslav Rudnytskyi
 * @version 25.06.2016
 */
public class Product {

	private final String name;
	private final BigDecimal price;

	@JsonCreator
	public Product(@JsonProperty("name") String name, @JsonProperty("price") int price) {
		if (name == null || name.isEmpty() || price < 0) throw new IllegalArgumentException();

		this.name = name;
		this.price = new BigDecimal(price);
	}

	public String getName() {
		return name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Product product = (Product) o;
		return Objects.equals(name, product.name) &&
				Objects.equals(price, product.price);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, price);
	}

	@Override
	public String toString() {
		return "Product{" + "name=" + name + ", price=" + price + '}';
	}
}
