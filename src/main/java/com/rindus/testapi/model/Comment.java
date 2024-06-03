package com.rindus.testapi.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data	
@EqualsAndHashCode(callSuper = false)
public class Comment extends Base {
	Integer postId;
	
	@Size(min = 1, max = 100, message = "Name length must be between 1 and 100 characteres") 
	String name;
	
	@Email(message = "Email should be valid")
	String email;	

}
