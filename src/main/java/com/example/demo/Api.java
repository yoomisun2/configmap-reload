package com.example.demo;

import java.util.List;

public class Api {
	private String uri;
	private List<String> methods;
	private List<String> roles;
	
	public Api() {
		
	}
	
	public Api(String uri, List<String> methods, List<String> roles) {
		this.uri = uri;
		this.methods = methods;
		this.roles = roles;
	}
	
	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public List<String> getMethods() {
		return methods;
	}
	public void setMethods(List<String> methods) {
		this.methods = methods;
	}
	public List<String> getRoles() {
		return roles;
	}
	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
}
