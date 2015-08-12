package test;

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
    @Autowired  
    private WebApplicationContext wac;  
	private MockMvc mockMvc;  
	  
    @Before  
    public void setUp() {  
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();  
    }  
	
	@Test
	public void resourceTest() throws Exception{
		List<Resource> list = this.resourceService.getTree(10);
		System.out.println(list.size());
		Assert.assertEquals(list.size(), 9);
	}
	
	@Test
	public void updateSortTest() throws Exception{
		String requestBody = "{\"parentId\":1, \"sortArr\":\"zhang\"}";  
		mockMvc.perform(MockMvcRequestBuilders.post("/resource/updateTreeSort")  
		            .contentType(MediaType.APPLICATION_JSON).content(requestBody)  
		            .accept(MediaType.APPLICATION_JSON)) //执行请求
		            .andReturn();
	}
}
