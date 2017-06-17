package org.worldbuilders.lottery.dao;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.worldbuilders.lottery.bean.Prize;

import java.util.List;

/**
 * Created by brendondugan on 6/13/17.
 */
@Slf4j
public class PrizeDAO extends DAO<Prize> {


	public PrizeDAO() {
		super();
	}


	@Override
	public List<Prize> getAll() {
		Session session = this.getSession();
		Query<Prize> query = session.createQuery("from Prize where isClaimed = false", Prize.class);
		List<Prize> list = query.list();
		return list;
	}

	@Override
	public List<Prize> getByType(String type) {
		log.debug("Getting Prizes by type {}", type);
		Session session = this.getSession();
		Query<Prize> query = session.createQuery("from Prize where Prize.type = :type and Prize.isClaimed = false", Prize.class);
		query.setParameter("type", type);
		List<Prize> list = query.list();
		return list;
	}
}
