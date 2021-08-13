package org.organizerClient.gui;

import javafx.scene.layout.AnchorPane;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.context.ApplicationContext;

public class Commons {

    public static void changeScreen(ApplicationContext applicationContext, AnchorPane rootPane, Class<?> viewController) {
        FxWeaver fxWeaver = applicationContext.getBean(FxWeaver.class);
        AnchorPane anchorPane = fxWeaver.loadView(viewController);
        rootPane.getChildren().setAll(anchorPane);
    }
}
