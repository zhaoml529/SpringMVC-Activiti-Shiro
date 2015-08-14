package com.wizsharing.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wizsharing.dao.IBaseDao;
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
	public Integer getCount(String tableSimpleName, String[] columns, String[] values) throws Exception{
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
				Integer count = this.baseDao.getCount(sb.toString());  
	            return count;  
			}else{  
	            return 0;  
	        }
		}
		return 0; 
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
	public List<T> findByQuery(Class<T> entity, String[] columns,
			String[] querys) throws Exception {
		List<T> list =  this.baseDao.findByQuery(entity, columns, querys);
		if(BeanUtils.isBlank(list)){
      		return Collections.emptyList();
       }else{
      		return list;
       } 
	}

	@Override
	public List<T> findByPage(String tableSimpleName, String[] columns,
			String[] values, String[] orderBy, String[] orderType, Page<T> page)
			throws Exception {
		Integer totalSum = 0;
		List<T> list = new ArrayList<T>();
		if(columns != null && values != null){
        	if(columns.length > 0 && columns.length==values.length){  
        		list = findByWhere(tableSimpleName, columns, values, orderBy, orderType);
        	}else{
        		list = getAllList(tableSimpleName, orderBy, orderType);
        	}
		}else{
			list = getAllList(tableSimpleName, orderBy, orderType);
		}
		
		if(!BeanUtils.isBlank(list)){
			totalSum = list.size();
		}
		int[] pageParams = page.getPageParams(totalSum);
		
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
        
//        String hql = sb.toString();
//        if(hql.endsWith("where ")){
//    	    hql = hql.substring(0, hql.length()-6);
//        }
        
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
        
        String hql = sb.toString();
        
        logger.info("findByPage: HQL: "+hql);
        list = this.baseDao.findByPage(hql, pageParams[0], pageParams[1]); 
        if( list.size()>0 ){
        	page.setResult(list);
    	    return list;
        }else{
    	    return Collections.emptyList();
        }
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
