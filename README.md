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


USAGE
registration -> POST http://localhost:8080/auth/user
example body: {
                  "firstName": "test",
                  "surname": "test",
                  "password": "test",
                  "email": "test"
              }
              

login -> POST http://localhost:8080/auth/login
example body: {
                  "email": "test",
                  "password": "test"
              } ->
we receive token in header

example api -> GET http://localhost:8080/test
with authorizatoin token 
-> than we receive {
                       "test": "Hello World!"
                   } 