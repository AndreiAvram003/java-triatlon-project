package ro.mpp2024.presentation;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ro.mpp2024.domain.Participant;
import ro.mpp2024.domain.Referee;
import ro.mpp2024.domain.Trial;
import ro.mpp2024.service.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ReportController {

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
}
