package com.yt.app.frame.config;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import com.yt.app.frame.datasource.DynamicDataSource;

import javax.sql.DataSource;

import java.util.HashMap;
import java.util.Map;

@Configuration
@MapperScan(basePackages = "com.yt.app.api.v1.mapper")
public class DataSouceConfiguration implements TransactionManagementConfigurer {

	@Autowired
	MySqlConfig mysqlconfig;

	@Bean
	public DataSource dataSourceSlave() {
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName(mysqlconfig.getSlavedriver());
		ds.setUsername(mysqlconfig.getSlaveuser());
		ds.setPassword(mysqlconfig.getSlavepassword());
		ds.setUrl(mysqlconfig.getSlavejdbcurl());
		ds.setMaxActive(mysqlconfig.getMaxActive());
		ds.setValidationQuery(mysqlconfig.getValidationQuery());
		ds.setTestOnBorrow(mysqlconfig.isTestOnBorrow());
		ds.setTestOnReturn(mysqlconfig.isTestOnReturn());
		ds.setTestWhileIdle(mysqlconfig.isTestWhileIdle());
		ds.setTimeBetweenEvictionRunsMillis(mysqlconfig.getTimeBetweenEvictionRunsMillis());
		ds.setMinEvictableIdleTimeMillis(mysqlconfig.getMinEictableIdleTimeMillis());
		ds.setPoolPreparedStatements(mysqlconfig.isPoolPreparedStatements());
		ds.setMaxOpenPreparedStatements(mysqlconfig.getMaxOpenPreparedStatements());
		return ds;
	}

	@Bean
	public DataSource dataSourceMaster() {
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName(mysqlconfig.getMasterdriver());
		ds.setUsername(mysqlconfig.getMasteruser());
		ds.setPassword(mysqlconfig.getMasterpassword());
		ds.setUrl(mysqlconfig.getMasterjdbcurl());
		ds.setMaxActive(mysqlconfig.getMaxActive());
		ds.setValidationQuery(mysqlconfig.getValidationQuery());
		ds.setTestOnBorrow(mysqlconfig.isTestOnBorrow());
		ds.setTestOnReturn(mysqlconfig.isTestOnReturn());
		ds.setTestWhileIdle(mysqlconfig.isTestWhileIdle());
		ds.setTimeBetweenEvictionRunsMillis(mysqlconfig.getTimeBetweenEvictionRunsMillis());
		ds.setMinEvictableIdleTimeMillis(mysqlconfig.getMinEictableIdleTimeMillis());
		ds.setPoolPreparedStatements(mysqlconfig.isPoolPreparedStatements());
		ds.setMaxOpenPreparedStatements(mysqlconfig.getMaxOpenPreparedStatements());
		return ds;
	}

	@Bean
	public SqlSessionFactory sqlSessionFactory() throws Exception {
		final SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
		sqlSessionFactory.setDataSource(dynamicDataSource());
		sqlSessionFactory.setConfigLocation(new ClassPathResource("config/mybatis-config.xml"));
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		sqlSessionFactory.setMapperLocations(resolver.getResources("classpath:com/yt/app/api/" + mysqlconfig.getVersion() + "/mapper/impl/*.xml"));
		return sqlSessionFactory.getObject();
	}

	@Bean
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}

	@Override
	public PlatformTransactionManager annotationDrivenTransactionManager() {
		return new DataSourceTransactionManager(dynamicDataSource());
	}

	@Primary
	@Bean
	public DynamicDataSource dynamicDataSource() {
		Map<String, DataSource> _slavetDataSources = new HashMap<String, DataSource>();
		_slavetDataSources.put("slaveDataSource", dataSourceSlave());
		Map<String, DataSource> _masterDataSources = new HashMap<String, DataSource>();
		_masterDataSources.put("masterDataSource", dataSourceMaster());
		return new DynamicDataSource(_slavetDataSources, _masterDataSources, dataSourceMaster());
	}

}