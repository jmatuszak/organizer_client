package org.organizerClient.client;


import org.organizerClient.dataObjects.Task;
import org.organizerClient.dataObjects.TodoList;
import org.organizerClient.gui.utilities.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
public class RestClient {

    @Autowired
    RestTemplate restTemplate;

    public List<TodoList> getAllTodos(){
        TodoList[] todosArr = restTemplate.getForObject(Constants.JSON_URL, TodoList[].class);
        return Arrays.asList(todosArr);
    }

    public void updateTodo(TodoList todo) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity(todo, headers);
        restTemplate.postForObject(Constants.TODO_UPDATE_URL,request, TodoList.class);
    }

    public Task findTaskById(Integer taskId) {
        return restTemplate.getForObject(String.format(Constants.TASK_BY_ID, taskId), Task.class);
    }

    public void saveTask(Task task) {

    }
}
