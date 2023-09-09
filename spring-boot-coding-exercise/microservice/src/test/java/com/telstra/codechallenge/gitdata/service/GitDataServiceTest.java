package com.telstra.codechallenge.gitdata.service;

import com.telstra.codechallenge.gitdata.exception.RestTemplateErrorHandler;
import com.telstra.codechallenge.gitdata.model.CustomException;
import com.telstra.codechallenge.gitdata.model.GitDataResponse;
import com.telstra.codechallenge.gitdata.model.GitRepoDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


import static com.telstra.codechallenge.gitdata.constant.GitRepoConstant.DATE_FORMAT;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;


@ExtendWith(SpringExtension.class)
 @TestPropertySource(properties = {
        "gitRepo.base.url = https://api.github.com"
})
@RestClientTest
public class GitDataServiceTest {

    @InjectMocks
    private GitDataService gitDataService;

    @Autowired
    private RestTemplate restTemplate;


    @Autowired
    private MockRestServiceServer mockRestServiceServer;


    @Test
    void testGetHottestGitRepos() throws Exception {
        ReflectionTestUtils.setField(gitDataService,"restTemplate",restTemplate);
        ReflectionTestUtils.setField(gitDataService,"gitRepoBaseUrl","https://api.github.com");
        restTemplate.setErrorHandler(new RestTemplateErrorHandler());
        mockRestServiceServer = MockRestServiceServer.createServer(restTemplate);
        Resource response = new ClassPathResource("json/sample.json",this.getClass());
        this.mockRestServiceServer.expect(ExpectedCount.once(),requestTo("https://api.github.com/search/repositories?q="+getCurrentDate()+"&sort=stars&order=desc"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK));
        assertNotNull(gitDataService.getHottestGitRepos(2));

    }
    @Test
    void testGetHottestGitReposFailure() throws Exception {
        ReflectionTestUtils.setField(gitDataService,"restTemplate",restTemplate);
        ReflectionTestUtils.setField(gitDataService,"gitRepoBaseUrl","https://api.github.com");
        restTemplate.setErrorHandler(new RestTemplateErrorHandler());
        mockRestServiceServer = MockRestServiceServer.createServer(restTemplate);
        this.mockRestServiceServer.expect(ExpectedCount.once(),requestTo("https://api.github.com/search/repositories?q="+getCurrentDate()+"&sort=stars&order=desc"))
                        .andExpect(method(HttpMethod.GET))
                                .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));
        assertThrows(CustomException.class,()->gitDataService.getHottestGitRepos(1));

    }

private GitDataResponse createGitDataResponse(){
    var response = new GitDataResponse();
    var detailsList= new ArrayList<GitRepoDetails>();
    var details = new GitRepoDetails();

    details.setId("22");
    details.setWatchers_count(100);

    detailsList.add(details);
    response.setItems(detailsList);
    return response;
}
    private String getCurrentDate(){
        var currentDate = LocalDate.now();
        var dateFormat = DateTimeFormatter.ofPattern(DATE_FORMAT);
        return currentDate.format(dateFormat);

    }

}