package com.rindus.testapi.model;

import javax.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class Post extends Base {
	
	Integer userId;
	
	@Size(min = 1, max = 100, message = "Title length must be between 1 and 100 characteres")
	String title;	

}
