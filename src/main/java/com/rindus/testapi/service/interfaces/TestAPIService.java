package com.rindus.testapi.service.interfaces;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.rindus.testapi.model.Comment;
import com.rindus.testapi.model.Post;
import com.rindus.testapi.service.ServiceException;

@Service
public interface TestAPIService {
	
	ResponseEntity<List<Post>> getAllPosts() throws ServiceException;
		
	ResponseEntity<Post> getPost(Integer id) throws ServiceException;
		
	ResponseEntity<Post> addPost(Post post) throws ServiceException;
		
	ResponseEntity<HttpStatus> deletePost(Integer id) throws ServiceException;
		
	ResponseEntity<Post> updatePost(Integer id, Post post) throws ServiceException;
		
	ResponseEntity<Post> modifyPost(Integer id, Post post) throws ServiceException;	
		
	ResponseEntity<List<Comment>> getAllPostComment(Integer id) throws ServiceException;
		
	ResponseEntity<List<Comment>> getAllComments() throws ServiceException;

}
