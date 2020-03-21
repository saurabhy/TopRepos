package com.github.toprepos.serviceimpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.toprepos.exception.CustomException;
import com.github.toprepos.gateway.Gateway;
import com.github.toprepos.gatewayimpl.GatewayImpl;
import com.github.toprepos.model.Committers;
import com.github.toprepos.model.PopularRepo;
import com.github.toprepos.model.PopularRepos;
import com.github.toprepos.request.PopularRequest;
import com.github.toprepos.response.CommiterList;
import com.github.toprepos.response.ReposApiResponse;
import com.github.toprepos.response.ReposResponse;
import com.github.toprepos.service.TopRepoService;
import com.github.toprepos.utils.CustomComparator;
import com.github.toprepos.utils.UtilityService;
import com.github.toprepos.utils.UtilityServiceImpl;

@Service
public class TopRepoServiceImpl implements TopRepoService{
	
	private static PopularRepos resp;
	
	public static class Mythreads implements Runnable{
		
		private ReposResponse respCall1;
		
		private PopularRequest req;
		
		public Mythreads(ReposResponse resp,PopularRequest request) {
			respCall1=resp;
			req=request;
		}

		@Override
		public void run() {
			PopularRepo repo= new PopularRepo();
			repo.setRepoName(respCall1.getFull_name());
			repo.setForks(respCall1.getForks_count());
			try{
				CommiterList resp2= new GatewayImpl().callCommitterApi(repo.getRepoName(),req,new CommiterList());
				System.out.println("For debugging purpose");
				List<Committers> topCommit= new UtilityServiceImpl().getTopCommitters(resp2, req.getCommitterCount());
				repo.setTopCommiters(topCommit);
			}
			catch(CustomException e) {
				System.out.println("Exception occured while making second call for repo "+ repo.getRepoName());
			}
			catch(Exception e) {
				e.printStackTrace();
				System.out.println("Exception occured while making second call for repo "+ repo.getRepoName());
			}
			fillResponse(repo);
		}
		
	}
    
	@Autowired
	private Gateway gateway;
	
	@Autowired
	private UtilityService utility;
	
	public PopularRepos getAnswer(String org,PopularRequest req) {
		System.out.println("Inside service ********************************************");
        resp= new PopularRepos();
		resp.setOrganisation(org);
		List<PopularRepo> returnList= new ArrayList<>();
		resp.setPopularRepos(returnList);
        List<ReposResponse> call1Resp= getTopRepos(org,req.getRepoCount(),req);
        try {
			callByThreads(org,req,call1Resp);
		} catch (InterruptedException e) {
			System.out.println("Interupted exception occured in calling second api");
			e.printStackTrace();
		}
       // getTopCommitters(resp,call1Resp,req.getCommitterCount(),req);
        System.out.println("End service ********************************************");
        Collections.sort(resp.getPopularRepos(), new CustomComparator());
		return resp;
	}
	public static void fillResponse(PopularRepo repo) {
		synchronized(resp) {
			resp.getPopularRepos().add(repo);
		}
		
	}
	private void callByThreads(String org,PopularRequest req,List<ReposResponse> call1Resp) throws InterruptedException{
		System.out.println("Started all calls for second api *************************************************************************");
		ExecutorService executor = Executors.newFixedThreadPool(req.getRepoCount());
		for(int i=0;i<call1Resp.size();i++) {
			Runnable worker = new Mythreads(call1Resp.get(i),req);
			executor.execute(worker);
		}
		executor.shutdown();
		executor.awaitTermination(10, TimeUnit.MINUTES);
		System.out.println("All the apis are called ***********************************************************************************");
	}

	public List<ReposResponse> getTopRepos(String org,Integer total,PopularRequest req) {
		System.out.println("Calling 1st api from service");
		List<ReposResponse> topList= null;
		try {
			ReposApiResponse listResp=gateway.callReposApi(org,req);
			topList= utility.findTopReposInList(listResp,total);
		}
		catch(CustomException e) {
			System.out.println("Exception Occured inside service 1st call hence not making next call");
			throw e;
		}
		catch (Exception e) {
			System.out.println("Generic Exception  Occured inside service 1 hence not making next call");
			throw new CustomException(e.getMessage(),"0002");
		}
		System.out.println("Call ended for 1st api");
		return topList;
	}
	
}
