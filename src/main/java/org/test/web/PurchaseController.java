package org.test.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.test.domain.Purchase;

import java.util.List;

/**
 * Class provides RESTful API for the application.
 *
 * @author Myroslav Rudnytskyi
 * @version 25.06.2016
 */
@RestController
@RequestMapping("/api/v1/purchases")
public class PurchaseController {

	public static final String LIST_PURCHASES_URL = "http://localhost:8090/api/v1/db/?duringPeriod=";
	public static final String ADD_PURCHASE_URL = "http://localhost:8090/api/v1/db/";

	@Autowired
	private RestTemplate restTemplate;

	public PurchaseController() {
	}

	public PurchaseController(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	@SuppressWarnings("unchecked")
	public List<Purchase> listPurchases(@RequestParam(value = "duringPeriod", required = true) int duringPeriod) {
		if (duringPeriod < 0) throw new IllegalArgumentException("Use only positive period parameter, please!");

		return restTemplate.getForObject(LIST_PURCHASES_URL + duringPeriod, List.class);
	}

	@RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void addPurchase(@RequestBody Purchase purchase) {
		if (purchase == null) throw new IllegalArgumentException("Specify purchase request body, please!");

		restTemplate.postForObject(ADD_PURCHASE_URL, purchase, Purchase.class);
	}
}
