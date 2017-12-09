package org.worldbuilders.lottery.bean.excel;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by brendondugan on 4/14/17.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DonationEntry extends ExcelEntry {

	private String campaign;
	private String receivedDate;
	private double combinedDonationAmount;
	private int eligibleAmount;
	private int numberOfTickets;
	private String emailAddress;
	private String shippingName;
	private String shippingAddress1;
	private String shippingAddress2;
	private String shippingCity;
	private String shippingState;
	private String shippingPostCode;
	private String shippingCountry;
	private boolean jocoPref;
	private boolean booksPref;
	private boolean gamesPref;
	private boolean comicsPref;
	private boolean jewelryPref;
}
