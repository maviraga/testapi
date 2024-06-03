package com.rindus.testapi.controller.interfaces;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rindus.testapi.model.Comment;
import com.rindus.testapi.model.Post;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RequestMapping(path = "/testapi")
@RestController
public interface TestAPIController {
		
	@ApiOperation(value ="Get all posts", notes = "Get all posts")
	@GetMapping("/posts")
	ResponseEntity<List<Post>> getAllPosts();
		
	@ApiOperation(value = "Get a post by id", notes = "Get a post by id")
	@GetMapping("/posts/{id}")
	ResponseEntity<Post> getPost(@ApiParam(value = "Post id to retrieve", required = true, defaultValue = "1")
								 @PathVariable("id") Integer id);
		
	@ApiOperation(value = "Create new post", notes = "Create new post")
	@PostMapping("/posts")
	ResponseEntity<Post> addPost(@ApiParam(value = "New post to create", required = true)
								 @RequestBody Post post);
		
	@ApiOperation(value = "Delete a post", notes = "Delete a post")
	@DeleteMapping(value = "/posts/{id}")
	ResponseEntity<HttpStatus> delelePost(@ApiParam(value = "Post id to create", required = true, defaultValue = "1")
										  @PathVariable("id") Integer id);
		
	@ApiOperation(value = "Update a post", notes = "Update a post")
	@PutMapping(value = "/posts/{id}")
	ResponseEntity<Post> updatePost(@ApiParam(value = "Post id to update", required = true, defaultValue = "1") 
		 							@PathVariable("id") Integer id, 
		 							@ApiParam(value = "Data to update", required = true)
		 							@RequestBody Post post);
	
	@ApiOperation(value = "Partial update a post", notes = "Partial update a post")
	@PatchMapping("/posts/{id}")
	ResponseEntity<Post> modifyPost(@ApiParam(value = "Post id to modify", required = true, defaultValue = "1") 
									@PathVariable("id") Integer id, 
									@ApiParam(value = "Data to modify", required = true)
									@RequestBody Post post);	
		
	@ApiOperation(value = "Get all comments for a post", notes = "Get all comments for a post")
	@GetMapping("/posts/{id}/comments")
	ResponseEntity<List<Comment>> getAllPostComment(@ApiParam(value = "Post id to retrieve the comments", required = true, defaultValue = "2")
												    @PathVariable("id") Integer id);
		
	@ApiOperation(value = "Get all comments", notes = "Get all comments")
	@GetMapping("/comments")
	ResponseEntity<List<Comment>> getComment();

}
