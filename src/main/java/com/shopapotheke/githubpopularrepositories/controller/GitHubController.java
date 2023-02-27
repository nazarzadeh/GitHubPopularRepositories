package com.shopapotheke.githubpopularrepositories.controller;

import com.shopapotheke.githubpopularrepositories.model.Repository;
import com.shopapotheke.githubpopularrepositories.service.GitHubService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GitHubController {

    private final GitHubService gitHubServiceImpl;
    private final GitHubService gitHubServiceWithCache;
    private final GitHubService aggregatingGitHubServiceWithCache;
    private final GitHubService aggregatingGitHubServiceWithoutCache;

    public GitHubController(
            @Qualifier("gitHubServiceImpl") GitHubService gitHubServiceImpl,
            @Qualifier("gitHubServiceWithCache") GitHubService gitHubServiceWithCache,
            @Qualifier("aggregatingGitHubServiceWithCache") GitHubService aggregatingGitHubServiceWithCache,
            @Qualifier("aggregatingGitHubServiceWithoutCache") GitHubService aggregatingGitHubServiceWithoutCache
            )
    {
        this.gitHubServiceImpl = gitHubServiceImpl;
        this.gitHubServiceWithCache = gitHubServiceWithCache;
        this.aggregatingGitHubServiceWithCache = aggregatingGitHubServiceWithCache;
        this.aggregatingGitHubServiceWithoutCache = aggregatingGitHubServiceWithoutCache;
    }

    private ResponseEntity getRepositories(
            GitHubService gitHubService,
            String sinceDate,
            int limit,
            String language) {

        LocalDate since = LocalDate.parse(sinceDate);
        List<Repository> repositories;
        try {
            repositories = gitHubService.getPopularRepositories(since, limit, language);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Invalid date format or url attribute", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(repositories, HttpStatus.OK);
    }

    @GetMapping("/repositories")
    public ResponseEntity getPopularRepositories(
            @RequestParam(name = "since") String sinceDate,
            @RequestParam(name = "limit", defaultValue = "10") int limit,
            @RequestParam(name = "language", defaultValue = "all") String language) {
        return getRepositories(gitHubServiceImpl, sinceDate, limit, language);
    }

    @GetMapping("/repositories/with-cache")
    public ResponseEntity getPopularRepositoriesWithCache(
            @RequestParam(name = "since") String sinceDate,
            @RequestParam(name = "limit", defaultValue = "10") int limit,
            @RequestParam(name = "language", defaultValue = "all") String language) {
        return getRepositories(gitHubServiceWithCache, sinceDate, limit, language);
    }

    @GetMapping("/repositories/with-cache/aggregated")
    public ResponseEntity getPopularRepositoriesWithCacheAggregated(
            @RequestParam(name = "since") String sinceDate,
            @RequestParam(name = "limit", defaultValue = "10") int limit,
            @RequestParam(name = "language", defaultValue = "all") String language) {
        return getRepositories(aggregatingGitHubServiceWithCache, sinceDate, limit, language);
    }

    @GetMapping("/repositories/aggregated")
    public ResponseEntity getPopularRepositoriesAggregated(
            @RequestParam(name = "since") String sinceDate,
            @RequestParam(name = "limit", defaultValue = "10") int limit,
            @RequestParam(name = "language", defaultValue = "all") String language) {
        return getRepositories(aggregatingGitHubServiceWithoutCache, sinceDate, limit, language);
    }
}
