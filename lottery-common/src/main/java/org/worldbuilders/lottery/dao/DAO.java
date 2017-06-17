package org.worldbuilders.lottery.dao;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.worldbuilders.lottery.util.HibernateUtil;

import java.util.List;

/**
 * Created by brendondugan on 6/13/17.
 */
@Slf4j
public abstract class DAO<T> {
	protected SessionFactory sessionFactory;


	public DAO() {
		this.sessionFactory = HibernateUtil.getSessionFactory();
	}

	public void update(T instance) {
		Session session = this.getSession();
		Transaction transaction = session.getTransaction();
		session.update(instance);
		transaction.commit();
	}

	public void save(T instance) {
		Session session = this.getSession();
		Transaction transaction = session.getTransaction();
		session.save(instance);
		transaction.commit();
	}

	abstract List<T> getAll();

	abstract List<T> getByType(String type);

	protected Session getSession() {
		Session currentSession = sessionFactory.getCurrentSession();
		if (currentSession == null) {
			currentSession = sessionFactory.openSession();
		}
		if (!currentSession.getTransaction().isActive()) {
			currentSession.beginTransaction();
		}
		return currentSession;
	}

	public void cleanup() {
		Session session = getSession();
		log.debug("Flushing Session");
		session.flush();
		log.debug("Closing Session");
//		session.close();
	}
}
