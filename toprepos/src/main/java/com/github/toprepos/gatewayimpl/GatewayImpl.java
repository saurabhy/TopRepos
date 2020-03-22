package com.github.toprepos.gatewayimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.toprepos.constants.Constants;
import com.github.toprepos.gateway.Gateway;
import com.github.toprepos.request.PopularRequest;
import com.github.toprepos.response.CommiterList;
import com.github.toprepos.response.CommiterResponse;
import com.github.toprepos.response.ReposApiResponse;
import com.github.toprepos.response.ReposResponse;
import com.github.toprepos.utils.UtilityService;
import com.github.toprepos.utils.UtilityServiceImpl;
/*
 * Gateway implementation layer 
 * Connects to the downstream api using rest template
 */
@Service
public class GatewayImpl implements Gateway{
	
	  private ReposApiResponse resp;
	  
	  @Autowired
	  private UtilityService utility;
	  /*
	   * this is a thread class to call apis for fetching all the repos of the organisation
	   */
	  public static class MyThreadRepo implements Runnable{
        
		private String url;
		
		private PopularRequest req;
		
		private ReposApiResponse resp;
		
		public MyThreadRepo(String url1,PopularRequest request,ReposApiResponse respo) {
			url=url1;
			req=request;
			resp=respo;
		}
		@Override
		public void run() {
			ResponseEntity<String> response=new UtilityServiceImpl().callApi(url,req);
			String json= response.getBody();
	    	ObjectMapper mapper = new ObjectMapper();
	    	try {
				List<ReposResponse> r= mapper.readValue(json, new TypeReference<List<ReposResponse>>() {});
				fillReposResponse(r,resp);
			} catch(Exception e) {
				System.out.println("Exception occured in parsing into java list");
				e.printStackTrace();
			}
			
		}
		  
	  }
	  /*
	   * This is a thread class to call apis for fetching commits in a repo
	   */
	  public static class MyThreadCommit implements Runnable{
		  
		  private String url;
			
		  private PopularRequest req;
		  
		  private CommiterList commitlist;
		  
		  public MyThreadCommit(String url1,PopularRequest request,CommiterList commit) {
				url=url1;
				req=request;
				commitlist=commit;
			}
		  
		  @Override
		  public void run() {
			    ResponseEntity<String> response=new UtilityServiceImpl().callApi(url,req);
				String json= response.getBody();
		    	ObjectMapper mapper = new ObjectMapper();
		    	try {
					List<CommiterResponse> r= mapper.readValue(json, new TypeReference<List<CommiterResponse>>() {});
					fillCommitsResponse(r,commitlist);
				} catch(Exception e) {
					System.out.println("Exception occured in parsing into java list");
					e.printStackTrace();
				}
			
		  }
		  
	  }
	
      /*
       * public method exposed to call Get repo api from service layer
       */
      public ReposApiResponse callReposApi(String org,PopularRequest req) throws Exception{
    	  System.out.println("Inside gateway layer for 1st api *****************************************************************");
          resp= new ReposApiResponse();
    	  String url=Constants.REPO_URL.replace(Constants.REPO_REPLACE, org);
    	  ResponseEntity<String> result=utility.callApi(url,req);
    	  String json= result.getBody();
    	  ObjectMapper mapper = new ObjectMapper();
    	  List<ReposResponse> r= mapper.readValue(json, new TypeReference<List<ReposResponse>>() {});
    	  resp.setResponse(r);
    	  if(result.getHeaders().containsKey(Constants.LINK)) {
    	       List<String> headerList= result.getHeaders().get(Constants.LINK);
    	       List<String> checkMore=checkNext(headerList.get(0));
    	       if(checkMore!=null) {
    	    	   Integer last=Integer.parseInt(checkMore.get(1));
    	    	   ExecutorService executor = Executors.newFixedThreadPool(last);
    	    	   for(Integer i=2;i<=last;i++) {
    	    		   String urlnew=checkMore.get(0)+i.toString();
    	    		   Runnable worker=new MyThreadRepo(urlnew,req,resp);
    	    		   executor.execute(worker);
    	    	   }
    	    	   executor.shutdown();
    	   		   executor.awaitTermination(10, TimeUnit.MINUTES);
    	       }    	       
    	  }
    	  System.out.println("End gateway layer for 1st api *******************************************************************");
    	  return resp;
      }
      
      public static void fillCommitsResponse(List<CommiterResponse> r,CommiterList commitlist) {
		synchronized(commitlist) {
			for(int i=0;i<r.size();i++) {
				commitlist.getResp().add(r.get(i));
			}
		}
		
	  }
      
	  public static void fillReposResponse(List<ReposResponse> r,ReposApiResponse resp) {
		 synchronized(resp) {
			 for(int i=0;i<r.size();i++) {
				 resp.getResponse().add(r.get(i));
			 }
		 }
		
	  }
		
      /*
       * Public method exposed to call committer api
       */
      public CommiterList callCommitterApi(String repoName,PopularRequest req,CommiterList commitlist) throws Exception {
    	  System.out.println("Inside gateway layer for 2nd api ");
    	  String url=Constants.COMMIT_URL.replace(Constants.COMMIT_REPLACE, repoName);
    	  ResponseEntity<String> result=new UtilityServiceImpl().callApi(url,req);
    	  String json= result.getBody();
    	  ObjectMapper mapper = new ObjectMapper();
    	  List<CommiterResponse> r= mapper.readValue(json, new TypeReference<List<CommiterResponse>>() {});
    	  commitlist.setResp(r);
    	  if(result.getHeaders().containsKey(Constants.LINK)) {
    		  List<String> headerList= result.getHeaders().get(Constants.LINK);
   	          List<String> checkMore=checkNext(headerList.get(0));
   	          if(checkMore!=null) {
	    	     Integer last=Integer.parseInt(checkMore.get(1));
	    	     ExecutorService executor = Executors.newFixedThreadPool(last);
	    	     for(Integer i=2;i<=last;i++) {
	    		    String urlnew=checkMore.get(0)+i.toString();
	    		    Runnable worker=new MyThreadCommit(urlnew,req,commitlist);
	    		    executor.execute(worker);
	    	     }
	    	     executor.shutdown();
	   		     executor.awaitTermination(10, TimeUnit.MINUTES);
	          }    
    	   }
    	  System.out.println("End gateway layer for 2nd api ");
    	  return commitlist;
      }
      
      /*
       * method to check whether recall is necessary or not
       * this returns a list containing base url and the last number of the page for an api
       */
      private List<String> checkNext(String header) {
    	  System.out.println("Checking for recall for link : "+ header);
    	  String[] urls=header.split(",");
    	  String s=null;
    	  for(int i=0;i<urls.length;i++) {
    		  int index=urls[i].indexOf(Constants.LAST);
    		  if(index!=-1) {
    			  int ind1=urls[i].indexOf("https");
    			  s= urls[i].substring(ind1, index);
    			  break;
    		  }
    	  }
    	  if(s!=null) {
    		  int index=s.indexOf('=');
    		  List<String> returnVal= new ArrayList<String>();
    		  returnVal.add(s.substring(0,index+1));
    		  returnVal.add(s.substring(index+1, s.length()));
    		  return returnVal;
    	  }
    	  return null;
      }
}
