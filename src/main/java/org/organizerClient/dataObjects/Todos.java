package org.organizerClient.dataObjects;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"id",
"taskDate",
"complete",
"task"
})
@Generated("jsonschema2pojo")
public class Todos {

@JsonProperty("id")
private Integer id;
@JsonProperty("taskDate")
private String taskDate;
@JsonProperty("complete")
private Boolean complete;
@JsonProperty("task")
private Task task;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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

@JsonProperty("task")
public Task getTask() {
return task;
}

@JsonProperty("task")
public void setTask(Task task) {
this.task = task;
}

@JsonAnyGetter
public Map<String, Object> getAdditionalProperties() {
return this.additionalProperties;
}

@JsonAnySetter
public void setAdditionalProperty(String name, Object value) {
this.additionalProperties.put(name, value);
}

    @Override
    public String toString() {
        return "Todos{" +
                "id=" + id +
                ", taskDate='" + taskDate + '\'' +
                ", complete=" + complete +
                ", task=" + task +
                ", additionalProperties=" + additionalProperties +
                '}';
    }
}