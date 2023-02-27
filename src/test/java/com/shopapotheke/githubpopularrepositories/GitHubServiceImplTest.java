package com.shopapotheke.githubpopularrepositories;

import com.shopapotheke.githubpopularrepositories.model.Repository;
import com.shopapotheke.githubpopularrepositories.service.GitHubServiceImpl;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GitHubServiceImplTest {

    @Test
    public void testGetPopularRepositories() throws Exception {
        GitHubServiceImpl gitHubService = new GitHubServiceImpl();
        LocalDate sinceDate = LocalDate.of(2022, 1, 1);
        int limit = 100;
        String language = "java";

        List<Repository> repositories = gitHubService.getPopularRepositories(sinceDate, limit, language);

        assertNotNull(repositories);
        assertEquals(limit, repositories.size());

        for (Repository repository : repositories) {
            assertNotNull(repository.getName());
            assertNotNull(repository.getUrl());
            assertTrue(repository.getStars() >= 0);
            assertNotNull(repository.getProgrammingLanguage());
        }
    }
}
