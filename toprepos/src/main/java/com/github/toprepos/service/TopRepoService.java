package com.github.toprepos.service;

import com.github.toprepos.model.PopularRepos;
import com.github.toprepos.request.PopularRequest;
/*
 * Interface for service layer
 */
public interface TopRepoService {

	public abstract PopularRepos getAnswer(String org,PopularRequest req);
}
