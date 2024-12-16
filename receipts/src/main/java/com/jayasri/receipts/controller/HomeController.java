package com.jayasri.receipts.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jayasri.receipts.domains.Receipt;
import com.jayasri.receipts.dto.PointsDTO;
import com.jayasri.receipts.dto.UuidDTO;
import com.jayasri.receipts.service.ReceiptService;


@RestController
@RequestMapping("/receipts")
public class HomeController {

	@Autowired
	ReceiptService receiptService;
	
	@PostMapping("/process")
	public ResponseEntity<UuidDTO> process(@RequestBody Receipt receipt) {
		return ResponseEntity.ok(receiptService.processReceipts(receipt));
	}
	
	@GetMapping("/{id}/points")
	public ResponseEntity<PointsDTO> getPoints(@PathVariable("id") UUID id) {
		return ResponseEntity.ok(receiptService.getPoints(id));
	}
}
