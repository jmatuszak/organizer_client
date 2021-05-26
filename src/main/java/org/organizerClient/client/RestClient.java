package org.organizerClient.client;


import org.organizerClient.domain.Task;
import org.organizerClient.domain.TodoList;
import org.organizerClient.dto.UserAuth;
import org.organizerClient.dto.UserRegistration;
import org.organizerClient.gui.utilities.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

@Component
public class RestClient {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    UserAuth userAuth;

    public List<TodoList> getAllTodos(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", userAuth.getUserCredentials());
        ResponseEntity<TodoList[]> todolistResponseEntity = restTemplate.exchange(Constants.JSON_URL, HttpMethod.GET, new HttpEntity<>(headers), TodoList[].class);
        return Arrays.asList(todolistResponseEntity.getBody());
    }

    public void updateTodo(TodoList todo) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", userAuth.getUserCredentials());
        HttpEntity<String> request = new HttpEntity(todo, headers);
        restTemplate.postForObject(Constants.TODO_UPDATE_URL,request, TodoList.class);
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
        ResponseEntity<String> serverAuthResponse = restTemplate.exchange(new URI("http://localhost:8080/login"), HttpMethod.GET,new HttpEntity<>(headers), String.class);
        return serverAuthResponse.getStatusCode().is2xxSuccessful();
    }


    public boolean registerUser(UserRegistration registeredUser) {
        HttpEntity<String> request = new HttpEntity(registeredUser);
        ResponseEntity<UserRegistration> serverRegistrationResponse = restTemplate.exchange("http://localhost:8080/register", HttpMethod.POST, request, UserRegistration.class);
        return serverRegistrationResponse.getStatusCode().is2xxSuccessful();
    }
}
