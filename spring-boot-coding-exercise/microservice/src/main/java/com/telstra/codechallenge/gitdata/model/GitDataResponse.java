package com.telstra.codechallenge.gitdata.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class GitDataResponse {
    List<GitRepoDetails> items;
}

