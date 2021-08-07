package org.organizerClient.gui.utilities;

import javafx.scene.control.Alert;

public class Dialogs {

    public static void showDialog(Alert.AlertType alertType, String windowTitle, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(windowTitle);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
