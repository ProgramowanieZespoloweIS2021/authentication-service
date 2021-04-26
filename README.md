# java-repository-template

### How to finish setting up java repository

* Register repository to [codecov.io](https://app.codecov.io/). *Remember to add proper secret into your repository configuration*
* Create badge for code coverage. Copy from:`https://app.codecov.io/gh/<user>/<repository>>/settings/badge`
* Create badge for CI action 
* Template is prepared for Maven based project. Add JaCoCo to your project to properly generate coverage report.
* Fill proper username/app_name in `.github/workflows/release.yml` and add `DOCKERHUB_USERNAME` and `DOCKERHUB_TOKEN` secrets
```
![CI/CD](https://github.com/<user>/<repository>/actions/workflows/ci.yml/badge.svg)
```


### USAGE
* registration 
  
POST http://localhost:8080/auth/user

Example body: 
```
{
    "firstName": "test",
    "surname": "test",
    "password": "test",
    "email": "test"
}
```

* login 
  
POST http://localhost:8080/auth/login

Example body: 
```
{
    "email": "test",
    "password": "test"
}
```
Token is sent back in the `Authorization` header.

* example api 
  
GET http://localhost:8080/test

If we have valid token in `Authorization` header we receive:
```
{
    "test": "Hello World!"
} 
```

* getting logged user info

GET http://localhost:8080/auth/user

If we have valid token in `Authorization` header we receive example response:
```
{
    "id": 2,
    "email": "john",
    "password": "$2a$10$qnU6tdUWyJKjm4ENs0e56OGj04avyHIwPJrORk7noBxIxxJiyhFZK",
    "firstName": "john",
    "surname": "snow"
}
```

* logout

GET http://localhost:8080/auth/logout

Token must be sent in the `Authorization` header. Then it will be invalidated.
