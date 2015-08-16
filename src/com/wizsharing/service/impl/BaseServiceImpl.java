package com.wizsharing.service.impl;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wizsharing.dao.IBaseDao;
import com.wizsharing.entity.Parameter;
import com.wizsharing.pagination.Page;
import com.wizsharing.service.IBaseService;



@Service
public class BaseServiceImpl<T> implements IBaseService<T> {
	
	@Autowired  
    private IBaseDao<T> baseDao;  
	
	@Override
	public Serializable add(T bean) throws Exception{
		return this.baseDao.add(bean);
	}

	@Override
	public void saveOrUpdate(T bean) throws Exception{
		this.baseDao.saveOrUpdate(bean);
	}

	@Override
	public void delete(T bean) throws Exception{
		this.baseDao.delete(bean);
	}

	@Override
	public void update(T bean) throws Exception{
		this.baseDao.update(bean);
	}

	@Override
	public T getBean(Class<T> obj, Serializable id) throws Exception{
		return this.baseDao.getBean(obj, id);
	}
	
	@Override
	public List<T> findListPage(String tableSimpleName, Parameter param, Map<String, Object> map, Page<T> page) throws Exception {
		StringBuffer sb = new StringBuffer();  
        sb.append("select a from ").append(tableSimpleName).append(" a where a.isDelete = 0 ");  
        
        //普通模糊查询
        if(param != null && StringUtils.isNotBlank(param.getSearchValue())){
        	//如果查询的字段中有日期
        	if(param.getSearchName().toLowerCase().indexOf("date") >= 0){
        		sb.append(" and to_char(a." + param.getSearchName() + ", 'yyyy-MM-dd') like '%" + param.getSearchValue() + "%' ");
        	}else{
        		sb.append(" and a." + param.getSearchName() + " like '%" + param.getSearchValue() + "%' ");
        	}
        }
        
        //自定义查询条件
        if (map != null && !map.isEmpty()) {
        	for(Map.Entry<String, Object> entry:map.entrySet()){    
        	     sb.append(" and a." + entry.getKey() + " = "+entry.getValue()+" ");
        	}   
        }
        
        //高级查询
        if(param != null && param.getSearchColumnNames() != null && param.getSearchColumnNames().trim().length() > 0){
	        String[] searchColumnNameArray=param.getSearchColumnNames().split(",");
			String[] searchAndsArray=param.getSearchAnds().split(",");
			String[] searchConditionsArray=param.getSearchConditions().split(",");
			String[] searchValsArray=param.getSearchVals().split(",");
	        
			if(searchColumnNameArray.length >0 ){
				for (int i = 0; i < searchColumnNameArray.length; i++) {
					if (searchColumnNameArray[i].trim().length() > 0 && searchConditionsArray[i].trim().length()>0) {
						String value=searchValsArray[i].trim().replaceAll("\'", "");
						if ("like".equals(searchConditionsArray[i].trim())){
							if(searchColumnNameArray[i].trim().toLowerCase().indexOf("date") >= 0){
								sb.append(" " + searchAndsArray[i].trim() + " to_char(a." + searchColumnNameArray[i].trim() + ", 'yyyy-MM-dd') " + searchConditionsArray[i].trim() + " " +"'%"+ value+"%'");
							}else{
								sb.append(" " + searchAndsArray[i].trim() + " a. " + searchColumnNameArray[i].trim() + " " + searchConditionsArray[i].trim() + " " +"'%"+ value+"%'");
							}
						}else {
							if(searchColumnNameArray[i].trim().toLowerCase().indexOf("date") >= 0){
								sb.append(" " + searchAndsArray[i].trim() + " to_char(a." + searchColumnNameArray[i].trim() + ", 'yyyy-MM-dd') " + searchConditionsArray[i].trim() + " " +"'"+ value+"'");
							}else{
								sb.append(" " + searchAndsArray[i].trim() + " a. " + searchColumnNameArray[i].trim() + " " + searchConditionsArray[i].trim() + " " +"'"+ value+"'");
							}
						}
					}
				}
			}
        }
		
		if(param != null && StringUtils.isNotBlank(param.getSort())) {
			sb.append(" order by a." + param.getSort() + " " + param.getOrder());
		}
		
		String hql = sb.toString();
		Long total = getCount(tableSimpleName, param, map);
		
		int[] pageParams = page.getPageParams(total);
		List<T> list = this.baseDao.findByPage(hql, pageParams[0], pageParams[1]); 
        if( list.size()>0 ){
        	page.setResult(list);
    	    return list;
        }else{
    	    return Collections.emptyList();
        }
	}
	
	@Override
	public Long getCount(String tableSimpleName, Parameter param, Map<String, Object> map) throws Exception{
		StringBuffer sb = new StringBuffer();  
        sb.append("select count(*) from ").append(tableSimpleName).append(" a where a.isDelete = 0 ");
        
        //普通模糊查询
        if(param != null && StringUtils.isNotBlank(param.getSearchValue())){
        	//如果查询的字段中有日期
        	if(param.getSearchName().toLowerCase().indexOf("date") >= 0){
        		sb.append(" and to_char(a." + param.getSearchName() + ", 'yyyy-MM-dd') like '%" + param.getSearchValue() + "%' ");
        	}else{
        		sb.append(" and a." + param.getSearchName() + " like '%" + param.getSearchValue() + "%' ");
        	}
        }
        
        //自定义查询条件
        if (map != null && !map.isEmpty()) {
        	for(Map.Entry<String, Object> entry:map.entrySet()){    
        	     sb.append(" and a." + entry.getKey() + " = "+entry.getValue()+" ");
        	}   
        }
        
        //高级查询
        if(param != null && param.getSearchColumnNames() != null && param.getSearchColumnNames().trim().length() > 0){
	        String[] searchColumnNameArray=param.getSearchColumnNames().split(",");
			String[] searchAndsArray=param.getSearchAnds().split(",");
			String[] searchConditionsArray=param.getSearchConditions().split(",");
			String[] searchValsArray=param.getSearchVals().split(",");
	        
			if(searchColumnNameArray.length >0 ){
				for (int i = 0; i < searchColumnNameArray.length; i++) {
					if (searchColumnNameArray[i].trim().length() > 0 && searchConditionsArray[i].trim().length()>0) {
						String value=searchValsArray[i].trim().replaceAll("\'", "");
						if ("like".equals(searchConditionsArray[i].trim())){
							if(searchColumnNameArray[i].trim().toLowerCase().indexOf("date") >= 0){
								sb.append(" " + searchAndsArray[i].trim() + " to_char(a." + searchColumnNameArray[i].trim() + ", 'yyyy-MM-dd') " + searchConditionsArray[i].trim() + " " +"'%"+ value+"%'");
							}else{
								sb.append(" " + searchAndsArray[i].trim() + " a. " + searchColumnNameArray[i].trim() + " " + searchConditionsArray[i].trim() + " " +"'%"+ value+"%'");
							}
						}else {
							if(searchColumnNameArray[i].trim().toLowerCase().indexOf("date") >= 0){
								sb.append(" " + searchAndsArray[i].trim() + " to_char(a." + searchColumnNameArray[i].trim() + ", 'yyyy-MM-dd') " + searchConditionsArray[i].trim() + " " +"'"+ value+"'");
							}else{
								sb.append(" " + searchAndsArray[i].trim() + " a. " + searchColumnNameArray[i].trim() + " " + searchConditionsArray[i].trim() + " " +"'"+ value+"'");
							}
						}
					}
				}
			}
        }
		String hql = sb.toString();
		return this.baseDao.getCount(hql);
	}
	
	@Override
	public List<T> findByWhere(String tableSimpleName, Map<String, Object> params) throws Exception {
		//如果有排序的需求可以加个String[] orderBy, String[] orderType变量来扩展
		StringBuffer sb = new StringBuffer();  
        sb.append("select a from ").append(tableSimpleName).append(" a where a.isDelete = 0 ");  
        
        if (params != null && !params.isEmpty()) {
        	for (String name : params.keySet()){
        	     sb.append(" and a." + name + " = :" + name);
        	}   
        }
        String hql = sb.toString();
        List<T> list = this.baseDao.find(hql, params);  
        if( list.size()>0 ){
    	    return list;
        }else{
    	    return Collections.emptyList();
        }
	}
	
	@Override
	public T findUnique(String tableSimpleName, Map<String, Object> params) throws Exception{
		StringBuffer sb = new StringBuffer();  
        sb.append("select a from ").append(tableSimpleName).append(" a where a.isDelete = 0 ");  
        
        if (params != null && !params.isEmpty()) {
        	for (String name : params.keySet()){
        	     sb.append(" and a." + name + " = :" + name);
        	}   
        }
        String hql = sb.toString();
        return this.baseDao.unique(hql, params);
	}
	
	@Override
	public Integer executeHql(String hql) throws Exception {
		return this.baseDao.executeHql(hql);
	}

	@Override
	public Integer executeHql(String hql, Map<String, Object> params) throws Exception {
		return this.baseDao.executeHql(hql, params);
	}

	@Override
	public List<T> find(String hql) throws Exception {
		return this.baseDao.find(hql);
	}

	@Override
	public List<T> find(String hql, Map<String, Object> params)
			throws Exception {
		return this.baseDao.find(hql, params);
	}
	

	
}
