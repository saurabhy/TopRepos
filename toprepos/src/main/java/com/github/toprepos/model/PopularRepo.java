package com.github.toprepos.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/*
 * Model class for each popular repo
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PopularRepo {
	
    private String repoName;
    private List<Committers> topCommiters;
    private Integer forks;
	public Integer getForks() {
		return forks;
	}
	public void setForks(Integer forks) {
		this.forks = forks;
	}
	public String getRepoName() {
		return repoName;
	}
	public void setRepoName(String repoName) {
		this.repoName = repoName;
	}
	public List<Committers> getTopCommiters() {
		return topCommiters;
	}
	public void setTopCommiters(List<Committers> topCommiters) {
		this.topCommiters = topCommiters;
	}
}
