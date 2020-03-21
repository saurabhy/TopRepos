package com.github.toprepos.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Commit {
   private Committer committer;

public Committer getCommitter() {
	return committer;
}

public void setCommitter(Committer committer) {
	this.committer = committer;
}
}
