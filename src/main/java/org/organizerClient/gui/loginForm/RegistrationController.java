package org.organizerClient.gui.loginForm;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.organizerClient.TaskService;
import org.organizerClient.dto.UserRegistration;
import org.organizerClient.dto.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.ResourceBundle;

@Component
@FxmlView("register.fxml")
public class RegistrationController implements Initializable {

    @FXML
    public TextField firstNameTF;
    @FXML
    public TextField lastNameTF;
    @FXML
    public TextField emailTF;
    @FXML
    public TextField loginTF;
    @FXML
    public PasswordField passwordTF;
    @FXML
    public PasswordField passwordVerifyingTF;
    @FXML
    public Label registerBtn;

    private double xOffset = 0;
    private double yOffset = 0;

    @Autowired
    private TaskService taskService;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        registerBtn.setOnMouseClicked(event1 -> performRegistration());
    }

    private void performRegistration() {
//        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

        UserRegistration registeredUser = new UserRegistration(firstNameTF.getText(),
                lastNameTF.getText(),
                emailTF.getText(),
                loginTF.getText(),
                passwordTF.getText(),
                Collections.singletonList(UserRole.USER.toString()));

        if (taskService.registerUser(registeredUser)){
            showDialog(Alert.AlertType.CONFIRMATION,"Gartulacje!","Użytkownik został poprawnie zarejestrowany");
        }
        else {
            showDialog(Alert.AlertType.ERROR, "Coś poszło nie tak!", "Spróbuj zarejestrować się ponownie");
        }
    }

    private void showDialog(Alert.AlertType alertType, String windowTitle, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(windowTitle);
        alert.setContentText(message);
        alert.showAndWait();
    }


    public void back(MouseEvent event) throws IOException {
        Parent blah = FXMLLoader.load(getClass().getResource("pin.fxml"));
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        blah.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        blah.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                appStage.setX(event.getScreenX() - xOffset);
                appStage.setY(event.getScreenY() - yOffset);
            }
        });

        Scene scene = new Scene(blah);


        appStage.setScene(scene);
        appStage.show();

    }

    /**** minimize ****/
    @FXML
    public void minclick(MouseEvent event) throws IOException {

        ((Stage) ((Circle) event.getSource()).getScene().getWindow()).setIconified(true);


    }

    /**** close screen ****/
    @FXML
    public void closeclick(MouseEvent event) throws IOException {


        System.exit(0);


    }
}
