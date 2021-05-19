package org.organizerClient.domain;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"id",
"taskName",
"description",
"category"
})
@Generated("jsonschema2pojo")
public class Task {

@JsonProperty("id")
private Integer id;
@JsonProperty("taskName")
private String taskName;
@JsonProperty("description")
private String description;
@JsonProperty("category")
private String category;


@JsonProperty("id")
public Integer getId() {
return id;
}

@JsonProperty("id")
public void setId(Integer id) {
this.id = id;
}

@JsonProperty("taskName")
public String getTaskName() {
return taskName;
}

@JsonProperty("taskName")
public void setTaskName(String taskName) {
this.taskName = taskName;
}

@JsonProperty("description")
public String getDescription() {
return description;
}

@JsonProperty("description")
public void setDescription(String description) {
this.description = description;
}

@JsonProperty("category")
public String getCategory() {
return category;
}

@JsonProperty("category")
public void setCategory(String category) {
this.category = category;
}



    @Override
    public String toString() {
        return "{" +
                "id:" + id +
                ", taskName:'" + taskName + '\'' +
                ", description:'" + description + '\'' +
                ", category:'" + category + '\'' +
                '}';
    }
}