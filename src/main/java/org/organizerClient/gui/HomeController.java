/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.organizerClient.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.organizerClient.TaskService;
import org.organizerClient.client.RestClient;
import org.organizerClient.client.UserNotAuthorizedException;
import org.organizerClient.gui.loginForm.LoginController;
import org.organizerClient.gui.utilities.Dialogs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;


import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static org.organizerClient.gui.utilities.Constants.*;

/**
 * @author Too
 */
@Slf4j
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
    public AnchorPane anchorPane;
    private String lastEditedLbl;
    private Integer taskId;


    private RestClient restClient;
    private TaskItemController taskItemController;
    private TaskService taskService;
    private LoginController loginController;
    private ApplicationContext applicationContext;

    @Autowired
    public HomeController(RestClient restClient, TaskItemController controller, TaskService taskService, ApplicationContext applicationContext, LoginController loginController) {
        this.restClient = restClient;
        this.taskItemController = controller;
        this.taskService = taskService;
        this.applicationContext = applicationContext;
        this.loginController = loginController;
    }

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
        saveBtn.setOnMouseClicked(evt -> {
            String description = taskDescriptionTa.getText();
            String taskName = taskNameTf.getText();
            try {
                org.organizerClient.domain.Task createdTask = createTask(description, taskName);
                if (taskId == null) {
                    restClient.saveTask(loginController.getLoggedUser(), createdTask);
                } else {
                    createdTask.setId(taskId);
                    restClient.updateTask(loginController.getLoggedUser(), createdTask);
                }
                clearFields();
            } catch (RestClientException rce) {
                goToLoginScreen();
            }
        });
        newTaskBtn.setOnMouseClicked(evt -> {
            taskId=null;
            clearFields();
        });
        updateList();
    }

    private void clearFields() {
        taskId = null;
        lastEditedLbl = null;
        taskDatePicker.setValue(null);
        taskNameTf.setText("");
        taskDescriptionTa.setText("");
    }

    private org.organizerClient.domain.Task createTask(String description, String taskName) {
        org.organizerClient.domain.Task task1 = new org.organizerClient.domain.Task();
        task1.setTask(taskName.trim());
        task1.setDescription(description);
        task1.setCompleted(false);
        task1.setDate(getDateFromDatePicker());
        return task1;
    }

    private Date getDateFromDatePicker() {
        LocalDate value = taskDatePicker.getValue();
        return Date.valueOf(value);
    }

    private void updateList() {

        if (!svc.isRunning()) {
            svc.setPeriod(Duration.millis(500));
            svc.start();
        }


        svc.setOnSucceeded((event) -> {
            vTaskItems.getChildren().clear();
            listOfTasks = FXCollections.observableArrayList(svc.getValue());
            int size = listOfTasks.size();
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
                log.error("Error Creating Tasks... {}", e.getMessage());
            }
        });

    }

    private void initTaskItemControllerNodes() {
        Button taskCompleteBtn = taskItemController.getBtnInfo();
        taskCompleteBtn.setOnAction(evt -> {

            String taskIdFromField = ((Label) ((BorderPane) ((Button) evt.getSource()).getParent()).getTop()).getText();
            this.taskId = Integer.parseInt(taskIdFromField);
            listOfTasks.stream().filter(tasksModel -> tasksModel.getId() == taskId)
                    .findFirst()
                    .ifPresent(tasksModel -> tasksModel.setCompleted(getTaskState(tasksModel.getCompleted())));
            BorderPane pane = (BorderPane) vTaskItems.getChildren().stream().filter(node -> ((Label) (((BorderPane) node).getTop())).getText().equals(taskIdFromField)).findAny().orElse(null);
            try {
                taskService.updateTaskState(loginController.getLoggedUser(), taskId);
            } catch (RestClientException rce) {
                goToLoginScreen();
            }
            evt.consume();

        });

        Label taskNameLbl = taskItemController.getLblTaskName();
        taskNameLbl.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                if (mouseEvent.getClickCount() == 2) {
                    Parent parent = taskNameLbl.getParent();
                    Set<Integer> idSet = parent.getChildrenUnmodifiable().stream().filter(node -> {
                        if (node instanceof Label) {
                            try {
                                Integer.parseInt(((Label) node).getText());
                                return true;
                            } catch (NumberFormatException nfe) {
                                return false;
                            }
                        }
                        return false;
                    }).map(node -> this.taskId = Integer.parseInt(((Label) node).getText())).collect(Collectors.toSet());
                    this.taskId = idSet.iterator().next();
//                    try {
                    org.organizerClient.domain.Task task = taskService.findTaskById(loginController.getLoggedUser(), this.taskId);
//                    } catch (UserNotAuthorizedException e) {
//                        e.printStackTrace();
//                    }

                    lastEditedLbl = taskNameLbl.getText();
                    taskDescriptionTa.clear();
                    String description = task.getDescription();
                    String[] multiRowDesc = description.split("\\\\n");
                    StringBuilder stringBuilder = new StringBuilder();
                    for (String position : multiRowDesc) {
                        stringBuilder.append(position);
                    }
                    taskDescriptionTa.setText(stringBuilder.toString());
                    taskNameTf.setText(lastEditedLbl.split("\\[")[0]);
                    taskDatePicker.setValue(task.getDate().toLocalDate());

                }
            }
        });


    }

    private void goToLoginScreen() {
        Dialogs.showDialog(Alert.AlertType.WARNING, "UWAGA!", "Użytkownik nie przeszedł autentykacji. Nastąpi przekierowanie do strony logowania.");
        FxWeaver fxWeaver = applicationContext.getBean(FxWeaver.class);
        AnchorPane root = fxWeaver.loadView(LoginController.class);
        Scene scene = new Scene(root);
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }


//    private void updateOrganiserServiceObjects(int taskId) {
//        taskService.updateTaskState(todoFromService, taskId);
//    }


    private Boolean getTaskState(Boolean completed) {
        return !completed;
    }

    ScheduledService<List<TasksModel>> svc = new ScheduledService<List<TasksModel>>() {

        @Override
        protected Task<List<TasksModel>> createTask() {
            return new Task<List<TasksModel>>() {
                @Override
                protected List<TasksModel> call() throws Exception {
                    List<org.organizerClient.domain.Task> allTasksForUser = null;
                    try {
                        allTasksForUser = restClient.getAllTasksForUser(loginController.getLoggedUser());
                    } catch (RestClientException rce) {
                        goToLoginScreen();
                    }
                    List<TasksModel> tasksList = new ArrayList<>();
                    allTasksForUser.forEach(task -> tasksList.add(new TasksModel(task.getId(), task.getTask(), task.getDate(), task.getCompleted()))); //TODO - complete for task, not for todo
                    return tasksList;
                }
            };
        }
    };


}
