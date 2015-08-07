package test;

import static junit.framework.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wizsharing.dao.IJdbcDao;
import com.wizsharing.entity.Group;
import com.wizsharing.entity.User;
import com.wizsharing.service.IGroupService;
import com.wizsharing.service.IUserService;
import com.wizsharing.service.impl.PasswordHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/applicationContext.xml", "classpath*:/springMVC.xml" })
public class UserTest {
	
	@Autowired
	private IJdbcDao jdbcDao;
	
    @Autowired
    private PasswordHelper passwordHelper;
    
    @Autowired 
	private IUserService userService;
    
    @Autowired
    private IGroupService groupService;
	
    @Test
	public void userTest() throws Exception{
    	Group group = this.groupService.getGroupById("10");
    	assertEquals( group.getName(), "group1" );
    	assertEquals( group.getType(), "group_A" );
    	
		User user = new User();
		user.setName("admin");
		user.setPasswd("123");
		user.setRegisterDate(new Date());
		user.setLocked(0);
		user.setGroup(group);
//		this.userService.doAdd(user, false);
		
		//修改密码时判断，原密码是否输入正确
		User u = this.userService.getUserById(5);
		String oldPass = u.getPasswd();
		
		u.setPasswd("123");
		this.passwordHelper.encryptPassword(u);
		
        String newPass = u.getPasswd(); 
        assertEquals(oldPass, newPass);
	}
    
}
