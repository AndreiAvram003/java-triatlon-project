package ro.mpp2024.presentation;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import ro.mpp2024.domain.Participant;
import ro.mpp2024.domain.Referee;
import ro.mpp2024.domain.Result;
import ro.mpp2024.domain.Trial;
import ro.mpp2024.exception.ParticipantAlert;
import ro.mpp2024.service.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ResultController {
    @FXML

    public TextField pointsTextField;
    ObservableList<Participant> model = FXCollections.observableArrayList();
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



    public void displayParticipantsAndPointsAtTrial(Trial trial) {
        List<Participant> participants = service.getParticipantsWithPointsAtTrial(trial);
        model.clear(); // Curățăm modelul pentru a elimina datele anterioare

        for (Participant participant : participants) {
            int pointsAtTrial = service.getTotalPointsAtTrial(participant,trial); // Obținem punctele pentru participant la proba dată
            participant.setPoints(pointsAtTrial); // Actualizăm punctele participantului în model
            model.add(participant); // Adăugăm fiecare participant în modelul TableView
        }
    }
    public void setService(Service service, Referee referee) {
        this.service = service;
        this.referee = referee;
        displayParticipantsAndPointsAtTrial(referee.getTrial());
    }

    public void addResult(ActionEvent actionEvent) {
        try{
            Participant participant = (Participant) tableView.getSelectionModel().getSelectedItem();
            if(participant!=null){
                Trial trial = referee.getTrial();
                int points = Integer.parseInt(pointsTextField.getText());
                Result result = service.addResult(participant, trial, points);
                if(result==null) {
                    ParticipantAlert.showMessage(null, Alert.AlertType.INFORMATION, "Add result", "There is already a result for this participant at this trial!");
                }
                else {
                    ParticipantAlert.showMessage(null, Alert.AlertType.INFORMATION, "Save result", "The result has been saved successfully!");
                }}
            else{
                ParticipantAlert.showMessage(null, Alert.AlertType.INFORMATION, "Add result", "You must select a participant!");
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void refresh(ActionEvent actionEvent) {
        displayParticipantsAndPointsAtTrial(referee.getTrial());
    }
}
