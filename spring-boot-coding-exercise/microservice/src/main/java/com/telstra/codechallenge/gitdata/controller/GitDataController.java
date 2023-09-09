package com.telstra.codechallenge.gitdata.controller;

import com.telstra.codechallenge.gitdata.model.CustomException;
import com.telstra.codechallenge.gitdata.model.GitRepoDetails;
import com.telstra.codechallenge.gitdata.service.GitDataService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j

public class GitDataController {


    private final GitDataService gitDataService;


    @GetMapping(path = "/git-hottest-repos")
    public ResponseEntity<List<GitRepoDetails>> getHottestGitRepos(@RequestParam @Positive @Min(0) Integer count) {
        log.info("Invoking API to get hottest repos in git in last 7 days");

        try {

            return Optional.ofNullable(gitDataService.getHottestGitRepos(count))
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.badRequest().build());
        } catch (Exception e) {
            log.error("Exception invoking GIT search repositories API" + e.getMessage());
            if(e instanceof CustomException)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.emptyList());
            else
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }
}
