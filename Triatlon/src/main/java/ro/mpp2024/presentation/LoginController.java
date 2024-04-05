package ro.mpp2024.presentation;

import com.sun.tools.javac.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ro.mpp2024.domain.Participant;
import ro.mpp2024.domain.Referee;
import ro.mpp2024.exception.ParticipantAlert;
import ro.mpp2024.service.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


public class LoginController {
    public Label RefereeLabel;
    public ListView participantsListView;


    @FXML
    private TextField passwordField;
    @FXML
    private TextField usernameField;

    Service service;


    public void handleLogin(ActionEvent actionEvent) {
        try {
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/main-window.fxml"));


            AnchorPane root = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Referee");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            //dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);
            Referee wantedRef = service.login(usernameField.getText(),passwordField.getText());
            if (wantedRef != null){
                MainController mainController = loader.getController();
                mainController.setService(service,wantedRef);
                mainController.initialize(wantedRef.getName());
                mainController.initializeTable();
                dialogStage.show();
            }
            else
            {
                ParticipantAlert.showErrorMessage(null, "Wrong username or password! ");
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setService(Service service) {
        this.service = service;
    }



}
