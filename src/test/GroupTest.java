package test;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wizsharing.entity.Group;
import com.wizsharing.service.IGroupService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/applicationContext.xml", "classpath*:/springMVC.xml" })
public class GroupTest {
	
	@Autowired
	private IGroupService groupService;
	
    @Test
	public void groupTest() throws Exception{
		Group group = new Group();
		group.setName("group1");
		group.setType("group_A");
		this.groupService.doAdd(group);
		
		Group group2 = new Group();
		group2.setName("group2");
		group2.setType("group_B");
		
		this.groupService.doAdd(group2);
		
		List<Group> list = this.groupService.getGroupList();
		assertEquals(list.size(), 2);
		assertEquals(list.get(0).getName(), "group1");
		
	}
}
