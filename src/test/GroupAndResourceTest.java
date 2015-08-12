package test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wizsharing.entity.GroupAndResource;
import com.wizsharing.service.IGroupAndResourceService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/applicationContext.xml", "classpath*:/springMVC.xml" })
public class GroupAndResourceTest {

	@Autowired
	private IGroupAndResourceService gsService;
	
	@Test
	public void grTest() throws Exception{
		GroupAndResource gr1 = new GroupAndResource();
		gr1.setGroupId(1);
		gr1.setResourceId(1);
		this.gsService.doAdd(gr1);
		
		GroupAndResource gr2 = new GroupAndResource();
		gr2.setGroupId(1);
		gr2.setResourceId(2);
		this.gsService.doAdd(gr2);
		
		GroupAndResource gr3 = new GroupAndResource();
		gr3.setGroupId(1);
		gr3.setResourceId(3);
		this.gsService.doAdd(gr3);
		
		GroupAndResource gr4 = new GroupAndResource();
		gr4.setGroupId(1);
		gr4.setResourceId(4);
		this.gsService.doAdd(gr4);
	}
}
