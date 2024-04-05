package ro.mpp2024.service;

import ro.mpp2024.domain.Participant;
import ro.mpp2024.domain.Referee;
import ro.mpp2024.domain.Result;
import ro.mpp2024.domain.Trial;

import java.util.List;

public interface Service {
    Referee login(String username, String password);
    void logout();
    List<Participant> getParticipants();

    Result addResult(Participant participant, Trial trial, int points);

    int getTotalPointsAtTrial(Participant participant, Trial trial);

    List<Participant> getParticipantsWithPointsAtTrial(Trial trial);


}
