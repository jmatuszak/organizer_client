/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.organizerClient.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import lombok.Cleanup;
import org.organizerClient.TaskService;
import org.organizerClient.client.RestClient;
import org.organizerClient.dataObjects.Todos;
import org.organizerClient.gui.utilities.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static org.organizerClient.gui.utilities.Constants.*;

/**
 * @author Too
 */
@Component
public class HomeController implements Initializable {
    private RestClient restClient;
    private TaskItemController taskItemController;
    private TaskService taskService;
    private List<Todos> todosFromService;
    @Autowired
    public HomeController(RestClient restClient, TaskItemController controller,TaskService taskService) {
        this.restClient = restClient;
        this.taskItemController = controller;
        this.taskService = taskService;
    }

    @FXML
    private Label lblToday;

    @FXML
    private Label lblUpcoming;

    @FXML
    private VBox vTaskItems;

    private ObservableList<TasksModel> listOfTasks;

    @FXML
    private void closeWindow(MouseEvent event) {
        System.exit(0);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(fetchList);

        fetchList.setOnSucceeded((event) -> {

            listOfTasks = FXCollections.observableArrayList(fetchList.getValue());
            int size = listOfTasks.size();
            lblToday.setText("Today(" + size + ")");
            lblUpcoming.setText("Upcoming(" + 0 + ")");

            try { //load task items to vbox
                Node[] nodes = new Node[size];
                for (int i = 0; i < nodes.length; i++) {
                    //load specific item
                    FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_ITEM_TASK));
                    loader.setController(taskItemController);
                    nodes[i] = loader.load();
                    vTaskItems.getChildren().add(nodes[i]);
                    taskItemController.setTask(listOfTasks.get(i));
                    initTaskItemControllerNodes();
                }

                // Optional
                for (int i = 0; i < nodes.length; i++) {
                    try {
                        nodes[i] = FXMLLoader.load(getClass().getResource(FXML_ITEM_TASK));
                        //vTaskItemsupcoming.getChildren().add(nodes[i]);
                    } catch (Exception e) {
                    }
                }
            } catch (Exception e) {
                System.err.println("Error Creating Tasks...");
                System.err.println(e.getMessage());
            }
        });
    }

    private void initTaskItemControllerNodes() {
        Button actionBtn = taskItemController.getBtnInfo();
        actionBtn.setOnAction(evt -> {

            String taskName = ((Label) ((BorderPane) ((Button) evt.getSource()).getParent()).getCenter()).getText();
            Optional<Todos> optionalTodo = taskService.findTodoByName(taskName);
            optionalTodo.ifPresent(todo -> {
                listOfTasks.stream().filter(tasksModel -> tasksModel.getTitle().equals(taskName))
                        .findFirst()
                        .ifPresent(tasksModel -> tasksModel.setCompleted(getTaskState(tasksModel.getCompleted())));
                BorderPane pane = (BorderPane) vTaskItems.getChildren().stream()
                        .filter(children -> ((Label) ((BorderPane) children).getChildren().stream().filter(node -> node instanceof Label).findFirst().get()).getText().equals(taskName))
                        .findFirst().get();
                Button button = (Button) pane.getChildren().stream().filter(node -> node instanceof Button).findFirst().get();
                ImageView image = (ImageView) pane.getChildren().stream().filter(node -> node instanceof ImageView).findFirst().get();
                changeNodeStates(button,image);
                updateOrganiserServiceObjects(taskName);
                evt.consume();
                //1. Wyjąć taska z obecnym stanem z listy i podmienić mu stan na przecistawny
                //2. podmiana buttona na przeciwstawny i tak samo ikonka (może przekreslenie)
                //3. bierzemy
            });
        });
    }

    private void updateOrganiserServiceObjects(String taskName) {
        taskService.updateTaskState(todosFromService,taskName);
    }

    private void changeNodeStates(Button button, ImageView image) {
        String buttonText = button.getText();
        switch (buttonText){
            case BTN_COMPLETE_TEXT:
                button.setText(BTN_INCOMPLETE_TEXT);
                image.setImage(new Image(getClass().getResourceAsStream(ICON_CHECK_UNFILL)));
                break;
            case BTN_INCOMPLETE_TEXT:
                button.setText(BTN_COMPLETE_TEXT);
                image.setImage(new Image(getClass().getResourceAsStream(ICON_CHECK_FILL)));
                break;
        }
    }

    private Boolean getTaskState(Boolean completed) {
        return !completed;
    }

    private final Task<List<TasksModel>> fetchList = new Task() {

        @Override
        protected List<TasksModel> call() {
            todosFromService = restClient.getAllTodos();
            List<TasksModel> list = todosFromService.stream().map(todo -> new TasksModel(todo.getTask().getTaskName(), todo.getComplete())).collect(Collectors.toList());
            return list;
        }

    };



}
