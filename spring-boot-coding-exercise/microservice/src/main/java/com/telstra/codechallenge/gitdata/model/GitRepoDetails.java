package com.telstra.codechallenge.gitdata.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class GitRepoDetails {

    private String id;
    private Integer watchers_count;
    private String language;
    private String description;
    private String name;
}
