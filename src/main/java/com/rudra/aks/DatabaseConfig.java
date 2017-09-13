package com.rudra.aks;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;

import com.rudra.aks.bo.CustomerBO;
import com.rudra.aks.bo.UserBO;

@Configuration
@ComponentScan(basePackages = "com.rudra.aks")
public class DatabaseConfig {
	
	@Bean
	@Autowired
	public HibernateTemplate getHibernateTemplate(SessionFactory sessionFactory)	{
		HibernateTemplate hibernateTemplate = new HibernateTemplate(sessionFactory);
	    hibernateTemplate.setCheckWriteOperations(false);
		return hibernateTemplate;
	}	
	
	@Autowired
	@Bean(name = "transactionManager")
	public HibernateTransactionManager getTransactionManager(SessionFactory sessionFactory) {
	    HibernateTransactionManager transactionManager = new HibernateTransactionManager(sessionFactory);
	    //transactionManager.getSessionFactory().getCurrentSession().setFlushMode(FlushMode.AUTO);
	    return transactionManager;
	}
	
	/*private Properties getHibernateProperties() {
	    Properties properties = new Properties();
	    properties.put("hibernate.show_sql", "true");
	    properties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
	    properties.put("hibernate.hbm2ddl.auto", "update");
	    properties.put("hibernate.search.default.directory_provider", "filesystem");
	    properties.put("hibernate.search.default.indexBase", "var/hiber/index");
	    properties.put("hibernate.search.analyzer", "org.apache.lucene.analysis.standard.StandardAnalyzer");
	    return properties;
	}*/

	@Autowired
	@Bean(name = "sessionFactory")
	public SessionFactory getSessionFactory(DataSource dataSource) {
	 
	    LocalSessionFactoryBuilder sessionBuilder = new LocalSessionFactoryBuilder(dataSource);
	    sessionBuilder.addPackage("com.rudra.aks.bo");
	    sessionBuilder.addAnnotatedClass(CustomerBO.class);
	    sessionBuilder.addAnnotatedClass(UserBO.class);
	    //sessionBuilder.addProperties(getHibernateProperties());
	    return sessionBuilder.buildSessionFactory();
	}

	@Bean(name = "dataSource")
	public DataSource getDataSource() {
	    BasicDataSource dataSource = new BasicDataSource();
	    dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
	    dataSource.setUrl("jdbc:mysql://10.98.8.100:3306/security_dev?useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC");
	    dataSource.setUsername("devuser");
	    dataSource.setPassword("leo$123");
	 
	    return dataSource;
	}
	
	
}
