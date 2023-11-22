# Illia Veduta

test project for ~ 8 h of development

## Task:
This is the situation: We are a publishing company that created an app for reading news articles.

To serve data to the app we need a backend to provide RESTful APIs for the following use cases:
1) allow an editor to create/update/delete an article
2) display one article
3) list all articles for a given author
4) list all articles for a given period
5) find all articles for a specific keyword

Each API should only return the data that is really needed to fulfill the use case.

An article usually consists of the following information:
1) header
2) short description
3) text
4) publish date
5) author(s)
6) keyword(s)

Hints:
Use the technology you are most comfortable with (e.g. spring-boot).
The data does not need to be persisted after the application is shut down.
Using Kotlin would be a plus.

## Small doc: 
1) kotlin version: 1.8.0
2) h2-database was used as non-persistant db
3) OpenApi withoud Swagger was used for docs auto generation 

## TODO
1) import OpenApi to github readme
2) make unit tests better: coverage and capacity
3) finish [postman collection](src/main/resources/DEMO-VERION-OF-POSTMAN-COLLECTION.json) by [TESTING-STEPS](src/main/resources/TESTING-STEPS)
4) make security config workable without superadmin role and add some auths except of basic.
