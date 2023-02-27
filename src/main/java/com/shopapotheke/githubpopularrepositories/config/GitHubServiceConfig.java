package com.shopapotheke.githubpopularrepositories.config;

import com.shopapotheke.githubpopularrepositories.model.Repository;
import com.shopapotheke.githubpopularrepositories.service.AggregatingGitHubService;
import com.shopapotheke.githubpopularrepositories.service.CachingGitHubService;
import com.shopapotheke.githubpopularrepositories.service.GitHubService;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class GitHubServiceConfig {

    /**
     * Configures and returns an instance of the {@link CachingGitHubService}.
     *
     * @param gitHubService the {@link GitHubService} to be wrapped with caching functionality
     * @param redisTemplate the {@link RedisTemplate} used to cache repositories
     * @return an instance of {@link CachingGitHubService}
     */
    @Bean
    public GitHubService gitHubServiceWithCache(@Qualifier("gitHubServiceImpl") GitHubService gitHubService,
                                                RedisTemplate<String, List<Repository>> redisTemplate){
        return new CachingGitHubService(gitHubService, redisTemplate);
    }

    /**
     * Configures and returns an instance of the {@link AggregatingGitHubService} that wraps the {@link GitHubService} with caching functionality.
     *
     * @param gitHubService the {@link GitHubService} to be wrapped with caching and aggregating functionality
     * @return an instance of {@link AggregatingGitHubService} that wraps the {@link GitHubService} with caching functionality
     */
    @Bean
    public GitHubService aggregatingGitHubServiceWithCache(@Qualifier("gitHubServiceWithCache") GitHubService gitHubService){
        return new AggregatingGitHubService(gitHubService);
    }

    /**
     * Configures and returns an instance of the {@link AggregatingGitHubService} that does not have caching functionality.
     *
     * @param gitHubService the {@link GitHubService} to be wrapped with aggregating functionality
     * @return an instance of {@link AggregatingGitHubService} that does not have caching functionality
     */
    @Bean
    public GitHubService aggregatingGitHubServiceWithoutCache(@Qualifier("gitHubServiceImpl") GitHubService gitHubService){
        return new AggregatingGitHubService(gitHubService);
    }
 }
