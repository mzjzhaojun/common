package com.yt.app.frame.d;

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

import com.yt.app.frame.c.MySqlConfig;
import com.yt.app.frame.e.Aj;

import javax.sql.DataSource;

import java.util.HashMap;
import java.util.Map;

@Configuration
@MapperScan(basePackages = { "com.yt.app.api.v1.mapper" })
public class Ae implements TransactionManagementConfigurer {

	@Autowired
	MySqlConfig g;

	@Bean
	public DataSource g() {
		BasicDataSource localBasicDataSource = new BasicDataSource();
		localBasicDataSource.setDriverClassName(this.g.getSlavedriver());
		localBasicDataSource.setUsername(this.g.getSlaveuser());
		localBasicDataSource.setPassword(this.g.getSlavepassword());
		localBasicDataSource.setUrl(this.g.getSlavejdbcurl());
		localBasicDataSource.setMaxActive(this.g.getMaxActive());
		localBasicDataSource.setValidationQuery(this.g.getValidationQuery());
		localBasicDataSource.setTestOnBorrow(this.g.isTestOnBorrow());
		localBasicDataSource.setTestOnReturn(this.g.isTestOnReturn());
		localBasicDataSource.setTestWhileIdle(this.g.isTestWhileIdle());
		localBasicDataSource.setTimeBetweenEvictionRunsMillis(this.g.getTimeBetweenEvictionRunsMillis());
		localBasicDataSource.setMinEvictableIdleTimeMillis(this.g.getMinEictableIdleTimeMillis());
		localBasicDataSource.setPoolPreparedStatements(this.g.isPoolPreparedStatements());
		localBasicDataSource.setMaxOpenPreparedStatements(this.g.getMaxOpenPreparedStatements());
		return localBasicDataSource;
	}

	@Bean
	public DataSource h() {
		BasicDataSource localBasicDataSource = new BasicDataSource();
		localBasicDataSource.setDriverClassName(this.g.getMasterdriver());
		localBasicDataSource.setUsername(this.g.getMasteruser());
		localBasicDataSource.setPassword(this.g.getMasterpassword());
		localBasicDataSource.setUrl(this.g.getMasterjdbcurl());
		localBasicDataSource.setMaxActive(this.g.getMaxActive());
		localBasicDataSource.setValidationQuery(this.g.getValidationQuery());
		localBasicDataSource.setTestOnBorrow(this.g.isTestOnBorrow());
		localBasicDataSource.setTestOnReturn(this.g.isTestOnReturn());
		localBasicDataSource.setTestWhileIdle(this.g.isTestWhileIdle());
		localBasicDataSource.setTimeBetweenEvictionRunsMillis(this.g.getTimeBetweenEvictionRunsMillis());
		localBasicDataSource.setMinEvictableIdleTimeMillis(this.g.getMinEictableIdleTimeMillis());
		localBasicDataSource.setPoolPreparedStatements(this.g.isPoolPreparedStatements());
		localBasicDataSource.setMaxOpenPreparedStatements(this.g.getMaxOpenPreparedStatements());
		return localBasicDataSource;
	}

	@Bean
	public SqlSessionFactory i() throws Exception {
		SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
		sqlSessionFactory.setDataSource(j());
		sqlSessionFactory.setConfigLocation(new ClassPathResource("config/mybatis-config.xml"));
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		sqlSessionFactory.setMapperLocations(resolver.getResources("classpath:com/yt/app/api/" + g.getVersion() + "/mapper/impl/*.xml"));
		return sqlSessionFactory.getObject();
	}

	@Bean
	public SqlSessionTemplate a(SqlSessionFactory paramSqlSessionFactory) {
		return new SqlSessionTemplate(paramSqlSessionFactory);
	}

	public PlatformTransactionManager annotationDrivenTransactionManager() {
		return new DataSourceTransactionManager(j());
	}

	@Primary
	@Bean
	public Aj j() {
		Map<String, DataSource> localHashMap1 = new HashMap<String, DataSource>();
		localHashMap1.put("slaveDataSource", g());
		Map<String, DataSource> localHashMap2 = new HashMap<String, DataSource>();
		localHashMap2.put("masterDataSource", h());
		return new Aj(localHashMap1, localHashMap2, h());
	}
}