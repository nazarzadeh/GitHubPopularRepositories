package com.shopapotheke.githubpopularrepositories.service;

import com.shopapotheke.githubpopularrepositories.model.Repository;
import java.time.LocalDate;
import java.util.List;
/**
 * This is a decorator service for {@link GitHubService} that aggregates additional
 * information for each repository returned by the underlying service. The aggregated
 * information is stored in the {@link Repository} objects returned by the
 * {@link #getPopularRepositories(LocalDate, int, String)} method.
 */
public class AggregatingGitHubService implements GitHubService {
    private final GitHubService delegate;
    /**
     * Constructs a new {@link AggregatingGitHubService} that delegates to the specified
     * {@link GitHubService} for retrieving repository information.
     *
     * @param delegate the {@link GitHubService} to delegate to
     */
    public AggregatingGitHubService(GitHubService delegate) {
        this.delegate = delegate;
    }
    /**
     * Retrieves a list of popular repositories from the underlying {@link GitHubService},
     * and augments each repository object with additional aggregated information in the
     * form of a string.
     *
     * @param sinceDate the date after which the repositories were created
     * @param limit the maximum number of repositories to return
     * @param language the programming language in which the repositories are written
     * @return a list of popular repositories that meet the specified criteria, with additional
     *         aggregated information stored in each repository object
     * @throws Exception if an error occurs while communicating with the GitHub API
     */
    @Override
    public List<Repository> getPopularRepositories(LocalDate sinceDate, int limit, String language) throws Exception {
        List<Repository> repositories = delegate.getPopularRepositories(sinceDate, limit, language);

        for (Repository repository : repositories) {
            String languageStr = repository.getProgrammingLanguage() != null ? repository.getProgrammingLanguage() : "";
            String fullName = repository.getName() != null ? repository.getName() : "";
            String url = repository.getUrl() != null ? repository.getUrl() : "";
            String star = Integer.toString(repository.getStars());

            String aggregatedString = String.format("name: %s, stars: %s, lang: %s, url: %s", fullName, star, languageStr, url);
            repository.setAggregatedString(aggregatedString);
        }

        return repositories;
    }
}
