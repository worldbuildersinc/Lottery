package org.worldbuilders.lottery.util.excel;

import lombok.extern.slf4j.Slf4j;
import org.worldbuilders.lottery.bean.RaffleTicket;
import org.worldbuilders.lottery.bean.excel.DonationEntry;
import org.worldbuilders.lottery.util.DonationMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by brendondugan on 6/23/17.
 */
@Slf4j
public class OptimizedDonationMapper {
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
		raffleTicket.setShippingState(entry.getShippingState());
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
		ExecutorService executorService = Executors.newCachedThreadPool();
		List<Future<List<RaffleTicket>>> futures = new ArrayList<>(entries.size());
		ArrayList<RaffleTicket> raffleTickets = new ArrayList<>(entries.size()*2);
		for (DonationEntry entry : entries) {
			futures.add(executorService.submit(() -> map(entry)));
		}
		executorService.shutdown();
		while (!futures.isEmpty()){
			for(int j = 0; j < futures.size(); j++){
				Future<List<RaffleTicket>> f = futures.get(j);
				if(f.isDone()){
					try {
						List<RaffleTicket> entry = f.get();
						raffleTickets.addAll(entry);
						futures.remove(f);
					} catch (InterruptedException | ExecutionException e) {
						log.error(e.getMessage());
					}
				}
			}
		}
		return raffleTickets;
	}
}
