package org.worldbuilders.lottery.bean;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

/**
 * Created by brendondugan on 4/16/17.
 */
@Data
@Entity
@Table
public class RaffleTicket {
	@Id
	private UUID id;
	@Column
	private String campaign;
	@Column
	private String receivedDate;
	@Column
	private double combinedDonationAmount;
	@Column
	private int eligibleAmount;
	@Column
	private int numberOfTickets;
	@Column
	private String emailAddress;
	@Column
	private String shippingName;
	@Column
	private String shippingAddress1;
	@Column
	private String shippingAddress2;
	@Column
	private String shippingCity;
	@Column
	private String shippingState;
	@Column
	private String shippingPostCode;
	@Column
	private String shippingCountry;
	@Column
	private boolean jocoPref;
	@Column
	private boolean booksPref;
	@Column
	private boolean gamesPref;
	@Column
	private boolean comicsPref;
	@Column
	private boolean jewelryPref;
	@Column
	private boolean isWinner = false;
}
