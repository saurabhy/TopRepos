package com.github.toprepos.gateway;

import com.github.toprepos.request.PopularRequest;
import com.github.toprepos.response.CommiterList;
import com.github.toprepos.response.ReposApiResponse;

public interface Gateway {
    
	public abstract ReposApiResponse callReposApi(String org,PopularRequest req) throws Exception;
	public abstract CommiterList callCommitterApi(String repoName,PopularRequest req,CommiterList commitlist) throws Exception;
}
