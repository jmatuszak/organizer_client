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


import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    public Button reloadBtn;
    public AnchorPane anchorPane;
    private String lastEditedLbl;
    private Integer taskId;

    private RestClient restClient;
    private TaskItemController taskItemController;
    private TaskService taskService;
    private LoginController loginController;
    private ApplicationContext applicationContext;
    @Autowired
    public HomeController(RestClient restClient, TaskItemController controller, TaskService taskService,ApplicationContext applicationContext, LoginController loginController) {
        this.restClient = restClient;
        this.taskItemController = controller;
        this.taskService = taskService;
        this.applicationContext = applicationContext;
        this.loginController = loginController;
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
        reloadBtn.setOnAction(evt -> updateList());
        saveBtn.setOnMouseClicked(evt -> {
            String description = taskDescriptionTa.getText();
            String taskName = taskNameTf.getText();
            try {
//            if (taskId != null) {

//                restClient.saveTask(loginController.getLoggedUser(), )
                org.organizerClient.domain.Task task1 = new org.organizerClient.domain.Task();
                task1.setTask(taskName);
                task1.setDescription(description);
                task1.setCompleted(false);


                taskId = null;
                lastEditedLbl = null;
                restClient.saveTask(loginController.getLoggedUser(), task1);

//            }
//            else {
//                org.organizerClient.domain.Task task = new org.organizerClient.domain.Task();
//                task.setId(0);
//                task.setCategory("");
//                task.setTaskName(taskName);
//                task.setDescription(description);
//                TodoList todo = taskService.findTodoForUser().orElse(new TodoList());
//                todo.setComplete(false);
//                todo.setTasks(Collections.singleton(task));
//                taskService.updateTodo(todo);
//            }
            }catch (HttpClientErrorException httpClientErrorException) {
                FxWeaver fxWeaver = applicationContext.getBean(FxWeaver.class);
                AnchorPane root = fxWeaver.loadView(LoginController.class);
                Scene scene = new Scene(root);
                Stage stage = (Stage) anchorPane.getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            }

        });
        updateList();
    }

    private void updateList() {

        svc.setPeriod(Duration.seconds(1));
        svc.start();

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
                log.error("Error Creating Tasks... {}",e.getMessage());
            }
        });
        executorService.shutdown();
    }

    private void initTaskItemControllerNodes() {
        Button taskCompleteBtn = taskItemController.getBtnInfo();
        taskCompleteBtn.setOnAction(evt -> {

            String taskIdFromField = ((Label) ((BorderPane) ((Button) evt.getSource()).getParent()).getTop()).getText();
            int taskId = Integer.parseInt(taskIdFromField);
 /*           TodoList todo = taskService.findTodoForUser();*/

            listOfTasks.stream().filter(tasksModel -> tasksModel.getId() == taskId)
                    .findFirst()
                    .ifPresent(tasksModel -> tasksModel.setCompleted(getTaskState(tasksModel.getCompleted())));
            BorderPane pane = (BorderPane) vTaskItems.getChildren().stream().filter(node -> ((Label) (((BorderPane) node).getTop())).getText().equals(taskIdFromField)).findAny().orElse(null);
            Button button = (Button) pane.getChildren().stream().filter(node -> node instanceof Button).findFirst().get();
            ImageView image = (ImageView) pane.getChildren().stream().filter(node -> node instanceof ImageView).findFirst().get();
            changeNodeStates(button, image);
            taskService.updateTaskState(loginController.getLoggedUser(), taskId,button.getText());
            evt.consume();

        });

        Label taskNameLbl = taskItemController.getLblTaskName();
        taskNameLbl.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                if (mouseEvent.getClickCount() == 2) {
                    String taskId = taskItemController.getLblTaskId().getText();
//                    try {
                    org.organizerClient.domain.Task task = taskService.findTaskById(loginController.getLoggedUser(), taskId);
//                    } catch (UserNotAuthorizedException e) {
//                        e.printStackTrace();
//                    }

                    lastEditedLbl = taskNameLbl.getText();
                    taskDescriptionTa.clear();
                    String description = task.getDescription();
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




    }


//    private void updateOrganiserServiceObjects(int taskId) {
//        taskService.updateTaskState(todoFromService, taskId);
//    }

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

    ScheduledService<List<TasksModel>> svc = new ScheduledService<List<TasksModel>>(){

        @Override
        protected Task<List<TasksModel>> createTask() {
            return new Task<List<TasksModel>>() {
                @Override
                protected List<TasksModel> call() throws Exception {
                    List<org.organizerClient.domain.Task> allTasksForUser = null;
                    try {
                        allTasksForUser = restClient.getAllTasksForUser(loginController.getLoggedUser());
                    } catch (HttpClientErrorException httpClientErrorException) {
                        Dialogs.showDialog(Alert.AlertType.WARNING,"UWAGA!", "Użytkownik nie przeszedł autentykacji. Nastąpi przekierowanie do strony logowania.");
                        FxWeaver fxWeaver = applicationContext.getBean(FxWeaver.class);
                        AnchorPane root = fxWeaver.loadView(LoginController.class);
                        Scene scene = new Scene(root);
                        Stage stage = (Stage) root.getScene().getWindow();
                        stage.setScene(scene);
                        stage.show();
                    }
                    List<TasksModel> tasksList = new ArrayList<>();
                    allTasksForUser.forEach(task -> tasksList.add(new TasksModel(task.getId(), task.getTask(), task.getCompleted()))); //TODO - complete for task, not for todo
                    return tasksList;
                }
            };
        }
    };

    private Task<List<TasksModel>> fetchList = new Task() {

        @Override
        public List<TasksModel> call() {
            List<org.organizerClient.domain.Task> allTasksForUser = null;
            try {
                allTasksForUser = restClient.getAllTasksForUser(loginController.getLoggedUser());
            } catch (HttpClientErrorException httpClientErrorException) {
                Dialogs.showDialog(Alert.AlertType.WARNING,"UWAGA!", "Użytkownik nie przeszedł autentykacji. Nastąpi przekierowanie do strony logowania.");
                FxWeaver fxWeaver = applicationContext.getBean(FxWeaver.class);
                AnchorPane root = fxWeaver.loadView(LoginController.class);
                Scene scene = new Scene(root);
                Stage stage = (Stage) root.getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            }
            List<TasksModel> tasksList = new ArrayList<>();
            allTasksForUser.forEach(task -> tasksList.add(new TasksModel(task.getId(), task.getTask(), task.getCompleted()))); //TODO - complete for task, not for todo
            return tasksList;
        }

    };


}
