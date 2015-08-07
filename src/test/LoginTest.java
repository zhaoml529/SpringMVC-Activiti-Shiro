package test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wizsharing.dao.IJdbcDao;
import com.wizsharing.entity.User;
import com.wizsharing.service.IBaseService;
import com.wizsharing.service.impl.PasswordHelper;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/applicationContext.xml", "classpath*:/springMVC.xml" })
public class LoginTest {
	
	@Autowired
	private IJdbcDao jdbcDao;
	
    @Autowired
    private PasswordHelper passwordHelper;
    
    @Autowired 
	private IBaseService<User> baseService;
	
	@Test
	public void testLogin(){
		
		
	}
}
