package org.organizerClient.domain;

import java.util.Set;
import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "taskDate",
        "complete",
        "tasks"
})
@Generated("jsonschema2pojo")
public class TodoList {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("taskDate")
    private String taskDate;
    @JsonProperty("complete")
    private Boolean complete;
    @JsonProperty("tasks")
    private Set<Task> tasks;


    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("taskDate")
    public String getTaskDate() {
        return taskDate;
    }

    @JsonProperty("taskDate")
    public void setTaskDate(String taskDate) {
        this.taskDate = taskDate;
    }

    @JsonProperty("complete")
    public Boolean getComplete() {
        return complete;
    }

    @JsonProperty("complete")
    public void setComplete(Boolean complete) {
        this.complete = complete;
    }

    @JsonProperty("tasks")
    public Set<Task> getTasks() {
        return tasks;
    }


    @JsonProperty("tasks")
    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }



}