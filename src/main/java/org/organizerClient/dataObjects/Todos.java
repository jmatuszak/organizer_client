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

    @Override
    public String toString() {
        //        return "{\n" +
//                "        \"id\": 1,\n" +
//                "        \"taskDate\": \"2021-04-29T17:10:06.765+00:00\",\n" +
//                "        \"complete\": true,\n" +
//                "        \"task\": {\n" +
//                "            \"id\": 2,\n" +
//                "            \"taskName\": \"wynieść śmieci\",\n" +
//                "            \"description\": \"To nie jest trudne\",\n" +
//                "            \"category\": \"Domowe obowiązki\"\n" +
//                "        }\n" +
//                "    }";
        return "{" +
                "xd:" + id +
                ", taskDate:'" + taskDate + '\'' +
                ", complete:" + complete +
                ", task:" + task +
                '}';
    }
}