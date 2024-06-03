package com.rindus.testapi.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.servlet.ServletContext;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rindus.testapi.Application;
import com.rindus.testapi.model.Post;
import com.rindus.testapi.service.interfaces.TestAPIService;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class TestAPIControllerTest {
	
	@InjectMocks
	TestAPIControllerImpl controller;
	
	@Mock
	TestAPIService testService;
	
	@Autowired
	MockMvc mvc;
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@Test
	public void testServletContextProvidesTestAPIController() {
	    ServletContext servletContext = webApplicationContext.getServletContext();
 	    
	    assertNotNull(servletContext);	    
	    assertTrue(servletContext instanceof MockServletContext);
	    assertNotNull(webApplicationContext.getBean("testAPIControllerImpl"));
	}
	
	@Test
	public void testGetAllPosts() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/testapi/posts").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}
	
	@Test
	public void testAllPostComments() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/testapi/posts/{id}/comments", 2).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}
	
	@Test
	public void testGetAllComments() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/testapi/comments").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	public void testGetPost() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/testapi/posts/{id}", 1).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	public void testGetPostsNotFound() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/testapi/posts/{id}", 9999).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testCreatePosts() throws Exception {
		Post post = createPost(101, "body", 2, "title");		

		mvc.perform(MockMvcRequestBuilders.post("/testapi/posts").content(asJsonString(post)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
	}
	
	@Test
	public void testUpdatePosts() throws Exception  {
		Post post = createPost(101, "body", 2, "title");
		
	    mvc.perform( MockMvcRequestBuilders.put("/testapi/posts/{id}", 1)
	      .content(asJsonString(post))
	      .contentType(MediaType.APPLICATION_JSON)
	      .accept(MediaType.APPLICATION_JSON))
	      .andExpect(status().isOk());
	}
	
	@Test
	public void testModifyPosts() throws Exception  {
		Post post = createPost(101, "body", null, null);
		
	    mvc.perform(MockMvcRequestBuilders.patch("/testapi/posts/{id}", 1)
	      .content(asJsonString(post))
	      .contentType(MediaType.APPLICATION_JSON)
	      .accept(MediaType.APPLICATION_JSON))
	      .andExpect(status().isOk());
	}
	
	
	@Test
	public void testDeletePosts() throws Exception {
		mvc.perform(MockMvcRequestBuilders.delete("/testapi/posts/{id}", 1))
        .andExpect(status().isOk());
	}
	
	/**
	 * Create a new Post object using the parameters received
	 * @param id Post id
	 * @param body Post body
	 * @param userId Post userId
	 * @param title Post title
	 * @return new Post object
	 */
	private Post createPost(Integer id, String body, Integer userId, String title) {
		Post post = new Post();
		post.setId(id);
		post.setBody(body);
		post.setUserId(userId);
		post.setTitle(title);
		return post;
	}
	
	/**
	 * Convert an object to json
	 * @param obj Object
	 * @return String with json representation
	 */
	private static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	

}
