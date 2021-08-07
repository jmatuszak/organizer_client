package org.organizerClient.client;


import lombok.extern.slf4j.Slf4j;
import org.organizerClient.domain.Task;
import org.organizerClient.domain.TodoList;
import org.organizerClient.dto.UserAuth;
import org.organizerClient.dto.UserRegistration;
import org.organizerClient.gui.utilities.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@Component
public class RestClient {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    UserAuth userAuth;

    public TodoList getTodoForUser() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", userAuth.getUserCredentials());
        ResponseEntity<TodoList> todolistResponseEntity = restTemplate.exchange(Constants.JSON_URL, HttpMethod.GET, new HttpEntity<>(headers), TodoList.class);
        return todolistResponseEntity.getBody();
    }

    public void updateTodo(TodoList todo) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", userAuth.getUserCredentials());
        HttpEntity<String> request = new HttpEntity(todo, headers);
        restTemplate.postForObject(Constants.TODO_UPDATE_URL, request, TodoList.class);
    }

    public Task findTaskById(Integer taskId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", userAuth.getUserCredentials());
        ResponseEntity<Task> taskResponseEntity = restTemplate.exchange(String.format(Constants.TASK_BY_ID, taskId), HttpMethod.GET, new HttpEntity<>(headers), Task.class);
        return taskResponseEntity.getBody();
    }

    public void saveTask(Task task) {

    }

    public boolean authenticateUser(String userCredentials) throws URISyntaxException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", userCredentials);
        ResponseEntity<String> serverAuthResponse = null;
        try {
            serverAuthResponse = restTemplate.exchange(new URI("http://localhost:8080/login"), HttpMethod.GET, new HttpEntity<>(headers), String.class);
        } catch (ResourceAccessException rae) {
            log.error("Authenthication failed");
            return false;
        }
        return serverAuthResponse.getStatusCode().is2xxSuccessful();
    }


    public boolean registerUser(UserRegistration registeredUser) {
        HttpEntity<String> request = new HttpEntity(registeredUser);
        ResponseEntity<UserRegistration> serverRegistrationResponse = null;
        try {
            serverRegistrationResponse = restTemplate.exchange("http://localhost:8080/register", HttpMethod.POST, request, UserRegistration.class);
        } catch (ResourceAccessException rae) {
            log.error("Registration failed");
            return false;
        }
        return serverRegistrationResponse.getStatusCode().is2xxSuccessful();
    }
}
