package com.shopapotheke.githubpopularrepositories.config;

import com.shopapotheke.githubpopularrepositories.model.Repository;
import com.shopapotheke.githubpopularrepositories.service.AggregatingGitHubService;
import com.shopapotheke.githubpopularrepositories.service.CachingGitHubService;
import com.shopapotheke.githubpopularrepositories.service.GitHubService;
import com.shopapotheke.githubpopularrepositories.service.GitHubServiceImpl;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class GitHubServiceConfig {
    @Bean
    public GitHubService gitHubServiceWithCache(@Qualifier("gitHubServiceImpl") GitHubService gitHubService,
                                                RedisTemplate<String, List<Repository>> redisTemplate){
        return new CachingGitHubService(gitHubService, redisTemplate);
    }

    @Bean
    public GitHubService aggregatingGitHubServiceWithCache(@Qualifier("gitHubServiceWithCache") GitHubService gitHubService){
        return new AggregatingGitHubService(gitHubService);
    }

    @Bean
    public GitHubService aggregatingGitHubServiceWithoutCache(@Qualifier("gitHubServiceImpl") GitHubService gitHubService){
        return new AggregatingGitHubService(gitHubService);
    }
 }
