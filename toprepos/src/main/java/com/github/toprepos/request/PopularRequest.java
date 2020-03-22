package com.github.toprepos.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/*
 * Request for getting popular repo and top committers
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PopularRequest {
    private int repoCount;
    private int committerCount;
	private String authorization; // authorization token is required as github apis have higher number of hits/hr for authenticated user
    
    public String getAuthorization() {
		return authorization;
	}
	public void setAuthorization(String authorization) {
		this.authorization = authorization;
	}
    
    public PopularRequest (int a,int b) {
    	repoCount=a;
    	committerCount=b;
    }
	public int getRepoCount() {
		return repoCount;
	}
	public void setRepoCount(int repoCount) {
		this.repoCount = repoCount;
	}
	public int getCommitterCount() {
		return committerCount;
	}
	public void setCommitterCount(int committerCount) {
		this.committerCount = committerCount;
	}
}
