package org.organizerClient;

import org.organizerClient.client.RestClient;
import org.organizerClient.dataObjects.Task;
import org.organizerClient.dataObjects.Todos;
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

    public Optional<Todos> findTodoByName(String todoName){
        List<Todos> allTodos = restClient.getAllTodos();
        return allTodos.stream().filter(todo -> todo.getTask().getTaskName().equals(todoName)).findAny();
    }

    public void updateTaskState(List<Todos> todosFromService, String taskName) {
        todosFromService.stream().filter(todo -> todo.getTask().getTaskName().equals(taskName))
                .findFirst().ifPresent(todo -> {
                    todo.setComplete(!todo.getComplete());
                    restClient.updateTodo(todo);
        });
    }

    public void updateTodo(Todos todo){
        restClient.updateTodo(todo);

    }

    public Optional<Task> findTaskById(Integer taskId) {
        return Optional.of(restClient.findTaskById(taskId));
    }
}
