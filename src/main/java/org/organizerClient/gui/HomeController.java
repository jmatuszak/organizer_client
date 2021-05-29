/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.organizerClient.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import net.rgielen.fxweaver.core.FxmlView;
import org.organizerClient.TaskService;
import org.organizerClient.client.RestClient;
import org.organizerClient.domain.TodoList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static org.organizerClient.gui.utilities.Constants.*;

/**
 * @author Too
 */
@Component
@FxmlView("home.fxml")
public class HomeController implements Initializable {
    public Button btnEX;
    public TextArea taskDescriptionTa;
    public Button saveBtn;
    public Button newTaskBtn;
    public Button deleteBtn;
    public TextField taskNameTf;

    public DatePicker taskDatePicker;
    public Button reloadBtn;
    private String lastEditedLbl;
    private Integer taskId;

    private RestClient restClient;
    private TaskItemController taskItemController;
    private TaskService taskService;
    private TodoList todoFromService;

    @Autowired
    public HomeController(RestClient restClient, TaskItemController controller, TaskService taskService) {
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
        reloadBtn.setOnAction(evt -> {
         updateList();
        });
        updateList();
    }

    private void updateList() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(fetchList);
        fetchList.setOnRunning(evt -> System.out.println("running"));
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
        Button taskCompleteBtn = taskItemController.getBtnInfo();
        taskCompleteBtn.setOnAction(evt -> {

            String taskIdFromField = ((Label) ((BorderPane) ((Button) evt.getSource()).getParent()).getTop()).getText();
            int taskId = Integer.parseInt(taskIdFromField);
            TodoList todo = taskService.findTodoForUser();

            listOfTasks.stream().filter(tasksModel -> tasksModel.getId() == taskId)
                    .findFirst()
                    .ifPresent(tasksModel -> tasksModel.setCompleted(getTaskState(tasksModel.getCompleted())));
            BorderPane pane = (BorderPane) vTaskItems.getChildren().stream().filter(node -> ((Label) (((BorderPane) node).getTop())).getText().equals(taskIdFromField)).findAny().orElse(null);
            Button button = (Button) pane.getChildren().stream().filter(node -> node instanceof Button).findFirst().get();
            ImageView image = (ImageView) pane.getChildren().stream().filter(node -> node instanceof ImageView).findFirst().get();
            changeNodeStates(button, image);
            updateOrganiserServiceObjects(taskId);
            evt.consume();

        });

        Label taskNameLbl = taskItemController.getLblTaskName();
        taskNameLbl.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                if (mouseEvent.getClickCount() == 2) {
                    TodoList todo = taskService.findTodoForUser();

                    lastEditedLbl = taskNameLbl.getText();
                    org.organizerClient.domain.Task foundedTask = todo.getTasks().stream().filter(task -> task.getTaskName().equals(taskNameLbl.getText())).findFirst().get();
                    taskId = foundedTask.getId();
                    taskDescriptionTa.clear();
                    String description = foundedTask.getDescription();
                    String[] multiRowDesc = description.split("\\\\n");
                    StringBuilder stringBuilder = new StringBuilder();
                    for (String position : multiRowDesc) {
                        stringBuilder.append("\n" + position);
                    }
                    taskDescriptionTa.setText(stringBuilder.toString());
                    taskNameTf.setText(lastEditedLbl);

                }
            }
        });


        saveBtn.setOnAction(evt -> {
            String description = taskDescriptionTa.getText();
            String taskName = taskNameTf.getText();
            if (taskId != null) {
                TodoList todoList = taskService.findTodoForUser();

                taskService.findTaskById(taskId).ifPresent(task -> {
                    task.setTaskName(taskName);
                    task.setDescription(description);
                    Set<org.organizerClient.domain.Task> tasks = todoList.getTasks();
                    tasks.add(task);
                    todoList.setTasks(tasks);
                    taskService.updateTodo(todoList);
                    taskId = null;
                    lastEditedLbl = null;
                });
            } else {
                org.organizerClient.domain.Task task = new org.organizerClient.domain.Task();
                task.setId(0);
                task.setCategory("");
                task.setTaskName(taskName);
                task.setDescription(description);
                TodoList todo = taskService.findTodoForUser();
                todo.setComplete(false);
                todo.setTasks(Collections.singleton(task));
                taskService.updateTodo(todo);
            }
        });
    }


    private void updateOrganiserServiceObjects(int taskId) {
        taskService.updateTaskState(todoFromService, taskId);
    }

    private void changeNodeStates(Button button, ImageView image) {
        String buttonText = button.getText();
        switch (buttonText) {
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

    private Task<List<TasksModel>> fetchList = new Task() {

        @Override
        public List<TasksModel> call() {
            todoFromService = restClient.getTodoForUser();
            List<TasksModel> tasksList = new ArrayList<>();
            todoFromService.getTasks().forEach(task -> tasksList.add(new TasksModel(task.getId(), task.getTaskName(), todoFromService.getComplete()))); //TODO - complete for task, not for todo
            return tasksList;
        }

    };


}
