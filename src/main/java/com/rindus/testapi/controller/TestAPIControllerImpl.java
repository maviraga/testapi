package com.rindus.testapi.controller;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.rindus.testapi.controller.interfaces.TestAPIController;
import com.rindus.testapi.model.Comment;
import com.rindus.testapi.model.Post;
import com.rindus.testapi.service.ServiceException;
import com.rindus.testapi.service.interfaces.TestAPIService;

@RestController
public class TestAPIControllerImpl implements TestAPIController {
	
	Logger logger = LoggerFactory.getLogger(TestAPIControllerImpl.class);
	
	@Autowired
	TestAPIService service;

	/**
	 * Retrieving of all posts
	 */
	public ResponseEntity<List<Post>> getAllPosts() {
		logger.info("Init getAllPosts");
		ResponseEntity<List<Post>> allPosts;
		try {
			allPosts = (ResponseEntity<List<Post>>) service.getAllPosts();
		} catch (ServiceException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return allPosts;
	}

	/**
	 * Retrieving a post by given id
	 */
	public ResponseEntity<Post> getPost(Integer id) {
		logger.info("Init get posts by Id");
		ResponseEntity<Post> postsById = null;
		try {
			postsById = service.getPost(id);
		} catch (ServiceException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return postsById;
	}

	/**
	 * Adding a post
	 */
	public ResponseEntity<Post> addPost(Post post) {
		logger.info("Init add post");
		ResponseEntity<Post> responseEntity = null;
		try {
			responseEntity = service.addPost(post);
		} catch (ServiceException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return responseEntity;
	}

	/**
	 * Deleting post
	 */
	public ResponseEntity<HttpStatus> delelePost(Integer id) {
		logger.info("Init delete post");
		ResponseEntity<HttpStatus> responseEntity;
		try {
			responseEntity = service.deletePost(id);
		} catch (ServiceException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return responseEntity;
	}

	/**
	 * Updating post
	 */
	public ResponseEntity<Post> updatePost(Integer id, Post post) {
		logger.info("Init update post");
		  ResponseEntity<Post> responseEntity;
		try {
			responseEntity = service.updatePost(id, post);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	    return responseEntity;
	}

	/**
	 * Modifying post
	 */
	public ResponseEntity<Post> modifyPost(Integer id, Post post) {
		logger.info("Init modify post");
        ResponseEntity<Post> responseEntity;
		try {
			responseEntity = service.modifyPost(id, post);
		} catch (ServiceException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
        return responseEntity;
	}

	/**
	 * Retrieving all comments  for a post
	 */
	public ResponseEntity<List<Comment>> getAllPostComment(Integer id) {
		logger.info("Init get comments for a post");
        ResponseEntity<List<Comment>> responseEntity;
		try {
			responseEntity = service.getAllPostComment(id);
		} catch (ServiceException e) {
			return new ResponseEntity<>(Collections.emptyList(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
        return responseEntity;
	}

	/**
	 * Retrieving all comments
	 */
	public ResponseEntity<List<Comment>> getComment() {
		logger.info("Init get all comments");
		ResponseEntity<List<Comment>> allComments;
		try {
			allComments = (ResponseEntity<List<Comment>>) service.getAllComments();
		} catch (ServiceException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return allComments;
	}

}
