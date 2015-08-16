package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wizsharing.dao.IJdbcDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/applicationContext.xml", "classpath*:/springMVC.xml" })
public class JdbcTest {
	@Autowired
	private IJdbcDao jdbcDao;
	
	@Test
	public void jdbcTest() {
		String sql = "update T_USER set GROUP_ID = 2 where USER_ID = ?";
		
		List<Object[]> paramList = new ArrayList<Object[]>();
		Integer[] param1 = {2};
		Integer[] param2 = {3};
		Integer[] param3 = {4};
		Integer[] param4 = {5};
		paramList.add(param1);
		paramList.add(param2);
		paramList.add(param3);
		paramList.add(param4);
		
		//this.jdbcDao.batchExecute(sql, paramList);
		
		String sql2 = "insert into T_USER values(SEQ_USER_ID.nextval, ?, ?, ?, to_date(?,'yyyy-mm-dd hh24:mi:ss'), ?, ?)";
		List<Object[]> paramList2 = new ArrayList<Object[]>();
		String[] param21 = {"0", "user_temp1", "123", "2015-08-17 00:00:00", "123", "2"};
		String[] param22 = {"0", "user_temp2", "123", "2015-08-17 00:01:20", "123", "2"};
		paramList2.add(param21);
		paramList2.add(param22);
//		int[] a = this.jdbcDao.batchExecute(sql2, paramList2);
//		System.out.println(a +"!!!!!");
		
		String sql3 = "select * from T_USER where is_delete = :isDelete and group_id = :groupId";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("isDelete", 0);
		paramMap.put("groupId", 2);
		List<Map<String, Object>> list = this.jdbcDao.findAll(sql3, paramMap);
		for(Map<String, Object> map : list) {
			System.out.println(map.get("USER_ID")+"---"+map.get("USER_NAME"));
		}
	}
}
