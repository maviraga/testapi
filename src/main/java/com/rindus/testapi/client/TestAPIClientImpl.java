package com.rindus.testapi.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.rindus.testapi.client.interfaces.TestAPIClient;
import com.rindus.testapi.model.Post;

@Component
public class TestAPIClientImpl implements TestAPIClient {
	
	private static final String URL = "http://jsonplaceholder.typicode.com/";
	
	@Autowired
	private RestTemplate restTemplate;
	
	public TestAPIClientImpl(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	/**
	 * Method in charge to retrieve of all posts
	 */
	public ResponseEntity<String> getAllPosts()  {
		return restTemplate.getForEntity(URL + "posts", String.class);
	}

	/**
	 * Method in charge to retrieve the post by given id
	 */
	public ResponseEntity<Post> getPost(Integer id) {
		return restTemplate.getForEntity(URL + "posts/" + id, Post.class);
	}

	/**
	 * Method in charge to add a new post
	 */
	public ResponseEntity<String> addPost(Post post) {
		return restTemplate.postForEntity(URL + "posts", post, String.class);
	}

	/**
	 * Method for deleting an existing post
	 */
	public void deletePost(Integer id) {
		restTemplate.delete(URL + "posts/" + id, String.class);		
	}

	/**
	 * Method for updating an existing post
	 */
	public ResponseEntity<Post> updatePost(Integer id, Post post) {
		HttpHeaders headers = buildHeaders();		
		buildRequestFactory();		
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL + "posts/" + id);		
		HttpEntity<Post> requestEntity = new HttpEntity<Post>(post, headers);
		ResponseEntity<Post> response = restTemplate.exchange(builder.toUriString(), HttpMethod.PUT, requestEntity,
				Post.class);		
		return response;
	}

	/**
	 * Method for performing a partial update of a post
	 */
	public ResponseEntity<Post> modifyPost(Integer id, Post post) {
		HttpHeaders headers = buildHeaders();		
		buildRequestFactory();
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL + "posts/" + id);		
		HttpEntity<Post> requestEntity = new HttpEntity<Post>(post, headers);
		ResponseEntity<Post> response = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, requestEntity,
				Post.class);

		return response;
	}

	/**
	 * Method in charge to retrieve all comments for a post
	 */
	public ResponseEntity<String> getAllPostComment(Integer id) {
		HttpHeaders headers = buildHeaders();
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL + "posts/" + id + "/comments");
		HttpEntity<?> entity = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity,
				String.class);		
        return response;
	}

	/**
	 * Method in charge to retrieve of all posts
	 */
	public ResponseEntity<String> getAllComments() {
		return restTemplate.getForEntity(URL + "comments", String.class);
	}
	
	/**
	 * Build RequestFactory
	 */
	private void buildRequestFactory() {
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();			
		requestFactory.setConnectTimeout(5000);		
		requestFactory.setReadTimeout(5000);		
		restTemplate.setRequestFactory(requestFactory);
	}

	
	/**
	 * Build HttpHeaders
	 * @return
	 */
	private HttpHeaders buildHeaders() {
		HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
		return headers;
	}

}
