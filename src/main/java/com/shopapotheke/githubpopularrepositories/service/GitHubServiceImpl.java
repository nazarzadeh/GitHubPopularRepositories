package com.shopapotheke.githubpopularrepositories.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopapotheke.githubpopularrepositories.model.Repository;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.stereotype.Service;

@Service
public class GitHubServiceImpl implements GitHubService{
    private final String API_URL = "https://api.github.com/search/repositories";
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();


    /**
     * Returns a list of popular repositories from GitHub that were created after the specified date and are written in the
     * specified language, up to the specified limit. If the limit exceeds the number of repositories available, only the available
     * repositories will be returned. If the specified language is "all", repositories in all languages will be returned.
     *
     * @param sinceDate the date after which the repositories were created
     * @param limit the maximum number of repositories to return
     * @param language the programming language in which the repositories are written
     * @return a list of popular repositories that meet the specified criteria
     * @throws Exception if an error occurs while communicating with the GitHub API
     */
    public List<Repository> getPopularRepositories(LocalDate sinceDate, int limit, String language) throws Exception {
        int perPage = 30;
        int pages = (limit + perPage - 1) / perPage; // round up division

        List<Repository> repositories = getRepositories(sinceDate, limit, language, perPage, pages);

        return repositories.stream().limit(limit).collect(Collectors.toList());
    }

    /**
     * Returns a list of repositories that match the search criteria specified in the given JSON root, up to the specified limit.
     *
     * @param language the programming language in which the repositories are written
     * @param limit the maximum number of repositories to return
     * @return a list of repositories that match the search criteria
     */
    private List<Repository> getRepositories(LocalDate sinceDate, int limit, String language, int perPage, int pages) throws IOException, InterruptedException {
        HttpResponse<String> response;
        List<Repository> repositories = new ArrayList<>();

        for (int page = 1; page <= pages; page++) {
            // Build the API query string
            String query = URLEncoder.encode("created:>" + sinceDate.format(DateTimeFormatter.ISO_LOCAL_DATE), StandardCharsets.UTF_8);
            String languageQuery = language.equals("all") ? "" : "+language:" + language;
            String urlString = API_URL + "?q=" + query + languageQuery + "&sort=stars&order=desc&page=" + page + "&per_page=" + perPage;

            // Send the API request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlString))
                    .header("Accept", "application/vnd.github.v3+json")
                    .build();

            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 403) {
                // Rate limit exceeded - return the repositories that have been fetched so far
                return repositories;
            } else if (response.statusCode() != 200) {
                // Handle other HTTP errors
                throw new RuntimeException("HTTP error: " + response.statusCode());
            }

            // Parse the response JSON
            JsonNode root = objectMapper.readTree(response.body());
            List<Repository> pageRepositories = getRepositoriesFromJson(root);
            repositories.addAll(pageRepositories);

            // Check if we've reached the requested limit
            if (repositories.size() >= limit) {
                return repositories.subList(0, limit);
            }
        }
        return repositories;
    }

    /**
     * Returns a list of repositories from GitHub that match the search criteria, up to the specified limit.
     * @return a list of repositories that match the search criteria
     */
    private List<Repository> getRepositoriesFromJson(JsonNode root) {
        return StreamSupport.stream(root.path("items").spliterator(), false)
            .map(item -> new Repository(
                item.path("name").asText(),
                item.path("html_url").asText(),
                item.path("stargazers_count").asInt(),
                item.path("language").asText("")
            ))
            .collect(Collectors.toList());
    }
}


