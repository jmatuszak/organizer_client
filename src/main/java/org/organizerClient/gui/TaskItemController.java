/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.organizerClient.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.organizerClient.gui.utilities.Constants;
import org.springframework.stereotype.Component;


import java.net.URL;
import java.util.ResourceBundle;

import static org.organizerClient.gui.utilities.Constants.BTN_COMPLETE_TEXT;
import static org.organizerClient.gui.utilities.Constants.BTN_INCOMPLETE_TEXT;

/**
 * FXML Controller class
 *
 * @author Too
 */
@Component
public class TaskItemController{

    @FXML
    private ImageView iconSelect;

    @FXML
    private Label lblTaskName;

    @FXML
    private Label lblTaskId;

    @FXML
    private Button btnInfo;


    public void setTask(TasksModel model) {
        lblTaskName.setText(model.getTitle());
        lblTaskId.setText(String.valueOf(model.getId()));
        Boolean isTaskCompleted = model.getCompleted();
        switchButton(isTaskCompleted);

    }

    public void changeTaskState(TasksModel tasksModel) {
        switchButton(tasksModel.getCompleted());
    }

    public void switchButton(boolean isTaskCompleted) {
        if (isTaskCompleted) {
            btnInfo.setText(BTN_COMPLETE_TEXT);
            iconSelect.setImage(new Image(getClass().getResourceAsStream(Constants.ICON_CHECK_FILL)));
        } else {
            btnInfo.setText(BTN_INCOMPLETE_TEXT);
            iconSelect.setImage(new Image(getClass().getResourceAsStream(Constants.ICON_CHECK_UNFILL)));
        }
    }

    public ImageView getIconSelect() {
        return iconSelect;
    }

    public Label getLblTaskName() {
        return lblTaskName;
    }

    public Label getLblTaskId() {
        return lblTaskId;
    }

    public Button getBtnInfo() {
        return btnInfo;
    }


}
