package org.organizerClient;

import org.organizerClient.client.RestClient;
import org.organizerClient.dataObjects.Task;
import org.organizerClient.dataObjects.TodoList;
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

    public Optional<TodoList> findTodoByTaskName(String todoName){
        List<TodoList> allTodos = restClient.getAllTodos();
        return allTodos.stream().filter(todo -> todo.getTasks().stream().anyMatch(task -> task.getTaskName().equals(todoName))).findAny();
    }

    public void updateTaskState(List<TodoList> todosFromService, String taskName) {
        todosFromService.stream().filter(todo -> todo.getTasks().stream().anyMatch(task -> task.getTaskName().equals(taskName)))
                .findFirst().ifPresent(todo -> {
                    todo.setComplete(!todo.getComplete());
                    restClient.updateTodo(todo);
        });
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

    public List<TodoList> findAllTodos() {
        return restClient.getAllTodos();
    }
}
