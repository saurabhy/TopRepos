package com.github.toprepos.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReposApiResponse {
  private List<ReposResponse> response;

public List<ReposResponse> getResponse() {
	return response;
}

public void setResponse(List<ReposResponse> response) {
	this.response = response;
}
  
}
