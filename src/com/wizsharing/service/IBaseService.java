package com.wizsharing.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.wizsharing.entity.Parameter;
import com.wizsharing.pagination.Page;

public interface IBaseService<T> {
	
	public Serializable add(T bean) throws Exception;
	 
	public void saveOrUpdate(T bean) throws Exception;
	
	public void delete(T bean) throws Exception;
	
	public void update(T bean) throws Exception;
	 
	public T getBean(final Class<T> obj,final Serializable id) throws Exception;
	 
	/**
	 * 条件查询分页
	 * @param tableSimpleName
	 * @param param
	 * @param map
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<T> findListPage(String tableSimpleName, Parameter param, Map<String, Object> params, Page<T> page) throws Exception;
	
	/**
	 * 获取数量
	 * @param tableSimpleName
	 * @param param
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Long getCount(String tableSimpleName, Parameter param, Map<String, Object> params) throws Exception;
	
	/**
	 * 条件查询
	 * @param tableSimpleName
	 * @param columns
	 * @param values
	 * @param orderBy
	 * @param orderType
	 * @return
	 * @throws Exception
	 */
	public List<T> findByWhere(String tableSimpleName, Map<String, Object> params) throws Exception;

	/**
	 * 获取唯一数据
	 * @param tableSimpleName
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public T findUnique(String tableSimpleName, Map<String, Object> params) throws Exception;
	
	/**
	 * 批量执行HQL 响应数目
	 * @param hql
	 * @return
	 * @throws Exception
	 */
	public Integer executeHql(final String hql) throws Exception;
	
	/**
	 * 批量执行HQL (更新) 响应数目
	 * @param hql
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Integer executeHql(String hql, Map<String, Object> params) throws Exception;
	
	/**
	 * 查询
	 * @param hql
	 * @return
	 * @throws Exception
	 */
	public List<T> find(final String hql) throws Exception;
	
	/**
	 * 按条件查询hql
	 * @param hql
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<T> find(String hql, Map<String, Object> params) throws Exception;
	
	
}
