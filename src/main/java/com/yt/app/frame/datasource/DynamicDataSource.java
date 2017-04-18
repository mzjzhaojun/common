package com.yt.app.frame.datasource;

import org.apache.commons.lang.math.RandomUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

@Component
public class DynamicDataSource extends AbstractRoutingDataSource {

	private static final ThreadLocal<String> dataSourceHolder = new ThreadLocal<String>();
	private List<String> slaveDataSourcesKeys;
	private List<String> masterDataSourcesKeys;
	private Map<String, DataSource> slavetDataSources;
	private Map<String, DataSource> masterDataSources;

	public DynamicDataSource(Map<String, DataSource> _slavetDataSources, Map<String, DataSource> _masterDataSources, Object _defaultTargetDataSource) {
		this.setSlavetDataSources(_slavetDataSources);
		this.setMasterDataSources(_masterDataSources);
		this.setDefaultTargetDataSource(_defaultTargetDataSource);
	}

	@Override
	protected Object determineCurrentLookupKey() {
		return dataSourceHolder.get();
	}

	@Override
	public void afterPropertiesSet() {
		Map<Object, Object> allDataSources = new HashMap<Object, Object>();
		allDataSources.putAll(masterDataSources);
		if (slavetDataSources != null) {
			allDataSources.putAll(slavetDataSources);
		}
		super.setTargetDataSources(allDataSources);
		super.afterPropertiesSet();
	}

	public void setSlavetDataSources(Map<String, DataSource> slavetDataSources) {
		if (slavetDataSources == null || slavetDataSources.size() == 0) {
			return;
		}
		this.slavetDataSources = slavetDataSources;
		slaveDataSourcesKeys = new ArrayList<String>();
		for (Entry<String, DataSource> entry : slavetDataSources.entrySet()) {
			slaveDataSourcesKeys.add(entry.getKey());
		}
	}

	public void setMasterDataSources(Map<String, DataSource> masterDataSources) {
		if (masterDataSources == null) {
		}
		this.masterDataSources = masterDataSources;
		this.masterDataSourcesKeys = new ArrayList<String>();
		for (Entry<String, DataSource> entry : masterDataSources.entrySet()) {
			masterDataSourcesKeys.add(entry.getKey());
		}
	}

	public void markSlave() {
		if (dataSourceHolder.get() != null) {
		}
		String dataSourceKey = selectFromSlave();
		setDataSource(dataSourceKey);
	}

	public void markMaster() {
		if (dataSourceHolder.get() != null) {
		}
		String dataSourceKey = selectFromMaster();
		setDataSource(dataSourceKey);
	}

	public void markRemove() {
		dataSourceHolder.remove();
	}

	public boolean hasBindedDataSourse() {
		boolean hasBinded = dataSourceHolder.get() != null;
		return hasBinded;
	}

	private String selectFromSlave() {
		if (slavetDataSources == null) {
			return selectFromMaster();
		} else {
			return slaveDataSourcesKeys.get(RandomUtils.nextInt(slaveDataSourcesKeys.size()));
		}
	}

	private String selectFromMaster() {
		String dataSourceKey = masterDataSourcesKeys.get(RandomUtils.nextInt(masterDataSourcesKeys.size()));
		return dataSourceKey;
	}

	private void setDataSource(String dataSourceKey) {
		dataSourceHolder.set(dataSourceKey);
	}

}
