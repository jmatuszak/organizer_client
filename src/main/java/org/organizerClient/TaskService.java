package org.organizerClient;

import org.organizerClient.client.RestClient;
import org.organizerClient.domain.Task;
import org.organizerClient.domain.User;
import org.organizerClient.gui.utilities.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class TaskService {

    private RestClient restClient;

    @Autowired
    public TaskService(RestClient restClient) {
        this.restClient = restClient;
    }


//    public Optional<Task> findTaskById(Integer taskId) {
//        return Optional.of(restClient.findTaskById(taskId));
//    }

    public Task findTaskById(User user, String taskId){
        return restClient.findTaskById(user,Integer.parseInt(taskId));
    }


    public HttpStatus registerUser(User registeredUser) {
        return restClient.register(registeredUser);
    }

    public void updateTaskState(User user, int taskId, String buttonText) {
        restClient.updateTaskState(user, taskId, buttonText.equals(Constants.BTN_COMPLETE_TEXT));
    }
}
