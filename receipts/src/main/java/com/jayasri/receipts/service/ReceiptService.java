package com.jayasri.receipts.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jayasri.receipts.domains.Item;
import com.jayasri.receipts.domains.Receipt;
import com.jayasri.receipts.dto.PointsDTO;
import com.jayasri.receipts.dto.UuidDTO;
import com.jayasri.receipts.repository.ReceiptsRepo;

@Service
public class ReceiptService {

	@Autowired
	ReceiptsRepo receiptsRepo;

	public UuidDTO processReceipts(Receipt receipt) {
		int totalPoints = 0;
		double totalPrice= 0;
		if(receipt.getTotal()!=null) {
			totalPrice=Double.parseDouble(receipt.getTotal());
		}

			// Rule 1: One point for every alphanumeric character in the retailer name
		if(receipt.getRetailer()!=null) {
			totalPoints += countAlphanumeric(receipt.getRetailer());
		}
		
		if(receipt.getTotal()!=null) {
			// Rule 2: 50 points if the total is a round dollar amount with no cents
			if (isRoundDollarAmount(totalPrice)) {
				totalPoints += 50;
			}

			// Rule 3: 25 points if the total is a multiple of 0.25
			if (isMultipleOfQuarter(totalPrice)) {
				totalPoints += 25;
			}
		}
		

		
		if(receipt.getItems()!=null) {
			// Rule 4: 5 points for every two items on the receipt
			totalPoints += (receipt.getItems().size() / 2) * 5;

			// Rule 5: Points based on item descriptions
			totalPoints += calculateItemDescriptionPoints(receipt.getItems());
		}
		

		// Rule 6: 6 points if the day in the purchase date is odd
		if (receipt.getPurchaseDate()!=null && isPurchaseDayOdd(receipt.getPurchaseDate())) {
			totalPoints += 6;
		}

		// Rule 7: 10 points if the time of purchase is after 2:00pm and before 4:00pm
		if (receipt.getPurchaseTime()!=null && isPurchaseTimeBetweenTwoAndFour(receipt.getPurchaseTime())) {
			totalPoints += 10;
		}

		receipt.setPoints(totalPoints);
		UuidDTO uuidDTO = new UuidDTO();
		uuidDTO.setId(receiptsRepo.saveReceipt(receipt));
		return uuidDTO;
	}

	public PointsDTO getPoints(UUID id) {
		PointsDTO prtDto = new PointsDTO();
		prtDto.setPoints(receiptsRepo.getPoints(id));
		return prtDto;
	}
	
	private  int countAlphanumeric(String retailer) {
		int count = 0;
		for (char c : retailer.toCharArray()) {
			if (Character.isLetterOrDigit(c)) {
				count++;
			}
		}
		return count;
	}

	private  boolean isRoundDollarAmount(Double total) {
		BigDecimal totalAmount = new BigDecimal(total);
		return totalAmount.stripTrailingZeros().scale() <= 0; 
	}

	private  boolean isMultipleOfQuarter(Double total) {
		BigDecimal totalAmount = new BigDecimal(total);
		return totalAmount.remainder(BigDecimal.valueOf(0.25)).compareTo(BigDecimal.ZERO) == 0;
	}

	private  int calculateItemDescriptionPoints(List<Item> items) {
		int points = 0;
		for (Item item : items) {
			String trimmedDescription = item.getShortDescription().trim();
			if (trimmedDescription.length() % 3 == 0) {
				BigDecimal price = new BigDecimal(item.getPrice());
				BigDecimal calculatedPoints = price.multiply(BigDecimal.valueOf(0.2)).setScale(0, RoundingMode.UP);
				points += calculatedPoints.intValue();
			}
		}
		return points;
	}

	private  boolean isPurchaseDayOdd(String purchaseDate) {
		LocalDate date = LocalDate.parse(purchaseDate);
		return date.getDayOfMonth() % 2 != 0;
	}

	private  boolean isPurchaseTimeBetweenTwoAndFour(String purchaseTime) {
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime time = LocalTime.parse(purchaseTime, timeFormatter);
		LocalTime start = LocalTime.of(14, 0);
		LocalTime end = LocalTime.of(16, 0);
		return time.isAfter(start) && time.isBefore(end);
	}
	
	
}
