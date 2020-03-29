package lt.bit.post;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import lt.bit.post.dto.AuthenticationRequestDTO;
import lt.bit.post.dto.AuthenticationResponseDTO;
import lt.bit.post.dto.PostDTO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author rimid
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class BlogApplicationTest {

    @LocalServerPort
    int randomServerPort;

    @Value("${testing.testUserLogin}")
    private String testUserLogin;

    @Value("${testing.testUserPassword}")
    private String testUserPassword;

    @Test
    public void getAllPostsTest() throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        String baseUrl = "http://localhost:" + randomServerPort + "/v1/posts/";
        URI uri = new URI(baseUrl);

        String token = getToken();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);

        HttpEntity requestEntity = new HttpEntity(null, headers);
        ResponseEntity<List<PostDTO>> result = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<List<PostDTO>>() {
        });
        Assert.assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    public void getPostTest() throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        String baseUrl = "http://localhost:" + randomServerPort + "/v1/posts/";
        String url = baseUrl;
        URI uri = new URI(baseUrl);

        String token = getToken();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);

        String postText = "TestForOnePost";
        String postTitle = "TestTitleOnePost";
        PostDTO request = new PostDTO(postTitle, postText);

        HttpEntity<PostDTO> requestEntity = new HttpEntity(request, headers);
        ResponseEntity<PostDTO> result = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, PostDTO.class);

        Integer putId = result.getBody().getId();
        url = url + putId + "/";
        uri = new URI(url);

        HttpEntity<PostDTO> requestEntityPut = new HttpEntity(null, headers);
        ResponseEntity<PostDTO> resultGetPost = restTemplate.exchange(uri, HttpMethod.GET, requestEntityPut, PostDTO.class);
        Assert.assertEquals(200, resultGetPost.getStatusCodeValue());
        Assert.assertEquals(postText, resultGetPost.getBody().getText());
        Assert.assertEquals(postTitle, resultGetPost.getBody().getTitle());
    }

    @Test
    public void addPostTest() throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        String baseUrl = "http://localhost:" + randomServerPort + "/v1/posts/";
        URI uri = new URI(baseUrl);

        String token = getToken();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);

        String postText = "Test";
        String postTitle = "TestTitle";
        PostDTO request = new PostDTO(postTitle, postText);

        HttpEntity<PostDTO> requestEntity = new HttpEntity(request, headers);
        ResponseEntity<PostDTO> result = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, PostDTO.class);
        Assert.assertEquals(200, result.getStatusCodeValue());
        Assert.assertEquals(postText, result.getBody().getText());
        Assert.assertEquals(postTitle, result.getBody().getTitle());
    }

    @Test
    public void updatePostTest() throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        String baseUrl = "http://localhost:" + randomServerPort + "/v1/posts/";
        String url = baseUrl;
        URI uri = new URI(url);

        String token = getToken();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);

        String postText = "TestForChnage";
        String postTitle = "TestTitleChange";
        PostDTO request = new PostDTO(postTitle, postText);

        HttpEntity<PostDTO> requestEntity = new HttpEntity(request, headers);
        ResponseEntity<PostDTO> result = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, PostDTO.class);

        String putText = "Changed";
        String putTitle = "ChangedTitle";
        Integer putId = result.getBody().getId();
        url = url + putId + "/";
        uri = new URI(url);
        PostDTO requestPut = new PostDTO(putId, putTitle, putText);

        HttpEntity<PostDTO> requestEntityPut = new HttpEntity(requestPut, headers);
        ResponseEntity<PostDTO> resultPut = restTemplate.exchange(uri, HttpMethod.PUT, requestEntityPut, PostDTO.class);
        Assert.assertEquals(200, resultPut.getStatusCodeValue());
        Assert.assertEquals(putText, resultPut.getBody().getText());
        Assert.assertEquals(putTitle, resultPut.getBody().getTitle());
    }

    @Test
    public void deletePostTest() throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        String baseUrl = "http://localhost:" + randomServerPort + "/v1/posts/";
        String url = baseUrl;
        URI uri = new URI(url);

        String token = getToken();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);

        String postText = "TestForDelete";
        String postTitle = "TestTitleDelete";
        PostDTO request = new PostDTO(postTitle, postText);

        HttpEntity<PostDTO> requestEntity = new HttpEntity(request, headers);
        ResponseEntity<PostDTO> result = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, PostDTO.class);

        Integer deleteId = result.getBody().getId();
        url = url + deleteId + "/";
        uri = new URI(url);
        HttpEntity<PostDTO> requestEntityDelete = new HttpEntity(deleteId, headers);
        ResponseEntity<PostDTO> resultDelete = restTemplate.exchange(uri, HttpMethod.DELETE, requestEntityDelete, PostDTO.class);
        Assert.assertEquals(200, resultDelete.getStatusCodeValue());
    }

    private String getToken() throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        final String loginUrl = "http://localhost:" + randomServerPort + "/v1/auth/login";
        URI uri = new URI(loginUrl);

        AuthenticationRequestDTO request = new AuthenticationRequestDTO(testUserLogin, testUserPassword);
        HttpEntity<AuthenticationRequestDTO> requestEntity = new HttpEntity<>(request, new HttpHeaders());

        ResponseEntity<AuthenticationResponseDTO> result = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, AuthenticationResponseDTO.class);
        String authToken = "Bearer " + result.getBody().getJwtToken();
        return authToken;
    }
}
