package com.rindus.testapi.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.rindus.testapi.model.Post;

@ExtendWith(MockitoExtension.class)
class TestAPIClientTest {
	
	private static final String URL = "http://jsonplaceholder.typicode.com/";
	
	@Mock
	RestTemplate restTemplate;
	
	@Mock
	HttpHeaders headers;

	
	@InjectMocks
	private TestAPIClientImpl client;	
	
	@Test
	void shoudlReturnAllPost() {
		//prepare
	    when(restTemplate.getForEntity(URL +"posts", String.class)).thenReturn(new ResponseEntity<String>("Body", HttpStatus.OK));	    
	    //execute
	    client = new TestAPIClientImpl(restTemplate);		 
		ResponseEntity<String> result = client.getAllPosts();
		//results
		assertNotNull(result.getBody());
		assertEquals(result.getStatusCode(), HttpStatus.OK);
	}
	
	@Test
	void shoudlReturnPostById() {
		//prepare
		Integer id = 1;			
		Post post = new Post();
		post.setUserId(id);
		post.setTitle("tittle");
		when(restTemplate.getForEntity(URL + "posts/" + id, Post.class)).thenReturn(new ResponseEntity<Post>(post, HttpStatus.OK));	    
		//execute
		client = new TestAPIClientImpl(restTemplate);
		ResponseEntity<Post> result = client.getPost(id);
		//results
		assertNotNull(result.getBody());
		assertEquals(result.getBody().getUserId(), id);
		assertEquals(result.getStatusCode(), HttpStatus.OK);
	}
	
	@Test
	void shoudlReturnNewPost() {
		//prepare
		Post post = new Post();
		post.setId(1000);
		//execute
		when(restTemplate.postForEntity(URL +"posts", post, String.class)).thenReturn(new ResponseEntity<String>("Body", HttpStatus.CREATED));
		client = new TestAPIClientImpl(restTemplate);
		ResponseEntity<String> entity = client.addPost(post);
		//results
		assertNotNull(entity);	
		assertEquals(entity.getBody(), "Body");
		assertEquals(entity.getStatusCode(), HttpStatus.CREATED);		
	}	
	
	@Test
	void shoudlReturnAllComments() {
		//prepare
	    when(restTemplate.getForEntity(URL +"comments", String.class)).thenReturn(new ResponseEntity<String>("Body", HttpStatus.OK));	    
	    //execute
	    client = new TestAPIClientImpl(restTemplate);		 
		ResponseEntity<String> result = client.getAllComments();
		//results
		assertNotNull(result);
		assertNotNull(result.getBody());
		assertEquals(result.getStatusCode(), HttpStatus.OK);
	}
	
	@Test
	void shoudlReturnUpdatedPost() {
		//prepare
		Post post = createPost();	
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL + "posts/1");
		HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
		when(restTemplate.exchange(builder.toUriString(), HttpMethod.PUT, new HttpEntity<Post>(post, headers), Post.class))
		.thenReturn(new ResponseEntity<Post>(new Post(), HttpStatus.OK));
		
		//execute		
		client = new TestAPIClientImpl(restTemplate);
		ResponseEntity<Post> entity = client.updatePost(1, post);
		
		//results
		assertNotNull(entity);
		assertNotNull(entity.getBody());
		assertEquals(entity.getStatusCode(), HttpStatus.OK);	
	}	
	
	@Test
	void shoudlReturnModifiedPost() {
		//prepare
		Post post = createPost();	
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL + "posts/1");
		HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
		when(restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, new HttpEntity<Post>(post, headers), Post.class))
		.thenReturn(new ResponseEntity<Post>(new Post(), HttpStatus.OK));
		
		//execute		
		client = new TestAPIClientImpl(restTemplate);
		ResponseEntity<Post> entity = client.modifyPost(1, post);
		
		//results
		assertNotNull(entity);
		assertNotNull(entity.getBody());
		assertEquals(entity.getStatusCode(), HttpStatus.OK);	
	}
		
	
	private Post createPost() {
		Post post = new Post();
		post.setId(1);
		post.setBody("my body");
		post.setTitle("my title");
		post.setUserId(1);
		return post;
	}

}
