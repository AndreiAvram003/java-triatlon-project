package ro.mpp2024.presentation;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ro.mpp2024.domain.Participant;
import ro.mpp2024.domain.Referee;
import ro.mpp2024.domain.Trial;
import ro.mpp2024.exception.ParticipantAlert;
import ro.mpp2024.service.Service;

import java.io.IOException;
import java.sql.Ref;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MainController {

    ObservableList<Participant> model = FXCollections.observableArrayList();

    @FXML
    public Label helloLabel;
    @FXML
    public TableView tableView;
    @FXML
    public TableColumn tableColumnParticipantName;
    @FXML
    public TableColumn tableColumnParticipantPoints;

    Service service;
    Referee referee;

    public void initializeTable() {
        tableColumnParticipantName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumnParticipantPoints.setCellValueFactory(new PropertyValueFactory<>("points"));
        tableView.setItems(model);
    }

    public void initialize(String username) {
        helloLabel.setText("Hello, " + username + "!");
    }

    private void initModel() {
        Iterable<Participant> participants = service.getParticipants();
        List<Participant> participantList = StreamSupport.stream(participants.spliterator(), false)
                .collect(Collectors.toList());
        model.setAll(participantList);
    }
    public void setService(Service service,Referee referee) {
        this.service = service;
        this.referee = referee;
        initModel();
    }



    public void addResult(ActionEvent actionEvent) {
        try {
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/result-view.fxml"));


            AnchorPane root = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add Result");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            //dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);
            if (referee != null){
                ResultController resultController = loader.getController();
                resultController.setService(service,referee);
                resultController.initializeTable();
                dialogStage.show();
            }
            else
            {
                ParticipantAlert.showErrorMessage(null, "You are not logged in!");
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void viewReport(ActionEvent actionEvent) {
        try {
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/report-view.fxml"));


            AnchorPane root = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("View Report");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            //dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);
            if (referee != null){
                ReportController reportController = loader.getController();
                reportController.setService(service,referee);
                reportController.initializeTable();
                dialogStage.show();
            }
            else
            {
                ParticipantAlert.showErrorMessage(null, "You are not logged in!");
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logout(ActionEvent actionEvent) {
        Stage stage = (Stage) helloLabel.getScene().getWindow();
        stage.close();
    }
}
