package com.github.toprepos.constants;

public class Constants {
	
    public static final String SUCCESS="success";
    public static final String FAILURE="failure";
    public static final String REPO_URL="https://api.github.com/orgs/{organisation}/repos";
    public static final String REPO_REPLACE="{organisation}";
    public static final String COMMIT_REPLACE="{repo}";
    public static final String COMMIT_URL="https://api.github.com/repos/{repo}/commits";
    public static final String NEXT=">; rel=\"next\"";
    public static final String LAST=">; rel=\"last\"";
    public static final String LINK="Link";
    public static final int THREAD_COUNT=50;
}
