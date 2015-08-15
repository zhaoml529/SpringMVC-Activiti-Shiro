package com.wizsharing.entity;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
/**
 * easyui分页组件datagrid
 * @author ZML
 *
 */
public class Datagrid<T> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<T>  rows= Collections.emptyList();
	private Long total=0L;
	
	public Datagrid(){
		
	}
	
	public Datagrid(Long total, List<T> rows){
		this.rows = rows;
		this.total = total;
	}
	
	public List<T> getRows() {
		return rows;
	}
	
	public void setRows(List<T> rows) {
		this.rows = rows;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}
	
}
