# TopRepos
This project will have api which will find the top m repos and top n committers of these respective repos for a given organisation

What are Project Requirements ?

JAVA 1.8

Spring boot 2.2.5

gradle 6.1.1

How to Call Api ?

The api by default is hosted on port 9090 . This can be changed in application.properties in src/main/resources folder

api url: http://localhost:9090/popularRepos/{organisation}?repo=5&commit=5

Example url: http://localhost:9090/popularRepos/google?repo=5&commit=5

Use this once the api is hosted on localhost:9090

{organisation} : replace this with organisation name . This is path variable in url

Two querry parameters needs to be passed :

repo: defines the number of top repos required according to the number of forks

commit : defines the number of top committers for each repo

Headers required for the api:

Github policy for number of api hits: 60 hits/hour for non authenticated users and 5000 hits/hour for authenticated users. 

Hence we need to pass authorization header for calling this api. To generate one's github token please refer to resource: https://docs.cachethq.io/docs/github-oauth-token

Authorization : token

Why do we require so many hits ?

Github has paging concepts in its apis as the reponse for the number of repos and the number of committers in each repo is too large. Please look into documentation 

resource : https://developer.github.com/v3/repos/commits/

We get Link in response header as : Status: 200 OK

Link: <https://api.github.com/resource?page=2>; rel="next",<https://api.github.com/resource?page=5>; rel="last"

These pages needs to parsed to get the entire lists, hence we will be making a lot of calls to github apis .Hence we need an authenticated user for same.

What is the Response structure of the api?

Sample response:

Success: 

{
    "organisation": "google",
    "msg": "success",
    "error": "0000",
    "popularRepos": [
        {
            "repoName": "google/googletest",
            "topCommiters": [
                {
                    "committerId": "Billy Donahue",
                    "commitCount": 132
                },
                {
                    "committerId": "vladlosev",
                    "commitCount": 141
                },
                {
                    "committerId": "zhanyong.wan",
                    "commitCount": 384
                },
                {
                    "committerId": "Gennadiy Civil",
                    "commitCount": 696
                },
                {
                    "committerId": "GitHub",
                    "commitCount": 836
                }
            ],
            "forks": 5893
        },
        {
            "repoName": "google/iosched",
            "topCommiters": [
                {
                    "committerId": "Shailen Tuli",
                    "commitCount": 109
                },
                {
                    "committerId": "Jonathan Koren",
                    "commitCount": 194
                },
                {
                    "committerId": "Nick Butcher",
                    "commitCount": 203
                },
                {
                    "committerId": "Takeshi Hagikura",
                    "commitCount": 424
                },
                {
                    "committerId": "Gerrit Code Review",
                    "commitCount": 714
                }
            ],
            "forks": 6049
        },
        {
            "repoName": "google/guava",
            "topCommiters": [
                {
                    "committerId": "Charles Fry",
                    "commitCount": 283
                },
                {
                    "committerId": "Kurt Kluever",
                    "commitCount": 324
                },
                {
                    "committerId": "guava.mirrorbot@gmail.com",
                    "commitCount": 414
                },
                {
                    "committerId": "Colin Decker",
                    "commitCount": 706
                },
                {
                    "committerId": "Chris Povirk",
                    "commitCount": 2753
                }
            ],
            "forks": 8183
        },
        {
            "repoName": "google/material-design-icons",
            "topCommiters": [
                {
                    "committerId": "Brenton Simpson",
                    "commitCount": 5
                },
                {
                    "committerId": "GitHub",
                    "commitCount": 9
                },
                {
                    "committerId": "Addy Osmani",
                    "commitCount": 12
                },
                {
                    "committerId": "Josh Estelle",
                    "commitCount": 37
                },
                {
                    "committerId": "Scott Hyndman",
                    "commitCount": 42
                }
            ],
            "forks": 8198
        },
        {
            "repoName": "google/styleguide",
            "topCommiters": [
                {
                    "committerId": "Stephen Hicks",
                    "commitCount": 13
                },
                {
                    "committerId": "Isaac Good",
                    "commitCount": 13
                },
                {
                    "committerId": "mmentovai",
                    "commitCount": 22
                },
                {
                    "committerId": "erg@google.com",
                    "commitCount": 37
                },
                {
                    "committerId": "GitHub",
                    "commitCount": 110
                }
            ],
            "forks": 8612
        }
    ]
}

Failure: By hitting http://localhost:9090/popularRepos/xyz?repo=5&commit=5 

{
    "organisation": null,
    "msg": "404 Not Found: [{\"message\":\"Not Found\",\"documentation_url\":\"https://developer.github.com/v3/repos/#list-organization-repositories\"}]",
    "error": "0002",
    "popularRepos": null
}










