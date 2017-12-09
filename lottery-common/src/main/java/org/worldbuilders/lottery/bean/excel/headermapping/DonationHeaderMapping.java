package org.worldbuilders.lottery.bean.excel.headermapping;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by brendondugan on 4/17/17.
 */
@Data
@AllArgsConstructor
public class DonationHeaderMapping {
	private String campaignHeader;
	private String receivedDateHeader;
	private String combinedDonationAmountHeader;
	private String eligibleAmountHeader;
	private String numberOfTicketsHeader;
	private String emailAddressHeader;
	private String shippingNameHeader;
	private String shippingAddress1Header;
	private String shippingAddress2Header;
	private String shippingCityHeader;
	private String shippingStateHeader;
	private String shippingPostCodeHeader;
	private String shippingCountryHeader;
	private String jocoPrefHeader;
	private String booksPrefHeader;
	private String gamesPrefHeader;
	private String comicsPrefHeader;
	private String jewelryPrefHeader;
}
