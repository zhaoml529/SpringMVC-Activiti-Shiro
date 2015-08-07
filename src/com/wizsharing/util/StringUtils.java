package com.wizsharing.util;

/**
 * 
 * @ClassName: StringUtils
 * @Description:String工具类
 * @author: zml
 * @date: 2015-8-6 上午10:19:58
 *
 */
public class StringUtils {
	public static boolean isBlank(String...judgeString){
		
		for(String str:judgeString){
			if(str == null || "".equals(str)){
				return true;
			}
		}
		return false;
	}
}
