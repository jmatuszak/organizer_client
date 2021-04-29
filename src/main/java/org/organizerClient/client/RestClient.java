package org.organizerClient.client;


import org.organizerClient.dataObjects.Tasks;
import org.organizerClient.dataObjects.Todos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
public class RestClient {

    @Autowired
    RestTemplate restTemplate;

    public List<Todos> getAllTodos(String url){
        Todos[] todosArr = restTemplate.getForObject(url, Todos[].class);
        return Arrays.asList(todosArr);
    }

}
