package com.rindus.testapi.model;

import javax.validation.constraints.Max;

import lombok.Data;

@Data
public class Base extends Object{
	Integer id;
	
	@Max(value = 250, message = "Body should not be more than 250 characters")
	String body;
}
