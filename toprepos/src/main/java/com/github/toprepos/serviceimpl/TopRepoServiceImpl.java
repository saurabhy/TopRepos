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
/*
 * Service layer Implementation
 */
@Service
public class TopRepoServiceImpl implements TopRepoService{
	
	private PopularRepos resp;
	/*
	 * Thread class to call committers of each repo per thread
	 */
	public static class Mythreads implements Runnable{
		
		private ReposResponse respCall1;
		
		private PopularRequest req;
		
		private PopularRepos resp;
		
		public Mythreads(ReposResponse resp1,PopularRequest request,PopularRepos respo) {
			respCall1=resp1;
			req=request;
			resp=respo;
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
			fillResponse(repo,resp);
		}
		
	}
    
	@Autowired
	private Gateway gateway;
	
	@Autowired
	private UtilityService utility;
	
	/*
	 * Public method exposed to controller
	 */
	public PopularRepos getAnswer(String org,PopularRequest req) {
		System.out.println("Inside service ********************************************");
        resp= new PopularRepos();
		resp.setOrganisation(org);
		List<PopularRepo> returnList= new ArrayList<>();
		resp.setPopularRepos(returnList);
        List<ReposResponse> call1Resp= getTopRepos(org,req.getRepoCount(),req);
        try {
			callByThreads(org,req,call1Resp,resp);   //calling committers of each repo per thread
		} catch (InterruptedException e) {
			System.out.println("Interupted exception occured in calling second api");
			e.printStackTrace();
		}
        System.out.println("End service ********************************************");
        Collections.sort(resp.getPopularRepos(), new CustomComparator());
		return resp;
	}
	public static void fillResponse(PopularRepo repo,PopularRepos resp) {
		synchronized(resp) {
			resp.getPopularRepos().add(repo);
		}
		
	}
	/*
	 * Method for calling committers of each repo per thread
	 */
	private void callByThreads(String org,PopularRequest req,List<ReposResponse> call1Resp,PopularRepos resp) throws InterruptedException{
		System.out.println("Started all calls for second api *************************************************************************");
		ExecutorService executor = Executors.newFixedThreadPool(req.getRepoCount());
		for(int i=0;i<call1Resp.size();i++) {
			Runnable worker = new Mythreads(call1Resp.get(i),req,resp);
			executor.execute(worker);
		}
		executor.shutdown();
		executor.awaitTermination(2, TimeUnit.MINUTES);
		System.out.println("All the apis are called ***********************************************************************************");
	}
    /*
     * Method to get all the repos of organsiation from gateway layer
     */
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
