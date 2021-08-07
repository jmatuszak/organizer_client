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
import org.organizerClient.dto.UserAuth;
import org.organizerClient.gui.HomeController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

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

    @Autowired
    private UserAuth userAuth;

    private void aa(ActionEvent event) {
        // Button was clicked, do something...
        System.out.println("jsdfh");
        //  change(this);

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        login.setText("Login");
        goToRegisterLbl.setOnMouseClicked(event1 ->
        {
            FxWeaver fxWeaver = applicationContext.getBean(FxWeaver.class);
            AnchorPane anchorPane = fxWeaver.loadView(RegistrationController.class);
            rootPane.getChildren().setAll(anchorPane);
        });

        login.setOnMouseClicked(evt -> performLogin());
    }

    private void performLogin() {
        userAuth.setUserName("jacek");
        userAuth.setPassword("jacek");
        String userCredentials = userAuth.baseUserCredentials();
        try {
            if (restClient.authenticateUser(userCredentials)) {
                userAuth.setUserCredentials(userCredentials);
                goToUserInterface();
            }
            else {
                showDialog(Alert.AlertType.ERROR,"Błąd!","Podano złe dane logowania! Spróbuj ponownie");
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void goToUserInterface() {
        FxWeaver fxWeaver = applicationContext.getBean(FxWeaver.class);
        Parent root = fxWeaver.loadView(HomeController.class);
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
