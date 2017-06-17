package org.worldbuilders.lottery.bean.excel;

import lombok.Data;

/**
 * Created by brendondugan on 4/14/17.
 */
@Data
public class DonationEntry extends ExcelEntry {
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
