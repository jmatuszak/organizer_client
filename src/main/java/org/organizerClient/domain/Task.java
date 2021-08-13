package org.organizerClient.domain;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "task",
        "success",
        "date"
})
@Data
@NoArgsConstructor
public class Task {
    private int id;
    private String task;
    private String description;
    private String date;
    private Boolean completed;
}