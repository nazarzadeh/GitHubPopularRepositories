# GitHub Popular Repositories
This project provides a RESTful API for retrieving popular repositories from GitHub that were created after a specified date and are written in a specific programming language.

The project uses the decorator pattern to comply with the SOLID principles. The decorator pattern allows additional functionality to be added to the API while keeping the original code unchanged.

## Architecture
The project is developed using Java and Spring Boot framework. The project uses a service layer to communicate with GitHub API and fetches data.

The service layer is responsible for fetching data from GitHub API and returning a list of popular repositories. The decorator service is responsible for modifying the original service's output by adding additional metadata and returning a list of repositories.

The project is designed with a layered architecture approach to ensure separation of concerns between the presentation, business logic, and data layers.

## API
The API provides a single endpoint that returns popular repositories based on the provided query parameters. The endpoint is GET /repositories. The available query parameters are:

* sinceDate: the date after which the repositories were created (format: yyyy-MM-dd)
* language: the programming language in which the repositories are written (default: all)
* limit: the maximum number of repositories to return (default: 30)

This endpoint returns a list of popular repositories from GitHub that were created after the specified date and are written in the specified language. By default, it returns a maximum of 10 repositories. You can specify the date, limit, and language by providing the corresponding query parameters: since, limit, and language. Example request: GET /repositories?since=2021-01-01&limit=5&language=java
```
GET /repositories
```
This endpoint is similar to the previous one, but it caches the results in a Redis instance. Subsequent requests with the same parameters will return the cached results instead of calling the GitHub API again.
```
GET /repositories/with-cache
```
This endpoint is similar to the previous one, but it also aggregates the results by combining the name, stars, language, and URL of each repository into a single string. The results are also cached in a Redis instance.
```
GET /repositories/with-cache/aggregated
```
This endpoint is similar to the previous one, but it does not cache the results in a Redis instance.
```
GET /repositories/aggregated
```





The API returns a list of repositories sorted by the number of stars in descending order. Each repository includes the following information:

* name: the name of the repository
* url: the URL of the repository on GitHub
* stars: the number of stars the repository has received
* programmingLanguage: the programming language in which the repository is written
* aggregatedString: a string that includes additional metadata, such as the full name and URL of the repository
## Known issues
There have been reports of inconsistencies in the results returned by GitHub API, particularly with regards to sorting. We recommend testing the API thoroughly and monitoring the results for any unexpected behavior.
https://github.com/isaacs/github/issues/1446
## Running the project
To run the project, ensure that you have the following prerequisites installed:

* JDK 11 or higher
* Docker
* Docker Compose

To start the Redis database, run the following command:
```
./run.sh
```
To start the API, run the following command:

```
./gradlew bootRun
```
The API will be available at http://localhost:8080.

## Sample request
A sample HTTP request is provided in the static folder. You can use a tool like curl or Postman to make the request.