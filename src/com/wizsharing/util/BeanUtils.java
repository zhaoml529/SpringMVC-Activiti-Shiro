package com.wizsharing.util;

import java.util.List;
import java.util.Map;

/**
 * 
 * @ClassName: BeanUtils
 * @Description:工具类，判断bean，数组，集合是否为空
 * @author: zml
 * @date: 2015-8-6 上午11:20:26
 *
 */
public class BeanUtils {

	public static boolean isBlank(Object obj){
		if(obj == null){
			return true;
		}
		return false;
	}
	public static boolean isBlank(List list){
		if(list == null || list.size()<=0){
			return true;
		}
		return false;
	}
	public static boolean isBlank(Map map){
		if(map == null || map.size()<=0){
			return true;
		}
		return false;
	}
	public static boolean isBlank(Object []obj){
		if(obj == null || obj.length<=0){
			return true;
		}
		return false;
	}
}
