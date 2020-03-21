package com.github.toprepos.utils;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.github.toprepos.model.Committers;
import com.github.toprepos.request.PopularRequest;
import com.github.toprepos.response.CommiterList;
import com.github.toprepos.response.ReposApiResponse;
import com.github.toprepos.response.ReposResponse;

public interface UtilityService {

	List<ReposResponse> findTopReposInList(ReposApiResponse listResp, Integer total);

	List<Committers> getTopCommitters(CommiterList resp2, int total);
	
     ResponseEntity<String> callApi(String url,PopularRequest req);

}