package com.shopapotheke.githubpopularrepositories;

import com.shopapotheke.githubpopularrepositories.model.Repository;
import com.shopapotheke.githubpopularrepositories.service.AggregatingGitHubService;
import com.shopapotheke.githubpopularrepositories.service.GitHubService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AggregatingGitHubServiceTest {

    @Mock
    private GitHubService delegate;

    @Test
    public void testGetPopularRepositories() throws Exception {
        // Setup
        LocalDate sinceDate = LocalDate.parse("2022-01-01");
        int limit = 2;
        String language = "java";
        AggregatingGitHubService aggregatingGitHubService = new AggregatingGitHubService(delegate);
        List<Repository> repositories = new ArrayList<>();
        Repository repository1 = new Repository("repo1", "url1", 5, "java");
        Repository repository2 = new Repository("repo2", "url2", 10, "java");
        repositories.add(repository1);
        repositories.add(repository2);

        when(delegate.getPopularRepositories(sinceDate, limit, language)).thenReturn(repositories);

        // Test
        List<Repository> result = aggregatingGitHubService.getPopularRepositories(sinceDate, limit, language);

        // Verify
        assertEquals(2, result.size());

        Repository resultRepo1 = result.get(0);
        assertEquals("name: repo1, stars: 5, lang: java, url: url1", resultRepo1.getAggregatedString());

        Repository resultRepo2 = result.get(1);
        assertEquals("name: repo2, stars: 10, lang: java, url: url2", resultRepo2.getAggregatedString());
    }
}
