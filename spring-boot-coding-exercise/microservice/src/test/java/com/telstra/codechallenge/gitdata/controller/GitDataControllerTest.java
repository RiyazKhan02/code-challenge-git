package com.telstra.codechallenge.gitdata.controller;

import com.telstra.codechallenge.gitdata.model.GitRepoDetails;
import com.telstra.codechallenge.gitdata.service.GitDataService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers =  GitDataController.class)
@ExtendWith(SpringExtension.class)
public class GitDataControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    WebApplicationContext context;

    @MockBean
    private GitDataService service;

    @BeforeEach
    public void setup()
    {
        this.mockMvc= MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void testGetHottestGitRepos() throws Exception {
        List<GitRepoDetails> repoDetailsList = Lists.newArrayList(new GitRepoDetails());
        when(service.getHottestGitRepos(anyInt())).thenReturn(repoDetailsList);
        mockMvc.perform(get("/git-hottest-repos")
                  .param("count",String.valueOf(1)))
                .andExpect(status().isOk());
    }
    @Test
    void testGetHottestGitReposBadRequest() throws Exception {
        List<GitRepoDetails> repoDetailsList = Lists.newArrayList(new GitRepoDetails());
        when(service.getHottestGitRepos(anyInt())).thenReturn(repoDetailsList);
        mockMvc.perform(get("/git-hottest-repos")
                        .param("count",String.valueOf("abc")))
                .andExpect(status().is4xxClientError());
    }
}
