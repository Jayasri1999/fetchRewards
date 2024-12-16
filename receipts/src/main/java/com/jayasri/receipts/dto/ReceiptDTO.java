package com.jayasri.receipts.dto;

import java.util.ArrayList;
import java.util.UUID;

import com.jayasri.receipts.domains.Item;

import lombok.Data;

@Data
public class ReceiptDTO {
     UUID id;
     String retailer;
     String purchaseDate;
     String purchaseTime;
     ArrayList<Item> items;
     double total;
     Integer points;
}
