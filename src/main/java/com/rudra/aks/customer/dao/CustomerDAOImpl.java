package com.rudra.aks.customer.dao;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.rudra.aks.bo.CustomerBO;

@Repository
public class CustomerDAOImpl implements CustomerDAO {

	@Autowired
	HibernateTemplate	hibernateTemplate;
	
	@Autowired
	SessionFactory	sessionFactory;
	
	private static final Logger logger = Logger.getLogger(CustomerDAOImpl.class);
	
	@PostConstruct
	public void init() {
		FullTextSession fullTextSessionInit = Search.getFullTextSession(sessionFactory.openSession());
		try {
			fullTextSessionInit.createIndexer().startAndWait();
		} catch (InterruptedException e) {
			logger.error("Exception while indexing entities...");
		}
	}
	
	/**
	 * 
	 */
	/*public int save(CustomerBO customer) {
		int id = 0;
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		id = (int) session.save(customer);
		tx.commit();
		session.close();
		return id;
	}*/
	
	public int save(CustomerBO customer) {
		return (Integer) hibernateTemplate.save(customer);
	}

	@Override
	public void delete(CustomerBO customer) {
		try {
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			session.delete(customer);
			tx.commit();
			session.close();
			logger.info("Customer information deleted...");
		} catch ( Exception e) { logger.error("Customer deletion failed! " + e); }
		
	}
	
	
	public List<CustomerBO> search(String columnName, String searchText) {
		List<CustomerBO>	searchResult = new ArrayList<CustomerBO>();
		try {
			
			Session session = sessionFactory.openSession();
			FullTextSession	fullTextSession = Search.getFullTextSession(session);
			//fullTextSession.createIndexer().startAndWait();
			 
			// alternatively you can write the Lucene query using the Lucene query parser
			// or the Lucene programmatic API. The Hibernate Search DSL is recommended though
			QueryBuilder qb = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(CustomerBO.class).get();
			
			org.apache.lucene.search.Query query = qb
					.keyword()
					.onFields(columnName)
					.matching(searchText)
					.createQuery();
			// wrap Lucene query in a org.hibernate.Query
			org.hibernate.Query hibQuery = fullTextSession.createFullTextQuery(query, CustomerBO.class);
			
			// execute search
			@SuppressWarnings("unchecked")
			List<CustomerBO> result = (hibQuery.list());
			logger.info("----No of matching result : " + result.size());
			searchResult = result;
			
			/*Iterator<CustomerBO> it = result.iterator();
			logger.info("==============================List of matched search resultl: ============================");
			while (it.hasNext()) {
				CustomerBO CustomerBO1 = it.next();
				logger.info(CustomerBO1);
			}*/
		} catch (Exception e) {
				logger.error("Exception occurred!" + e);
		}	
		return searchResult;
	}


}
