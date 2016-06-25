package org.test.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.test.domain.Product;
import org.test.domain.Purchase;

import java.util.List;

/**
 * Class provides RESTful API for the application database.
 *
 * @author Myroslav Rudnytskyi
 * @version 25.06.2016
 */
@RestController
@RequestMapping("/api/v1/db")
public class DatabaseController {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private PurchaseRepository purchaseRepository;

	public DatabaseController() {
	}

	public DatabaseController(ProductRepository productRepository, PurchaseRepository purchaseRepository) {
		this.productRepository = productRepository;
		this.purchaseRepository = purchaseRepository;
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public List<Purchase> listPurchases(@RequestParam(value = "duringPeriod", required = true) int duringPeriod) {
		if (duringPeriod < 0) throw new IllegalArgumentException("Use only positive period parameter, please!");

		return purchaseRepository.getPurchases(duringPeriod);
	}

	@RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void addPurchase(@RequestBody Purchase purchase) {
		if (purchase == null) throw new IllegalArgumentException("Specify purchase request body, please!");

		Product product = purchase.getProduct();
		Integer id = productRepository.findProduct(product);
		if (id == null) {
			productRepository.addProduct(product);
			id = productRepository.findProduct(product);
		}
		purchaseRepository.addPurchase(purchase, id);
	}
}
