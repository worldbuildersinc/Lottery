package org.worldbuilders.lottery;

import lombok.extern.slf4j.Slf4j;
import org.worldbuilders.lottery.bean.Prize;
import org.worldbuilders.lottery.bean.RaffleTicket;
import org.worldbuilders.lottery.dao.PrizeDAO;
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
	Random random;
	private PrizeDAO prizeDAO;
	private RaffleTicketDAO raffleTicketDAO;
	private int remainingPrizes;
	private Map<Prize, RaffleTicket> winners;

	public Lottery(List<RaffleTicket> raffleTickets, List<Prize> prizes) {
		this.random = new Random();
		raffleTicketDAO = new RaffleTicketDAO();
		prizeDAO = new PrizeDAO();
		this.winners = new HashMap<>();
		for (RaffleTicket r : raffleTickets) {
			log.debug("Saving RaffleTicket {}", r.toString());
			raffleTicketDAO.save(r);
		}
		raffleTicketDAO.cleanup();
		for (Prize p : prizes) {
			log.debug("Saving Prize {}", p.toString());
			prizeDAO.save(p);
		}
		prizeDAO.cleanup();
		remainingPrizes = prizes.size();
	}

	public void selectWinner() {
		log.debug("Selecting a winner");
		List<Prize> prizes = prizeDAO.getAll();
		log.debug("Choosing a random prize");
		Prize prize = prizes.get(random.nextInt(prizes.size()));
		log.debug("Prize chosen");
		log.debug("Getting tickets");
		List<RaffleTicket> tickets = raffleTicketDAO.getByType(prize.getType());
		if (tickets.size() == 0) {// Nobody wants this type, so treat it like a misc prize
			log.warn("Tickets size was 0, choosing new set");
			tickets = raffleTicketDAO.getByType("misc");
		}
		RaffleTicket raffleTicket = tickets.get(random.nextInt(tickets.size()));
		prize.setIsClaimed(true);
		prizeDAO.update(prize);
		raffleTicket.setWinner(true);
		raffleTicketDAO.update(raffleTicket);
		winners.put(prize, raffleTicket);
		remainingPrizes--;
		log.debug("{} Prizes Remaining", remainingPrizes);
	}

	public int getRemainingPrizes() {
		return remainingPrizes;
	}

	public Map<Prize, RaffleTicket> getWinners() {
		return winners;
	}
}
