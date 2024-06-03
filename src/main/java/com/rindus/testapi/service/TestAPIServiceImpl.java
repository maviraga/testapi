package com.rindus.testapi.service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.rindus.testapi.client.interfaces.TestAPIClient;
import com.rindus.testapi.model.Comment;
import com.rindus.testapi.model.Post;
import com.rindus.testapi.service.interfaces.TestAPIService;

@Service
public class TestAPIServiceImpl implements TestAPIService {
	
	@Autowired
	ObjectMapper mapper;
	
	@Autowired
	TestAPIClient client;	
	
	Logger logger = LoggerFactory.getLogger(TestAPIServiceImpl.class);
	
	private static final String POSTS_JSON_FILE_NAME = "posts.json";
	
	private static final String POSTS_XML_FILE_NAME = "posts.xml";
	
	private static final String COMMENTS_JSON_FILE_NAME = "comments.json";
	
	private static final String COMMENTS_XML_FILE_NAME = "comments.xml";
	
	private static final String POST_TAG_NAME = "post";
	
	private static final String COMMENT_TAG_NAME = "comment";
	
	/**
	 * Method in charge to retrieve of all posts
	 */
	public ResponseEntity<List<Post>> getAllPosts() throws ServiceException {
		List<Post> listAllPosts = null;	
		try {
			ResponseEntity<String> allPosts = client.getAllPosts();	
				
			listAllPosts = mapper.readValue(allPosts.getBody(), ArrayList.class);
			
			createJsonFile(listAllPosts, POSTS_JSON_FILE_NAME);
			createXMLFile(listAllPosts, POSTS_XML_FILE_NAME, POST_TAG_NAME);			
		} catch (Exception e) { 
			logger.error("[getAllPosts] - Error retrieving all posts [" + e.getMessage() + "]");
			throw new ServiceException();
		}		
		return ResponseEntity.ok(listAllPosts);
	}
	

	/**
	 * Method in charge to retrieve the post by given id
	 */
	public ResponseEntity<Post> getPost(Integer id) throws ServiceException {
		ResponseEntity<Post> postById = null;
		try {
			postById = client.getPost(id);			
		} catch (Exception e) {
			logger.warn("[getPost] - Post not found");			
		}		
		if (postById != null) {
			return ResponseEntity.ok(postById.getBody());			
		} else {
			logger.warn("[getPost] - Post not found");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}	
	}

	/**
	 * Method in charge to add a new post
	 */
	public ResponseEntity<Post> addPost(Post post) throws ServiceException {		
		ResponseEntity<Post> postsById = this.getPost(post.getId());		
		if (postsById.getBody() == null) {
			ResponseEntity<String> entity = client.addPost(post);			
			Post newPost;
			try {
				newPost = mapper.readValue(entity.getBody(), Post.class);				
			} catch (Exception e) {
				logger.error("[addPost] - " + e.getMessage());
				throw new ServiceException();
			}
			return new ResponseEntity<Post>(newPost, HttpStatus.CREATED);
		} else {
			logger.warn("[addPost] - Post already exist");
			return postsById;
		}
	}

	/**
	 * Method for deleting an existing post
	 */
	public ResponseEntity<HttpStatus> deletePost(Integer id) throws ServiceException {
		ResponseEntity<Post> postsById = this.getPost(id);
		if (postsById != null && postsById.getBody() != null) {
			client.deletePost(id);
			return new ResponseEntity<HttpStatus>(HttpStatus.OK);
		} else {
			logger.warn("[deletePost] - Post not exit");
			//throw new ServiceException();
			return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);
		}		
	}

	/**
	 * Method for updating an existing post
	 */
	public ResponseEntity<Post> updatePost(Integer id, Post post) throws ServiceException {
		try {
			ResponseEntity<Post> postsById = this.getPost(id);	
			if (postsById != null && postsById.getBody() != null) {
				ResponseEntity<Post> updatePost = client.updatePost(id, post);		  
				return ResponseEntity.ok(updatePost.getBody());
			} else {
				logger.warn("[updatePost] - Post not exit");
				return ResponseEntity.ok(null);
			}
		} catch (Exception e) {
			logger.error("[updatePost] - Post not exit");
			throw new ServiceException();
		}
	}

	/**
	 * Method for performing a partial update of a post
	 */
	public ResponseEntity<Post> modifyPost(Integer id, Post post) throws ServiceException {
		ResponseEntity<Post> postsById = this.getPost(id);
		if (postsById != null && postsById.getBody().getId() != null) {
			ResponseEntity<Post> modifyPost = client.modifyPost(id, post);
			return ResponseEntity.ok(modifyPost.getBody());
		} else {
			logger.warn("[modifyPost] - Post not exit");			
			throw new ServiceException("[modifyPost] - Post not exit");
		}
	}

	/**
	 * Method in charge to retrieve all comments for a post
	 */
	public ResponseEntity<List<Comment>> getAllPostComment(Integer id) throws ServiceException {
		HttpEntity<String> commentsById = client.getAllPostComment(id);
		List<Comment> commentList = null;
		try {
			commentList = mapper.readValue(commentsById.getBody(), ArrayList.class);
		} catch (Exception e) {
			logger.error("[getAllPostComment] - " + e.getMessage());
			throw new ServiceException();
		}
		return ResponseEntity.ok(commentList);
	}

	/**
	 * Method in charge to retrieve of all posts
	 */
	public ResponseEntity<List<Comment>> getAllComments() throws ServiceException {
		ResponseEntity<String> allComments = client.getAllComments();		
		List<Comment> listAllComments = null;
		try {
			listAllComments = mapper.readValue(allComments.getBody(), ArrayList.class);
			createJsonFile(listAllComments, COMMENTS_JSON_FILE_NAME);
			createXMLFile(listAllComments, COMMENTS_XML_FILE_NAME, COMMENT_TAG_NAME);			
		} catch (Exception e) { 
			logger.error("[getAllComments] - Error retrieving all comments [" + e.getMessage() + "]");
			throw new ServiceException();
		}		
		return ResponseEntity.ok(listAllComments);
	}
	
	/**
	 * Create a json file from a list of elements
	 * @param list Element list
	 * @throws JsonIOException 
	 * @throws IOException
	 */
	private void createJsonFile(List<?> list, String fileName) throws JsonIOException, IOException {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		gson.toJson(list, new FileWriter(fileName));	
	}
	
	/**
	 * Create a XML file from a list of elements
	 * @param list
	 * @throws IOException
	 */
	private void createXMLFile(List<?> list, String fileName, String tagName) throws IOException {
		if (list != null) {				
			JSONArray  jsonObject = new JSONArray(list);			
			String xmlString = XML.toString(jsonObject, tagName);		
			FileWriter myWriter = new FileWriter(fileName);
		    myWriter.write(xmlString);
		    myWriter.close();			 
		}
	}
	
}
