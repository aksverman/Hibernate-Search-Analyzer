package com.rudra.aks.user.dao;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.rudra.aks.bo.UserBO;
import com.rudra.aks.customer.dao.CustomerDAOImpl;

@Repository
public class UserDAOImpl implements UserDAO {

	@Autowired
	SessionFactory	sessionFactory;
	
	private static final Logger logger = Logger.getLogger(CustomerDAOImpl.class);
	
	//@PostConstruct
	public void init() {
		FullTextSession fullTextSessionInit = Search.getFullTextSession(sessionFactory.openSession());
		try {
			fullTextSessionInit.createIndexer().startAndWait();
		} catch (InterruptedException e) {
			logger.error("Exception while indexing entities...");
		}
	}
	
	@Override
	public int saveUser(UserBO userBO) {
		int userid = 0;
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		userid = (int) session.save(userBO);
		tx.commit();
		session.close();
		return userid;
		
	}

	@Override
	public List<UserBO> search(String columnName, String searchText) {
		List<UserBO>	searchResult = new ArrayList<UserBO>();
		try {
			
			Session session = sessionFactory.openSession();
			FullTextSession	fullTextSession = Search.getFullTextSession(session);
			//fullTextSession.createIndexer().startAndWait();
			 
			// alternatively you can write the Lucene query using the Lucene query parser
			// or the Lucene programmatic API. The Hibernate Search DSL is recommended though
			QueryBuilder qb = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(UserBO.class).get();
			
			org.apache.lucene.search.Query query = qb
					.keyword()
					.onFields(columnName)
					.matching(searchText)
					.createQuery();
			// wrap Lucene query in a org.hibernate.Query
			org.hibernate.Query hibQuery = fullTextSession.createFullTextQuery(query, UserBO.class);
			//FullTextQuery	fullQuery = fullTextSession.createFullTextQuery(query, UserBO.class);
			//fullQuery.setProjection("userID", "userName", "departmentID");
			
			// execute search
			@SuppressWarnings("unchecked")
			List<UserBO> result = hibQuery.list();
			//List<UserBO>	result = fullQuery.list();
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
