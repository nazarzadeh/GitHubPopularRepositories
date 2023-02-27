package com.shopapotheke.githubpopularrepositories.service;

import com.shopapotheke.githubpopularrepositories.model.Repository;
import java.time.LocalDate;
import java.util.List;

public interface GitHubService {
    List<Repository> getPopularRepositories(LocalDate sinceDate, int limit, String language) throws Exception;
}
