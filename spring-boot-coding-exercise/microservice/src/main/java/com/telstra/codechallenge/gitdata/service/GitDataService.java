package com.telstra.codechallenge.gitdata.service;

import com.telstra.codechallenge.gitdata.model.CustomException;
import com.telstra.codechallenge.gitdata.model.GitDataResponse;
import com.telstra.codechallenge.gitdata.model.GitRepoDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.Collections;

import static com.telstra.codechallenge.gitdata.constant.GitRepoConstant.ORDER;
import static com.telstra.codechallenge.gitdata.constant.GitRepoConstant.DATE_FORMAT;
import static com.telstra.codechallenge.gitdata.constant.GitRepoConstant.SORT_TYPE;
import static com.telstra.codechallenge.gitdata.constant.GitRepoConstant.GIT_HOTTEST_REPOS_URL;
import static com.telstra.codechallenge.gitdata.constant.GitRepoConstant.CURRENT_DATE;


@Service
@RequiredArgsConstructor
@Slf4j
public class GitDataService {

    @Value("${gitRepo.base.url}")
    private String gitRepoBaseUrl;

    private final RestTemplate restTemplate;


    public List<GitRepoDetails> getHottestGitRepos(int count) {
        validateInput(count);
        var params = new HashMap<>(Collections.singletonMap(CURRENT_DATE, getCurrentDate()));
        params.put(SORT_TYPE,"stars");
        params.put(ORDER,"desc");
        List<GitRepoDetails> response = new ArrayList<GitRepoDetails>();
        var startTime =System.currentTimeMillis();
        log.info("Invoking GIT search repositories API");
        var gitDataResponse =
                restTemplate.getForObject(gitRepoBaseUrl + GIT_HOTTEST_REPOS_URL, GitDataResponse.class, params);
        log.debug("Time Taken : GIT search repositories API - executed in {} ms",System.currentTimeMillis() - startTime);
        return Optional.ofNullable(gitDataResponse).map(GitDataResponse::getItems).stream().flatMap(List::stream).limit(count).toList();

    }
    private void validateInput(int count)
    {
        if(count < 0)
            throw new CustomException("Number of records cannot be less than 0",HttpStatus.BAD_REQUEST);
    }
    private String getCurrentDate(){
        var currentDate = LocalDate.now();
        var dateFormat = DateTimeFormatter.ofPattern(DATE_FORMAT);
        return currentDate.format(dateFormat);

    }
}
