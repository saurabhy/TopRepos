package com.github.toprepos.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.toprepos.exception.CustomException;
import com.github.toprepos.model.PopularRepos;
import com.github.toprepos.request.PopularRequest;
import com.github.toprepos.service.TopRepoService;

@RestController
public class Controller {
	
	@Autowired
	private TopRepoService service;
    
	@RequestMapping(value="/popularRepos/{organisation}",method=RequestMethod.GET)
	public PopularRepos getPopularRepoAndCommits(@PathVariable("organisation") String org,HttpServletRequest req) {
		System.out.println("Request in controller for organisation ="+org+"and querry params "+req.getParameter("repo")+" "+req.getParameter("commit"));
		PopularRepos response= new PopularRepos();
		try {
			 PopularRequest request=new PopularRequest(Integer.parseInt(req.getParameter("repo")),Integer.parseInt(req.getParameter("commit")));
			 if(req.getHeader("Authorization")!=null) {
				 request.setAuthorization(req.getHeader("Authorization"));
			 }
			response=service.getAnswer(org, request);
		}
		catch(CustomException e) {
			System.out.println("Exception occured in calling service");
			response.setError("0002");
			response.setMsg(e.getMsg());
		}
		catch(Exception e) {
			System.out.println("Generic Exception occured inside controller");
			response.setError("0002");
			response.setMsg(e.getMessage());
		}
		System.out.println("End execution of request and returning response");
		return response;
	}
}
