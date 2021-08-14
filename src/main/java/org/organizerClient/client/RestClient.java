package org.organizerClient.client;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.organizerClient.domain.Task;
import org.organizerClient.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
public class RestClient {

    private final RestTemplate restTemplate;

    @Value("${motivation.server.host}")
    private String serverHost;

    @Autowired
    public RestClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public User login(String username, String password) {
        String url = String.format("%s/login", serverHost);
        HttpHeaders headerWithUserData = createHeaderWithUserData(username, password);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headerWithUserData), String.class);
        HttpStatus statusCode = responseEntity.getStatusCode();
        if (statusCode.is2xxSuccessful()) {
            User user = new User();
            user.setHeaderWithCredentials(headerWithUserData);
            return user;
        }
        return null;
    }

    public HttpStatus register(User userData) {
        HttpEntity<User> UserHttpEntity = new HttpEntity<>(userData);
        ResponseEntity<String> responseEntity = restTemplate.exchange(String.format("%s/register", serverHost), HttpMethod.POST, UserHttpEntity, String.class);
        return responseEntity.getStatusCode();
    }

    public List<Task> getAllTasksForUser(User user) {
        HttpHeaders headerWithCredentials = user.getHeaderWithCredentials();
        String url = String.format("%s/findAllTasksForUser", serverHost);
        ResponseEntity<Task[]> exchange = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headerWithCredentials), Task[].class);
        return Arrays.stream(Objects.requireNonNull(exchange.getBody())).filter(Objects::nonNull).collect(Collectors.toList());
    }



    public ResponseEntity<Integer> saveTask(User User, Task task) {
        HttpHeaders headerWithCredentials = User.getHeaderWithCredentials();
        String url = String.format("%s/addTask", serverHost);
        return restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(task, headerWithCredentials), Integer.class);
    }

    public Task findTaskById(User user, int id) {
        HttpHeaders headerWithCredentials = user.getHeaderWithCredentials();
        String url = String.format("%s/getTask/%d", serverHost,id);
        return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headerWithCredentials), Task.class).getBody();
    }

    public ResponseEntity<Boolean> deleteTask(User user, int id) {
        HttpHeaders headerWithCredentials = user.getHeaderWithCredentials();
        String url = String.format("%s/deleteTask/%d", serverHost,id);
        return restTemplate.exchange(url, HttpMethod.DELETE, new HttpEntity<>(headerWithCredentials), Boolean.class);
    }

    private HttpHeaders createHeaderWithUserData(String username, String password) {
        String basicAuthString = username + ":" + password;
        byte[] encodedAuthData = Base64.encodeBase64(basicAuthString.getBytes(StandardCharsets.UTF_8));
        String authHeader = "Basic " + new String(encodedAuthData);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", authHeader);
        return httpHeaders;
    }

    public void changeTaskState(User user, int taskId) {
        HttpHeaders headerWithCredentials = user.getHeaderWithCredentials();
        String url = String.format("%s/changeTaskState/%s", serverHost,taskId);
        restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(headerWithCredentials), String.class);
    }

    public void updateTask(User user, Task task) {
        HttpHeaders headerWithCredentials = user.getHeaderWithCredentials();
        String url = String.format("%s/updateTask", serverHost);
        restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(task,headerWithCredentials), String.class);

    }
}
