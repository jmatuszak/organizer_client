package org.organizerClient.gui.loginForm;

import javafx.event.ActionEvent;
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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.organizerClient.client.RestClient;
import org.organizerClient.domain.User;
import org.organizerClient.dto.UserAuth;
import org.organizerClient.gui.HomeController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

import static org.organizerClient.gui.Commons.changeScreen;
import static org.organizerClient.gui.utilities.Dialogs.showDialog;

@Component
@FxmlView("login.fxml")
public class LoginController implements Initializable {

    @FXML
    public PasswordField passwordTF;
    @FXML
    public TextField userNameTF;
    @FXML
    public Label loginLBL;
    @FXML
    public Label goToCreateAccount;
    public Label goToRegisterLbl;
    public AnchorPane rootPane;
    @FXML
    ImageView ic;
    @FXML
    Circle min;
    @FXML
    Circle close;
    ActionEvent event;

    @FXML
    Label login;
    private double xOffset = 0;
    private double yOffset = 0;

    @Autowired
    private RestClient restClient;

    @Autowired
    private ApplicationContext applicationContext;

    private User loggedUser;

    public User getLoggedUser() {
        return loggedUser;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        login.setText("Login");
        goToRegisterLbl.setOnMouseClicked(event -> changeScreen(applicationContext, rootPane, RegistrationController.class));
        login.setOnMouseClicked(evt -> performLogin());
    }


    private void performLogin() {
        try {
            User loggedUser = restClient.login(userNameTF.getText(), passwordTF.getText());
            if (loggedUser != null) {
                this.loggedUser = loggedUser;
                goToUserInterface();
            }
        } catch (HttpClientErrorException httpClientErrorException) {
            showDialog(Alert.AlertType.ERROR, "Błąd!", "Podano złe dane logowania! Spróbuj ponownie");

        }
    }

    private void goToUserInterface() {
        FxWeaver fxWeaver = applicationContext.getBean(FxWeaver.class);
        AnchorPane root = fxWeaver.loadView(HomeController.class);
        Scene scene = new Scene(root);
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
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
