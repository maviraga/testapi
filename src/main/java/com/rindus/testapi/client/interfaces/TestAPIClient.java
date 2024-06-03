package com.rindus.testapi.client.interfaces;

import org.springframework.http.ResponseEntity;

import com.rindus.testapi.model.Post;

public interface TestAPIClient {
	
	
	ResponseEntity<String> getAllPosts();
	
	ResponseEntity<Post> getPost(Integer id);
	
	ResponseEntity<String> addPost(Post post);
	
	void deletePost(Integer id);
	
	ResponseEntity<Post> updatePost(Integer id, Post post);
	
	ResponseEntity<Post> modifyPost(Integer id, Post post);	
	
	ResponseEntity<String> getAllPostComment(Integer id);
	
	ResponseEntity<String> getAllComments();	

}
