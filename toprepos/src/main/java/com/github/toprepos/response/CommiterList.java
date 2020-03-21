package com.github.toprepos.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CommiterList {
   private List<CommiterResponse> resp;

public List<CommiterResponse> getResp() {
	return resp;
}

public void setResp(List<CommiterResponse> resp) {
	this.resp = resp;
}
}
