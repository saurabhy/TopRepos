package com.github.toprepos.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.github.toprepos.exception.CustomException;
import com.github.toprepos.model.Committers;
import com.github.toprepos.request.PopularRequest;
import com.github.toprepos.response.CommiterList;
import com.github.toprepos.response.CommiterResponse;
import com.github.toprepos.response.ReposApiResponse;
import com.github.toprepos.response.ReposResponse;
/*
 * Implementation for utility methods
 */
@Service
public class UtilityServiceImpl implements UtilityService {
	/*
	 * method to find top total repos 
	 * uses priority queue for same
	 */
	@Override
	public List<ReposResponse> findTopReposInList(ReposApiResponse listResp,Integer total) {
		System.out.println("filtering list obtained from api response");
		List<ReposResponse> returnVal= new ArrayList<ReposResponse>();
		PriorityQueue<ReposResponse> q= new PriorityQueue<ReposResponse>();
		if(listResp.getResponse()==null) {
			throw new CustomException("Top "+total.toString()+" Repos is Empty","0002");
		}
		for(int i=0;i<listResp.getResponse().size();i++) {
			if(q.size()<total)
				q.add(listResp.getResponse().get(i));
			else {
				if(q.peek().getForks_count()<listResp.getResponse().get(i).getForks_count()) {
					q.poll();
					q.add(listResp.getResponse().get(i));
				}
			}
		}
		while(!q.isEmpty()) {
			returnVal.add(q.poll());
		}
		System.out.println("filtered list according to top forks");
		return returnVal;
	}
	/*
	 * Method to find top total committers
	 * Uses priority queue for same
	 */
	@Override
	public List<Committers> getTopCommitters(CommiterList resp2,int total) {
		List<Committers> returnVal= new ArrayList<>();
		Map<String,Integer> m= new HashMap<String,Integer>();
		List<CommiterResponse> clist=resp2.getResp();
		for(int i=0;i<clist.size();i++) {
			if(m.containsKey(clist.get(i).getCommit().getCommitter().getName())) {
				m.put(clist.get(i).getCommit().getCommitter().getName(),m.get(clist.get(i).getCommit().getCommitter().getName())+1);
			}
			else {
				m.put(clist.get(i).getCommit().getCommitter().getName(), 1);
			}
		}
		PriorityQueue<Committers> q= new PriorityQueue<Committers>();
		Set entrySet= m.entrySet();
		Iterator it= entrySet.iterator();
		while(it.hasNext()) {
			Map.Entry me = (Map.Entry)it.next();
			if(q.size()<total) {
				Committers c= new Committers();
				c.setCommitterId((String)me.getKey());
				c.setCommitCount((Integer)me.getValue());
				q.add(c);
			}
			else {
				Integer count=(Integer)me.getValue();
				if(count>q.peek().getCommitCount()) {
					q.poll();
					Committers c= new Committers();
					c.setCommitterId((String)me.getKey());
					c.setCommitCount((Integer)me.getValue());
					q.add(c);
				}
			}
		}
		while(!q.isEmpty()) {
			returnVal.add(q.poll());
		}
		return returnVal;
	}
	/*
	 * Method to make rest api calls using rest template
	 */
	 public ResponseEntity<String> callApi(String url,PopularRequest req){
   	  System.out.println("Calling api for url : "+url);
   	  RestTemplate restTemplate = new RestTemplate();   	     
   	  HttpHeaders headers = new HttpHeaders();
   	  headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
   	  if(req.getAuthorization()!=null)
   	  headers.setBearerAuth(req.getAuthorization());
   	  HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
   	  ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
   	  if(result.getStatusCodeValue()!=200) {
   		  System.out.println("Call for "+url+" statuscode "+result.getStatusCodeValue());
   		  throw new CustomException("Error returned from github api","0002");
   	  }
   	  System.out.println("Call ended for url : "+url);
   	  return result;
     }
	
}
