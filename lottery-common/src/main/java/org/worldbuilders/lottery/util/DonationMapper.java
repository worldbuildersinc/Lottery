package org.worldbuilders.lottery.util;

import org.worldbuilders.lottery.bean.RaffleTicket;
import org.worldbuilders.lottery.bean.excel.DonationEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by brendondugan on 6/13/17.
 */
public class DonationMapper {

	public static List<RaffleTicket> map(DonationEntry entry) {
		ArrayList<RaffleTicket> raffleTickets = new ArrayList<>();
		for (int i = 0; i < entry.getNumberOfTickets(); i++) {
			raffleTickets.add(mapSingle(entry));
		}
		return raffleTickets;
	}

	public static RaffleTicket mapSingle(DonationEntry entry) {
		RaffleTicket raffleTicket = new RaffleTicket();
		raffleTicket.setId(UUID.randomUUID());
		raffleTicket.setEmailAddress(entry.getEmailAddress());
		raffleTicket.setShippingName(entry.getShippingName());
		raffleTicket.setShippingAddress1(entry.getShippingAddress1());
		raffleTicket.setShippingAddress2(entry.getShippingAddress2());
		raffleTicket.setShippingCity(entry.getShippingCity());
		raffleTicket.setShippingCountry(entry.getShippingCountry());
		raffleTicket.setShippingPostCode(entry.getShippingPostCode());
		raffleTicket.setJocoPref(entry.isJocoPref());
		raffleTicket.setBooksPref(entry.isBooksPref());
		raffleTicket.setGamesPref(entry.isGamesPref());
		raffleTicket.setComicsPref(entry.isComicsPref());
		raffleTicket.setJewelryPref(entry.isJewelryPref());
		return raffleTicket;
	}

	public static List<RaffleTicket> mapAll(List<DonationEntry> entries) {
		ArrayList<RaffleTicket> raffleTickets = new ArrayList<>();
		for (DonationEntry entry : entries) {
			raffleTickets.addAll(map(entry));
		}
		return raffleTickets;
	}
}
