package com.rudra.aks.test;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.util.Version;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.SearchFactory;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.rudra.aks.DatabaseConfig;
import com.rudra.aks.bo.CustomerBO;

public class SearchTest {

	private static final Logger logger = Logger.getLogger(SearchTest.class);
	
	
	public static void main(String[] args) {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(DatabaseConfig.class);
	    SessionFactory	sessionFactory = ctx.getBean(SessionFactory.class);   
	    Session session = sessionFactory.openSession();
	     
	     
	    try {
	    	FullTextSession	fullTextSession = Search.getFullTextSession(session);
			fullTextSession.createIndexer(CustomerBO.class).startAndWait();
			 
			// alternatively you can write the Lucene query using the Lucene query parser
			// or the Lucene programmatic API. The Hibernate Search DSL is recommended though
			QueryBuilder qb = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(CustomerBO.class).get();
			
			doKeywordSearch(qb, fullTextSession);
						
			doRangeSearch(qb, fullTextSession);
						
			doWildCardSearch(qb, fullTextSession);
			
			doPhraseSearch(qb, fullTextSession);
			
			showProjectedProperties(qb, fullTextSession);
			
			timeLimitedSearch(qb, fullTextSession);
			
			paginationSearch(qb, fullTextSession);
			
			criteriaSearch(qb, fullTextSession);
			
			luceneQuerySearch(fullTextSession);
			
		} catch (Exception e) {	logger.info("Exception occurred!" + e);
		} finally{	session.close();ctx.close();	}
	}

	private static void luceneQuerySearch(FullTextSession	fullTextSession) {
		SearchFactory searchFactory = fullTextSession.getSearchFactory();
		org.apache.lucene.queryParser.QueryParser parser = 
		    new QueryParser(Version.LUCENE_36, "customername", searchFactory.getAnalyzer(CustomerBO.class) );
		try {
			//creating lucene query for search using default column passed in QueryParser
		    org.apache.lucene.search.Query luceneQuery = parser.parse( "aks" );
		    
		    //can use other column also as "columnname/fieldname : value-to-search",  f.e.
		    //org.apache.lucene.search.Query luceneQuery = parser.parse( "customerid:4" );
		    org.hibernate.Query fullTextQuery = fullTextSession.createFullTextQuery(luceneQuery);
		    List result = fullTextQuery.list(); //return a list of managed objects    
		    logger.info("----No of matching result : " + result.size());
			Iterator<CustomerBO> it = result.iterator();
			logger.info("========== Lucene API Query ==============List of matched search resultl: ============================");
			while (it.hasNext()) {
				CustomerBO CustomerBO1 = it.next();
				System.out.println(CustomerBO1);
			}
		}
		catch (ParseException e) {
		    //handle parsing failure
		}

	}

	private static void criteriaSearch(QueryBuilder qb, FullTextSession fullTextSession) {
		logger.info("=========== Criteria on Search =============");
		org.apache.lucene.search.Query query = qb
				.keyword()
				.wildcard()
				.onField("customername")
				.matching("*")
				.createQuery();
		// wrap Lucene query in a org.hibernate.Query
		org.hibernate.Query hibQuery = fullTextSession
										.createFullTextQuery(query, CustomerBO.class)
										.setFirstResult(4);
		
		Criteria criteria = fullTextSession.createCriteria(CustomerBO.class, "searchcriteria");
		criteria.add(Restrictions.like("customername", "aks"));
		
		// execute search
		List result = criteria.list();
		Iterator<CustomerBO> it = result.iterator();
		logger.info("----No of matching result : " + result.size());
		while(it.hasNext())
			logger.info("Customer BO ... " + it.next());
			
	}
	
	
	private static void paginationSearch(QueryBuilder qb, FullTextSession fullTextSession) {
		logger.info("=========== Pagination on Search =============");
		org.apache.lucene.search.Query query = qb
				.keyword()
				.wildcard()
				.onField("customername")
				.matching("*")
				.createQuery();
		// wrap Lucene query in a org.hibernate.Query
		org.hibernate.Query hibQuery = fullTextSession
										.createFullTextQuery(query, CustomerBO.class)
										.setFirstResult(4);
		
		/*Criteria criteria = fullTextSession.createCriteria(CustomerBO.class, "searchcriteria");
		criteria.add(Restrictions.like("customername", "aks"));
		List result = criteria.list();*/
		
		// execute search
		List result = hibQuery.list();
		Iterator<CustomerBO> it = result.iterator();
		logger.info("----No of matching result : " + result.size());
		while(it.hasNext())
			logger.info("Customer BO ... " + it.next());
			
	}


	private static void timeLimitedSearch(QueryBuilder qb, FullTextSession fullTextSession) {
		logger.info("======= Time Limited Search ======== ");
		org.apache.lucene.search.Query query = qb
				.keyword()
				.wildcard()
				.onField("customername")
				.matching("ak*")
				.createQuery();
		// wrap Lucene query in a org.hibernate.Query
		org.hibernate.Query hibQuery = fullTextSession
										.createFullTextQuery(query, CustomerBO.class)
										.limitExecutionTimeTo(2, TimeUnit.MILLISECONDS);
		
		// execute search
		List result = hibQuery.list();
		Iterator<CustomerBO> it = result.iterator();
		logger.info("----No of matching result : " + result.size());
		while(it.hasNext())
			logger.info("Customer BO ... " + it.next());
	}


	private static void showProjectedProperties(QueryBuilder qb, FullTextSession fullTextSession) {
		org.apache.lucene.search.Query query = qb
				.keyword()
				.onFields("customername")
				.matching("aks")
				.createQuery();
		// wrap Lucene query in a org.hibernate.Query
		org.hibernate.Query hibQuery = fullTextSession
										.createFullTextQuery(query, CustomerBO.class)
										.setProjection("customername", "mobileno");
		
		// execute search
		List result = hibQuery.list();
		logger.info("----No of matching result : " + result.size());
		for( int i=0; i<result.size(); i++) {
			Object[] obj = (Object[])result.get(i);
			logger.info("Object values : " + obj[0] + "  " + obj[1]);
		} 
		
	}


	private static void doKeywordSearch(QueryBuilder qb, FullTextSession fullTextSession) {
		
		//custome name ---- You can also execute wildcard queries (queries where some of parts of the word are unknown).
		org.apache.lucene.search.Query query = qb
				.keyword()
				.onFields("customername")
				.matching("You execute queries")
				.createQuery();
		// wrap Lucene query in a org.hibernate.Query
		org.hibernate.Query hibQuery = fullTextSession.createFullTextQuery(query, CustomerBO.class);
		// execute search
		@SuppressWarnings("unchecked")
		List<CustomerBO> result = hibQuery.list();
		logger.info("----No of matching result : " + result.size());
		Iterator<CustomerBO> it = result.iterator();
		logger.info("========== Keyword Search ==============List of matched search resultl: ============================");
		while (it.hasNext()) {
			CustomerBO CustomerBO1 = it.next();
			System.out.println(CustomerBO1);
		}
	}
	
	
	private static void doRangeSearch(QueryBuilder qb, FullTextSession fullTextSession) {
		org.apache.lucene.search.Query query = qb
				.range()
				.onField("customerid")
				.above(4)
				.createQuery();
		// wrap Lucene query in a org.hibernate.Query
		org.hibernate.Query hibQuery = fullTextSession.createFullTextQuery(query, CustomerBO.class);
		// execute search
		@SuppressWarnings("unchecked")
		List<CustomerBO> result = hibQuery.list();
		logger.info("----No of matching result : " + result.size());
		Iterator<CustomerBO> it = result.iterator();
		logger.info("==========Range Search===============List of matched search resultl: ============================");
		while (it.hasNext()) {
			CustomerBO CustomerBO1 = it.next();
			System.out.println(CustomerBO1);
		}
		
	}
	
	private static void doWildCardSearch(QueryBuilder qb, FullTextSession fullTextSession) {
		org.apache.lucene.search.Query query = qb
				.keyword()
				.wildcard()
				.onField("customername")
				.matching("aks*")
				.createQuery();
		// wrap Lucene query in a org.hibernate.Query
		org.hibernate.Query hibQuery = fullTextSession.createFullTextQuery(query, CustomerBO.class);
		// execute search
		@SuppressWarnings("unchecked")
		List<CustomerBO> result = hibQuery.list();
		logger.info("----No of matching result : " + result.size());
		Iterator<CustomerBO> it = result.iterator();
		logger.info("==========Wild Card Search ===========List of matched search resultl: ============================");
		while (it.hasNext()) {
			CustomerBO CustomerBO1 = it.next();
			System.out.println(CustomerBO1);
		}
		
	}
	
	
	private static void doPhraseSearch(QueryBuilder qb, FullTextSession fullTextSession) {
		
		//custome name ---- You can also execute wildcard queries (queries where some of parts of the word are unknown).
		org.apache.lucene.search.Query query = qb
				.phrase().withSlop(0)
				.onField("customername")
				.sentence("You can queries")
				.createQuery();
		// wrap Lucene query in a org.hibernate.Query
		org.hibernate.Query hibQuery = fullTextSession.createFullTextQuery(query, CustomerBO.class);
		// execute search
		@SuppressWarnings("unchecked")
		List<CustomerBO> result = hibQuery.list();
		logger.info("----No of matching result : " + result.size());
		Iterator<CustomerBO> it = result.iterator();
		logger.info("==========Phrase Search=======List of matched search resultl: ============================");
		while (it.hasNext()) {
			CustomerBO CustomerBO1 = it.next();
			System.out.println(CustomerBO1);
		}
		
	}
}
