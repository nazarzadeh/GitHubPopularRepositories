package com.shopapotheke.githubpopularrepositories.service;

import com.shopapotheke.githubpopularrepositories.model.Repository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;

public class CachingGitHubService implements GitHubService {
    private final GitHubService delegate;
    private final RedisTemplate<String, List<Repository>> redisTemplate;

    /**
     * Constructs a new CachingGitHubService with the specified delegate and RedisTemplate.
     *
     * @param delegate the delegate GitHubService to use for fetching data
     * @param redisTemplate the RedisTemplate to use for storing and retrieving cached data
     */
    public CachingGitHubService(@Qualifier("gitHubServiceImpl") GitHubService delegate, RedisTemplate<String, List<Repository>> redisTemplate) {
        this.delegate = delegate;
        this.redisTemplate = redisTemplate;
    }

    /**
     * Returns a list of popular repositories from GitHub that were created after the specified date and are written in the
     * specified language, up to the specified limit. If the limit exceeds the number of repositories available, only the available
     * repositories will be returned. If the specified language is "all", repositories in all languages will be returned.
     *
     * If the requested data is found in the cache, it will be returned immediately. Otherwise, the delegate GitHubService will be
     * used to fetch the data, which will then be cached before being returned.
     *
     * @param sinceDate the date after which the repositories were created
     * @param limit the maximum number of repositories to return
     * @param language the programming language in which the repositories are written
     * @return a list of popular repositories that meet the specified criteria
     * @throws Exception if an error occurs while communicating with the GitHub API
     */
    @Override
    public List<Repository> getPopularRepositories(LocalDate sinceDate, int limit, String language) throws Exception {
        String cacheKey = sinceDate.toString() + language + limit;
        List<Repository> cachedValue = redisTemplate.opsForValue().get(cacheKey);
        if (cachedValue != null) {
            return cachedValue;
        } else {
            List<Repository> repositories = delegate.getPopularRepositories(sinceDate, limit, language);
            redisTemplate.opsForValue().set(cacheKey, repositories);
            return repositories;
        }
    }
}
