package com.shopapotheke.githubpopularrepositories;

import com.shopapotheke.githubpopularrepositories.model.Repository;
import com.shopapotheke.githubpopularrepositories.service.CachingGitHubService;
import com.shopapotheke.githubpopularrepositories.service.GitHubService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CachingGitHubServiceTest {

    @Mock
    private GitHubService delegate;

    @Mock
    private RedisTemplate<String, List<Repository>> redisTemplate;

    @Test
    public void getPopularRepositories_returnsCachedValue() throws Exception {
        // Arrange
        LocalDate sinceDate = LocalDate.now();
        int limit = 10;
        String language = "java";
        List<Repository> repositories = new ArrayList<>();
        repositories.add(new Repository("repo1", "url1", 5, "java"));
        repositories.add(new Repository("repo2", "url2", 3, "java"));
        String cacheKey = sinceDate + language + limit;

        // mock the ValueOperations object
        ValueOperations<String, List<Repository>> valueOps = mock(ValueOperations.class);
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        // Mock the RedisTemplate to return a cached value
        when(redisTemplate.opsForValue().get(cacheKey)).thenReturn(repositories);

        // Create a CachingGitHubService instance
        CachingGitHubService cachingService = new CachingGitHubService(delegate, redisTemplate);

        // Act
        List<Repository> result = cachingService.getPopularRepositories(sinceDate, limit, language);

        // Assert
        assertEquals(repositories, result);

        // Verify that the RedisTemplate was called but the delegate was not
        verify(redisTemplate.opsForValue()).get(cacheKey);
        verify(delegate, never()).getPopularRepositories(sinceDate, limit, language);
    }

    @Test
    public void getPopularRepositories_callsDelegateAndCachesValue() throws Exception {
        // Arrange
        LocalDate sinceDate = LocalDate.now();
        int limit = 10;
        String language = "java";
        List<Repository> repositories = new ArrayList<>();
        repositories.add(new Repository("repo1", "url1", 5, "java"));
        repositories.add(new Repository("repo2", "url2", 3, "java"));
        String cacheKey = sinceDate + language + limit;
        // mock the ValueOperations object
        ValueOperations<String, List<Repository>> valueOps = mock(ValueOperations.class);
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        // Mock the delegate to return the repositories
        when(delegate.getPopularRepositories(sinceDate, limit, language)).thenReturn(repositories);

        // Create a CachingGitHubService instance
        CachingGitHubService cachingService = new CachingGitHubService(delegate, redisTemplate);

        // Act
        List<Repository> result = cachingService.getPopularRepositories(sinceDate, limit, language);

        // Assert
        assertEquals(repositories, result);

        // Verify that the RedisTemplate and the delegate were called
        verify(redisTemplate.opsForValue()).set(cacheKey, repositories);
        verify(redisTemplate.opsForValue()).get(cacheKey);
        verify(delegate).getPopularRepositories(sinceDate, limit, language);
    }
}
