package com.jayasri.receipts.repository;

import java.util.HashMap;
import java.util.UUID;


import org.springframework.stereotype.Component;


import com.jayasri.receipts.domains.Receipt;

@Component
public class ReceiptsRepo {

	HashMap<UUID, Receipt> receiptsMap = new HashMap<>();
	
	public UUID saveReceipt(Receipt receipt) {
		try {
			receiptsMap.put(receipt.getId(), receipt);
			return receipt.getId();			
		} catch (Exception e) {
			System.err.println("Error: "+e);
			throw new RuntimeException("Failed to save receipt", e);
		}
		
	}
	
	public Integer getPoints(UUID id) {
		try {
			return receiptsMap.get(id).getPoints();
			
		} catch (Exception e) {
			System.err.println("Error: "+e);
			throw new RuntimeException("Failed to get Points", e);
		}
		
	}

}
