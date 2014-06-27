package com.ctfo.dao.impl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import com.ctfo.dao.TestSaveCenterDao;
import com.ctfo.statement.SystemStatement;
import com.ctfo.util.PropertiesUtil;

/**
 * 数据库操作
 * 
 * @author James 2014-3-15 1:06:04
 * 
 */
public class TestSaveCenterDaoImpl implements TestSaveCenterDao {

	private Logger logger = Logger.getLogger(TestSaveCenterDaoImpl.class);

	private JdbcTemplate jdbcTemplate;

	private String sql_queryVehicle = PropertiesUtil.PROPERTIES.readResource(SystemStatement.SQL_PROPERTIES, "sql_queryVehicle");

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<Map<String, Object>> queryVehicle(long startSim, long endSim) {
		try {
			return jdbcTemplate.queryForList(sql_queryVehicle, new Object[] { startSim, endSim });
		} catch (Exception e) {
			logger.error("queryVehicle DAO ERROR!", e);
			return null;
		}
	}

}
