package com.wizsharing.service.impl;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wizsharing.dao.IBaseDao;
import com.wizsharing.entity.Parameter;
import com.wizsharing.pagination.Page;
import com.wizsharing.service.IBaseService;
import com.wizsharing.util.BeanUtils;



@Service
public class BaseServiceImpl<T> implements IBaseService<T> {
	private static final Logger logger = Logger.getLogger(BaseServiceImpl.class);
	
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
	public List<T> getAllList(String tableSimpleName, String[] orderBy, String[] orderType) throws Exception{
		StringBuffer sff = new StringBuffer();  
        sff.append("select a from ").append(tableSimpleName).append(" a "); 
        if(orderBy != null && orderType != null){
        	if(orderBy.length > 0 && orderBy.length == orderType.length){
        		sff.append(" order by ");
        		for(int i = 0; i < orderBy.length; i++){
        			sff.append("a.").append(orderBy[i]).append(" ").append(orderType[i]);
        			if(i < orderBy.length-1){
        				sff.append(", ");
        			}
        		}
        	}
        }
        List<T> list = this.baseDao.find(sff.toString()); 
        if(BeanUtils.isBlank(list)){
        	return Collections.emptyList();
        }else{
        	return list;
        }
	}

	@Override
	public T getUnique(String tableSimpleName, String[] columns, String[] values) throws Exception{
		StringBuffer sb = new StringBuffer();  
        sb.append("select a from ").append(tableSimpleName).append(" a ");  
        if(columns != null && values != null){
        	if(columns.length > 0 && columns.length==values.length){  
        		sb.append( " where ");
                for(int i = 0; i < columns.length; i++){  
                    sb.append("a.").append(columns[i]).append("='").append(values[i]).append("'");  
                    if(i < columns.length-1){  
                        sb.append(" and ");  
                    }  
               }
               T entity = this.baseDao.unique(sb.toString());  
               return entity; 
            }else{  
                logger.error("columns.length != values.length");
                return null;  
            }
        }
        return null;
	}
	
	@Override
	public List<T> findByWhere(String tableSimpleName, String[] columns,
			String[] values, String[] orderBy, String[] orderType) throws Exception{
		StringBuffer sb = new StringBuffer();  
        sb.append("select a from ").append(tableSimpleName).append(" a ");  
        if(columns != null && values != null){
        	if(columns.length > 0 && columns.length==values.length){  
        		sb.append( " where ");
                for(int i = 0; i < columns.length; i++){  
     	            sb.append("a.").append(columns[i]).append("='").append(values[i]).append("'");  
     	            if(i < columns.length-1){  
     	                sb.append(" and ");  
     	            }  
                }  
        	}
        }
        if(orderBy != null && orderType != null){
           if(orderBy.length > 0 && orderBy.length == orderType.length){
        	   sb.append(" order by ");
        	   for(int i = 0; i < orderBy.length; i++){
        		   sb.append("a.").append(orderBy[i]).append(" ").append(orderType[i]);
        		   if(i < orderBy.length-1){
        			   sb.append(", ");
        		   }
        	   }
           }
        }
        List<T> list = this.baseDao.find(sb.toString());  
        if(BeanUtils.isBlank(list)){
   		    return Collections.emptyList();
        }else{
   		    return list;
        } 
	}

	@Override
	public Long getCount(String tableSimpleName, String[] columns, String[] values) throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("select count(*) from ").append(tableSimpleName).append(" a ");
		if(columns != null && values != null){
			if(columns.length > 0 && columns.length == values.length){
				sb.append( " where ");
				for(int i = 0; i < columns.length; i++){  
		            sb.append("a.").append(columns[i]).append("='").append(values[i]).append("'");  
		            if(i < columns.length-1){  
		                sb.append(" and ");  
		            }  
	            }
				Long count = this.baseDao.getCount(sb.toString());  
	            return count;  
			}else{  
	            return 0L;  
	        }
		}
		return 0L; 
	}

	/**
	 * 固定参数 columns 和 values 
	 * columns[]{"teacher", "beginDate"}
	 * values[]{teacher_id, beginDate, endDate}
	 */
	@Override
	public List<T> getRangeDate(String tableSimpleName, String[] columns,
			String[] values) throws Exception {
    	StringBuffer sb = new StringBuffer();  
        sb.append("select a from ").append(tableSimpleName).append( " a where a.").append(columns[0]); 
        sb.append("='").append(values[0]).append("'").append(" and a.").append(columns[1]); 
        sb.append(" BETWEEN '").append(values[1]).append("' and '").append(values[2]).append("'");
        String sub = sb.toString();
        List<T> list = this.baseDao.find(sub);
        return list.size()>0?list:null;
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
	
}
