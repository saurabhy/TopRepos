package com.github.toprepos.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/*
 * Model class for and individual committer
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Committers implements Comparable<Committers>{
	
    private String committerId;
    private Integer commitCount;
	public String getCommitterId() {
		return committerId;
	}
	public void setCommitterId(String committerId) {
		this.committerId = committerId;
	}
	public Integer getCommitCount() {
		return commitCount;
	}
	public void setCommitCount(Integer commitCount) {
		this.commitCount = commitCount;
	}
	@Override
	public int hashCode() {
		return committerId.hashCode();
	}
	@Override
	public boolean equals(Object ob) {
		return(((Committers) ob).getCommitterId().equalsIgnoreCase(this.committerId));
	}
	@Override
	public int compareTo(Committers o) {
		if(this.commitCount<o.commitCount)
			return -1;
		else if(this.commitCount>o.commitCount)
			return 1;
		else
		return 0;
	}
}
