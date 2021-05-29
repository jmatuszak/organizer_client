package org.organizerClient;

import org.organizerClient.client.RestClient;
import org.organizerClient.domain.Task;
import org.organizerClient.domain.TodoList;
import org.organizerClient.dto.UserRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TaskService {

    private RestClient restClient;

    @Autowired
    public TaskService(RestClient restClient) {
        this.restClient = restClient;
    }


    public void updateTaskState(TodoList todoFromService, int taskId) {
        todoFromService.setComplete(!todoFromService.getComplete());
        restClient.updateTodo(todoFromService);
    }

    public void updateTodo(TodoList todo){
        restClient.updateTodo(todo);

    }

    public Optional<Task> findTaskById(Integer taskId) {
        return Optional.of(restClient.findTaskById(taskId));
    }

    public void saveTask(Task task) {
        restClient.saveTask(task);
    }

    public TodoList findTodoForUser() {
        return restClient.getTodoForUser();
    }

    public boolean registerUser(UserRegistration registeredUser) {
        return restClient.registerUser(registeredUser);
    }
}
