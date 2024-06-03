package com.rindus.testapi.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rindus.testapi.client.interfaces.TestAPIClient;
import com.rindus.testapi.model.Comment;
import com.rindus.testapi.model.Post;

@ExtendWith(MockitoExtension.class)
public class TestAPIServiceTest {
	
	@InjectMocks
	TestAPIServiceImpl testapiService;
		
	@Mock
	TestAPIClient testapiClient;	
	
	@Mock
	RestTemplate restTemplate;
	
	@Mock
	ObjectMapper mapper;
	
	@Mock 
	JSONObject jsonObject;
	
	@Test
    public void testGetAllPosts() throws ServiceException {		
		//Prepare
		when(testapiClient.getAllPosts()).thenReturn(new ResponseEntity<String>("body", HttpStatus.OK));
				
		//Execute
		ResponseEntity<List<Post>> result = testapiService.getAllPosts();
		
		//Test
		assertThat(result).isNotNull();
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);		
	}
	
    @Test
    public void testAllComments() throws ServiceException {
    	//Prepare
    	when(testapiClient.getAllComments()).thenReturn(new ResponseEntity<String>("body", HttpStatus.OK));
    	
    	//Execute
    	ResponseEntity<List<Comment>> result = testapiService.getAllComments();
    	    	
    	//Test
    	assertThat(result).isNotNull();
    	assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);		    
    }
    
	@Test
	public void testGetPost() throws ServiceException {
		//prepare
		Integer id = 1;			
		Post post = createPost(id, "body", 2, "title");
		when(testapiClient.getPost(id)).thenReturn(new ResponseEntity<Post>(post, HttpStatus.OK));	    
		
		//Execute
    	ResponseEntity<Post> result = testapiService.getPost(id);
		
    	//results
		assertThat(result.getBody()).isNotNull();
		assertThat(result.getBody().getId()).isEqualTo(id);
		assertThat(result.getBody().getUserId()).isEqualTo(2);
		assertThat(result.getBody().getTitle()).isEqualTo("title");
		assertThat(result.getBody().getBody()).isEqualTo("body");		
	}
	
	@Test
    public void testGetAllPostComments() throws ServiceException {		
		//Prepare
		Integer id = 1;		
		when(testapiClient.getAllPostComment(id)).thenReturn(new ResponseEntity<String>("comments", HttpStatus.OK));
				
		//Execute
		ResponseEntity<List<Comment>> result = testapiService.getAllPostComment(id);
				
		//Test
		assertThat(result).isNotNull();
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);		
	}
	
	@Test
	public void testAddPost() throws ServiceException {
		//prepare				
		Post post = createPost(1, "body", 2, "title");
		when(testapiClient.addPost(post)).thenReturn(new ResponseEntity<String>("body", HttpStatus.CREATED));	    
		
		//Execute
    	ResponseEntity<Post> result = testapiService.addPost(post);
		
    	//results		
		assertThat(result).isNotNull();		
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
	}
	
	@Test
	public void testDeletePost() throws ServiceException {
		//prepare
		Post post = createPost(1, "body", 2, "title");
		Integer id = 1;
		testapiClient.deletePost(id);
		when(testapiService.getPost(id)).thenReturn(new ResponseEntity<Post>(post, HttpStatus.OK));
		verify(testapiClient, times(1)).deletePost(id);		
				
		//Execute
    	ResponseEntity<HttpStatus> result = testapiService.deletePost(id);
		    	
    	//results		
		assertThat(result).isNotNull();	
		
	}
	
	@Test
	public void testUpdatePost() throws ServiceException {
		//prepare
		Integer id = 1;			
		Post post = createPost(id, "body", 2, "title");	
		when(testapiService.getPost(id)).thenReturn(new ResponseEntity<Post>(post, HttpStatus.OK));
		when(testapiClient.updatePost(id, post)).thenReturn(new ResponseEntity<Post>(post, HttpStatus.OK));	
		
		//execute		
		ResponseEntity<Post> result = testapiService.updatePost(1, post);
		
		//results
		assertThat(result.getBody()).isNotNull();
		assertThat(result.getBody().getId()).isEqualTo(id);
		assertThat(result.getBody().getUserId()).isEqualTo(2);
		assertThat(result.getBody().getTitle()).isEqualTo("title");
		assertThat(result.getBody().getBody()).isEqualTo("body");	
	}
	
	@Test
	public void testModifyPost() throws ServiceException {
		//prepare
		Integer id = 1;			
		Post post = createPost(id, "new body", null, null);	
		when(testapiService.getPost(id)).thenReturn(new ResponseEntity<Post>(post, HttpStatus.OK));
		when(testapiClient.modifyPost(id, post)).thenReturn(new ResponseEntity<Post>(post, HttpStatus.OK));	
		
		//execute		
		ResponseEntity<Post> result = testapiService.modifyPost(1, post);
		
		//results
		assertThat(result.getBody()).isNotNull();
		assertThat(result.getBody().getId()).isEqualTo(id);
		assertThat(result.getBody().getBody()).isEqualTo("new body");	
	}
	
	@Test
	public void testModifyPostNotFound() throws ServiceException {
		//prepare
		Integer id = 1;			
		Post post = createPost(id, "new body", null, null);	
		when(testapiService.getPost(id)).thenReturn(new ResponseEntity<Post>(new Post(), HttpStatus.NOT_FOUND));
		
		//execute			
		ServiceException serviceException = assertThrows(ServiceException.class, () -> {testapiService.modifyPost(1, post);});;

		//results
		assertThat(serviceException).isNotNull();
		assertThat(serviceException.getMessage()).isEqualTo("[modifyPost] - Post not exit");
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
}
