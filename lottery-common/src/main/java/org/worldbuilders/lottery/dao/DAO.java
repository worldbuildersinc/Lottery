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

	public void batchSave(List<T> batch, int batchProcessingSize){
		for(int i = 0; i < batch.size(); i += batchProcessingSize){
			List<T> currentBatch = batch.subList(i, Math.min(batch.size(), i+batchProcessingSize));
			for(T instance : currentBatch){
				save(instance);
			}
			cleanup();
		}
	}


	abstract List<T> getAll();

	abstract List<T> getByType(String type);

	abstract T getRandomSingleByType(String type);

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
		session.flush();
		session.clear();
//		session.close();
	}
}
