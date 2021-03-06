package org.worldbuilders.lottery.dao;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.worldbuilders.lottery.bean.RaffleTicket;

import java.util.List;

/**
 * Created by brendondugan on 6/13/17.
 */
@Slf4j
public class RaffleTicketDAO extends DAO<RaffleTicket> {
	public RaffleTicketDAO() {
		super();
	}

	@Override
	public List<RaffleTicket> getAll() {
		Session session = this.getSession();
		return session.createQuery("from RaffleTicket where isWinner = false", RaffleTicket.class).list();
	}

	@Override
	public List<RaffleTicket> getByType(String type) {
		Session session = this.getSession();
		Query<RaffleTicket> query = session.createQuery(getQueryString(type), RaffleTicket.class);
		return query.list();
	}

	@Override
	public RaffleTicket getRandomSingleByType(String type) {
		Session session = this.getSession();
		String queryString = String.format("%s ORDER BY RAND() DESC", getQueryString(type));
		Query<RaffleTicket> query = session.createQuery(queryString, RaffleTicket.class);
		query.setMaxResults(1);
		return query.getSingleResult();
	}

	private String getQueryString(String type) {
		String query = "";
		switch (type.toLowerCase()) {
			case "joco":
				query = "from RaffleTicket where jocoPref = true and isWinner = false";
				break;
			case "book":
				query = "from RaffleTicket where booksPref = true and isWinner = false";
				break;
			case "game":
				query = "from RaffleTicket where gamesPref = true and isWinner = false";
				break;
			case "comic":
				query = "from RaffleTicket where comicsPref = true and isWinner = false";
				break;
			case "jewelry":
				query = "from RaffleTicket where jewelryPref = true and isWinner = false";
				break;
			case "misc":
				query = "from RaffleTicket where isWinner = false";
				break;
			default:

				break;
		}
		if (!StringUtils.isEmpty(query)) {
			return query;
		} else {
			throw new IllegalArgumentException("Invalid Prize Type");
		}
	}
}
