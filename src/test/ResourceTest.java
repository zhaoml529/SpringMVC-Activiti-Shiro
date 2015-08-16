package test;

import java.io.Serializable;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.opensymphony.xwork2.interceptor.annotations.Before;
import com.wizsharing.entity.Resource;
import com.wizsharing.service.IResourceService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/applicationContext.xml", "classpath*:/springMVC.xml" })
public class ResourceTest {

	@Autowired
    private IResourceService resourceService;
//    @Autowired  
//    private WebApplicationContext wac;  
//	private MockMvc mockMvc;  
	  
//    @Before  
//    public void setUp() {  
//        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();  
//    }  
	
	@Test
	public void resourceTest() throws Exception{
		Resource r1 = new Resource();
		r1.setName("ROOT");
		r1.setNote("主菜单");
		r1.setParentId(0);
		r1.setPermission("admin:*");
		r1.setType("menu");
		r1.setUrl("");
		r1.setIsDelete(1);
		Serializable id = this.resourceService.doAdd(r1);
		
		Resource r2 = new Resource();
		r2.setName("菜单1");
		r2.setNote("");
		r2.setParentId((Integer) id);
		r2.setPermission("admin:*");
		r2.setType("menu");
		r2.setUrl("");
		r2.setIsDelete(1);
		Serializable idr2 = this.resourceService.doAdd(r2);
		
		Resource r4 = new Resource();
		r4.setName("菜单1-1");
		r4.setNote("");
		r4.setParentId((Integer) idr2);
		r4.setPermission("admin:*");
		r4.setType("menu");
		r4.setUrl("");
		r4.setIsDelete(1);
		this.resourceService.doAdd(r4);
		
		Resource r3 = new Resource();
		r3.setName("菜单2");
		r3.setNote("");
		r3.setParentId((Integer) id);
		r3.setPermission("admin:*");
		r3.setType("menu");
		r3.setUrl("");
		r3.setIsDelete(1);
		this.resourceService.doAdd(r3);
	}
	
//	@Test
//	public void updateSortTest() throws Exception{
////		String requestBody = "{\"parentId\":1, \"sortArr\":\"zhang\"}";  
////		mockMvc.perform(MockMvcRequestBuilders.post("/resource/updateTreeSort")  
////		            .contentType(MediaType.APPLICATION_JSON).content(requestBody)  
////		            .accept(MediaType.APPLICATION_JSON)) //执行请求
////		            .andReturn();
//		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//	}
}
