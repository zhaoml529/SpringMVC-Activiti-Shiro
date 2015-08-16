package com.wizsharing.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.wizsharing.dao.IJdbcDao;
import com.wizsharing.pagination.Page;
import com.wizsharing.util.Constants;


@Repository
public class JdbcDaoImpl implements IJdbcDao {

	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Override
	public int saveOrUpdate(String sql, Map<String, Object> paramMap) {
		return this.namedParameterJdbcTemplate.update(sql, paramMap);
	}

	@Override
	public int delete(String sql, Map<String, Object> paramMap) {
		return this.namedParameterJdbcTemplate.update(sql, paramMap);
	}

	@Override
	public int getCount(String sql, Map<String, Object> paramMap) {
		return this.namedParameterJdbcTemplate.queryForObject(sql, paramMap, Integer.class);
	}
	
	@Override
	public int[] batchExecute(String sql, final List<Object[]> paramList) {
		return this.jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			// 返回更新的记录数
			@Override
			public int getBatchSize() {
				return paramList.size();
			}

			// 设置参数
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Object[] objs = paramList.get(i);
				for (int j = 0, length = objs.length; j < length; j++) {
					ps.setObject(j + 1, objs[j]);
				}
			}
		});
	}

	@Override
	public List<Map<String, Object>> find(String sql, Map<String, Object> paramMap, Page<Map<String, Object>> page) {
		Integer totalSum = 0;
		String countSql = "SELECT COUNT(1) FROM (" + sql + ") TEMP_TABLE_";
		totalSum = this.getCount(countSql, paramMap);
		int[] pageParams = page.getPageParams(Long.valueOf(totalSum));
		if(totalSum == 0){
			return Collections.emptyList();
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if (Constants.DB_NAME.equals("oracle")) {
			list = this.queryForOracle(sql, paramMap, pageParams);
			return list;
		} else if (Constants.DB_NAME.equals("mysql")) {
			list = this.queryForMysql(sql, paramMap, pageParams);
			return list;
		}
		page.setResult(list);
		return Collections.emptyList();
	}
	
	private List<Map<String, Object>> queryForOracle(String sql, Map<String, Object> paramMap, int[] pageParams) {
		int firstResult = pageParams[0];
		int maxResults = pageParams[1];
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * FROM (");
		sb.append("SELECT TEMP_TABLE_.*, ROWNUM ROWNUM_ FROM (").append(sql).append(") TEMP_TABLE_");
		sb.append(" WHERE ROWNUM <= ").append(firstResult + maxResults).append(")");
		sb.append(" WHERE ROWNUM_ > ").append(firstResult);
		return this.findAll(sb.toString(), paramMap);
	}

	private List<Map<String, Object>> queryForMysql(String sql, Map<String, Object> paramMap, int[] pageParams) {
		int firstResult = pageParams[0];
		int maxResults = pageParams[1];
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * FROM (").append(sql).append(") TEMP_TABLE_");
		sb.append(" LIMIT ").append(firstResult).append(", ").append(maxResults);
		return this.findAll(sb.toString(), paramMap);
	}

	@Override
	public List<Map<String, Object>> findAll(String sql, Map<String, Object> paramMap) {
		return this.namedParameterJdbcTemplate.query(sql, paramMap, new RowMapper<Map<String, Object>>() {
			@Override
			public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
				Map<String, Object> map = new HashMap<String, Object>();
				ResultSetMetaData meta = rs.getMetaData();
				for (int i = 1, count = meta.getColumnCount(); i <= count; i++) {
					Object obj = rs.getObject(i);
					String columnLabel = meta.getColumnLabel(i).toUpperCase();
					if (obj instanceof java.sql.Clob) {
						java.sql.Clob clob = rs.getClob(i);
						map.put(columnLabel, clob.getSubString((long) 1, (int) clob.length()));
					} else if (obj instanceof java.sql.Date || obj instanceof java.sql.Timestamp) {
						java.sql.Timestamp timestamp = rs.getTimestamp(i);
						map.put(columnLabel, timestamp);
					} else {
						map.put(columnLabel, obj);
					}
				}
				return map;
			}
		});
	}

}
