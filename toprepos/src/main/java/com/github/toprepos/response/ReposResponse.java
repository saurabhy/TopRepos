package com.github.toprepos.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/*
 * Response class for get repos api
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReposResponse implements Comparable<ReposResponse>{
	
     private String full_name;
     private int forks_count;
	public String getFull_name() {
		return full_name;
	}
	public void setFull_name(String full_name) {
		this.full_name = full_name;
	}
	public int getForks_count() {
		return forks_count;
	}
	public void setForks_count(int forks_count) {
		this.forks_count = forks_count;
	}
	
	@Override
	public int compareTo(ReposResponse o) {
		if(this.forks_count<o.forks_count) {
			return -1;
		}
		else if(this.forks_count>o.forks_count) {
			return 1;
		}
		else {
			int k=this.full_name.compareToIgnoreCase(o.full_name);
			return (k>0)?1:-1;
		}
	}
}
