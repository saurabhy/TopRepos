package com.github.toprepos.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.toprepos.constants.Constants;
/*
 * Final response of api
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PopularRepos {
  

  private String organisation;
  private String msg;
  private String error;
  private List<PopularRepo> popularRepos;
  
  public PopularRepos() {
	  msg=Constants.SUCCESS;
	  error="0000";
  }
  
  public String getOrganisation() {
	return organisation;
  }
  public void setOrganisation(String organisation) {
	this.organisation = organisation;
  }
public List<PopularRepo> getPopularRepos() {
	return popularRepos;
}
public void setPopularRepos(List<PopularRepo> popularRepos) {
	this.popularRepos = popularRepos;
}
public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

}
