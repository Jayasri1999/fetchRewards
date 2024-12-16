package com.jayasri.receipts.domains;

import java.util.ArrayList;
import java.util.UUID;


import lombok.Data;


@Data
public class Receipt {
	

	private UUID id;
	
	String retailer;
	String purchaseDate;
	String purchaseTime;
	ArrayList<Item> items;
	String total;
	Integer points;
	
	public Receipt() {
		this.id = UUID.randomUUID();
	}
}
