package org.worldbuilders.lottery;

import lombok.extern.slf4j.Slf4j;
import org.worldbuilders.lottery.bean.Prize;
import org.worldbuilders.lottery.bean.RaffleTicket;
import org.worldbuilders.lottery.dao.RaffleTicketDAO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by brendondugan on 6/10/17.
 */
@Slf4j
public class Lottery {
	private RaffleTicketDAO raffleTicketDAO;
	private int remainingPrizes;
	private Map<Prize, RaffleTicket> winners;
	private List<RaffleTicket> bookPreferrers;
	private List<RaffleTicket> gamePreferrers;
	private List<RaffleTicket> comicPreferrers;
	private List<RaffleTicket> jewelryPreferrers;
	private List<RaffleTicket> joCoPreferrers;
	private List<RaffleTicket> raffleTickets;
	private List<Prize> prizes;
	private Random random;

	public Lottery(List<RaffleTicket> raffleTickets, List<Prize> prizes) throws InterruptedException {
		this.random = new Random();
		this.raffleTickets = raffleTickets;
		this.prizes = prizes;
		int batchSize = 100;
		raffleTicketDAO = new RaffleTicketDAO();
		this.winners = new HashMap<>();
		raffleTicketDAO.batchSave(raffleTickets, batchSize);
		remainingPrizes = prizes.size();
		bookPreferrers = raffleTicketDAO.getByType("book");
		gamePreferrers = raffleTicketDAO.getByType("game");
		comicPreferrers = raffleTicketDAO.getByType("comic");
		jewelryPreferrers = raffleTicketDAO.getByType("jewelry");
		joCoPreferrers = raffleTicketDAO.getByType("joco");
	}

	public void selectWinner() {
		Prize prize = getRandomPrize();
		RaffleTicket raffleTicket = getRandomRaffleTicketByType(prize.getType());
		while (isDuplicatePrizeForUser(prize, raffleTicket)){
			raffleTicket = getRandomRaffleTicketByType(prize.getType());
		}
		winners.put(prize, raffleTicket);
		cleanupWinningRaffleTicket(raffleTicket);
		remainingPrizes--;
	}

	private boolean isDuplicatePrizeForUser(Prize prize, RaffleTicket raffleTicket){
		for (Prize p : winners.keySet()) {
			if (p.getName().equals(prize.getName())) {
				RaffleTicket r = winners.get(p);
				if (r.getEmailAddress().equals(raffleTicket.getEmailAddress())) {
					return true;
				}
			}
		}
		return false;
	}

	private void cleanupWinningRaffleTicket(RaffleTicket raffleTicket) {
		if (bookPreferrers.contains(raffleTicket)) {
			bookPreferrers.remove(raffleTicket);
		}
		if (gamePreferrers.contains(raffleTicket)) {
			gamePreferrers.remove(raffleTicket);
		}
		if (comicPreferrers.contains(raffleTicket)) {
			comicPreferrers.remove(raffleTicket);
		}
		if (jewelryPreferrers.contains(raffleTicket)) {
			jewelryPreferrers.remove(raffleTicket);
		}
		if (joCoPreferrers.contains(raffleTicket)) {
			joCoPreferrers.remove(raffleTicket);
		}
		if (raffleTickets.contains(raffleTicket)) {
			raffleTickets.remove(raffleTicket);
		}
	}

	private Prize getRandomPrize() {
		return prizes.remove(random.nextInt(prizes.size()));
	}

	private RaffleTicket getRandomRaffleTicketByType(String type) {
		RaffleTicket raffleTicket = null;
		switch (type.toLowerCase()) {
			case "joco":
				if (joCoPreferrers.isEmpty()) {
					raffleTicket = getRandomRaffleTicketByType("misc");
				} else {
					raffleTicket = joCoPreferrers.get(random.nextInt(joCoPreferrers.size()));
				}
				break;
			case "book":
				if (bookPreferrers.isEmpty()) {
					raffleTicket = getRandomRaffleTicketByType("misc");
				} else {
					raffleTicket = bookPreferrers.get(random.nextInt(bookPreferrers.size()));
				}
				break;
			case "game":
				if (gamePreferrers.isEmpty()) {
					raffleTicket = getRandomRaffleTicketByType("misc");
				} else {
					raffleTicket = gamePreferrers.get(random.nextInt(gamePreferrers.size()));
				}
				break;
			case "comic":
				if (comicPreferrers.isEmpty()) {
					raffleTicket = getRandomRaffleTicketByType("misc");
				} else {
					raffleTicket = comicPreferrers.get(random.nextInt(comicPreferrers.size()));
				}
				break;
			case "jewelry":
				if (jewelryPreferrers.isEmpty()) {
					raffleTicket = getRandomRaffleTicketByType("misc");
				} else {
					raffleTicket = jewelryPreferrers.get(random.nextInt(jewelryPreferrers.size()));
				}
				break;
			case "misc":
				raffleTicket = raffleTickets.get(random.nextInt(raffleTickets.size()));
				break;
			default:

				break;
		}
		return raffleTicket;
	}

	public int getRemainingPrizes() {
		return remainingPrizes;
	}

	public Map<Prize, RaffleTicket> getWinners() {
		return winners;
	}
}
