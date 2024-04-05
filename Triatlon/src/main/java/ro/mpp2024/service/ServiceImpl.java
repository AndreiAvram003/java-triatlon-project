package ro.mpp2024.service;

import ro.mpp2024.domain.Participant;
import ro.mpp2024.domain.Referee;
import ro.mpp2024.domain.Result;
import ro.mpp2024.domain.Trial;
import ro.mpp2024.repository.*;

import java.util.List;
import java.util.Optional;


public class ServiceImpl implements Service{

    private final ParticipantRepository participantRepository;

    private final TrialRepository trialRepository;

    private final ResultRepository resultRepository;

    private final RefereeRepository refereeRepository;

public ServiceImpl(ParticipantRepository participantRepository, TrialRepository trialRepository, ResultRepository resultRepository, RefereeRepository refereeRepository) {
        this.participantRepository = participantRepository;
        this.trialRepository = trialRepository;
        this.resultRepository = resultRepository;
        this.refereeRepository = refereeRepository;
    }


    @Override
    public Referee login(String username, String password) {

        List<Referee> referees = refereeRepository.getAll();
        for(Referee referee : referees){
            if(referee.getName().equals(username) && referee.getPassword().equals(password)){
                return referee;
            }
        }
        return null;
    }

    @Override
    public void logout() {

    }

    @Override
    public List<Participant>  getParticipants() {

        return participantRepository.getAllSorted();

    }

    @Override
    public Result addResult(Participant participant, Trial trial, int points) {
        Result result = new Result(participant, trial, points);
        if (resultRepository.save(result).isPresent()) {
            return result;
        } else {
            return null;
        }
    }

    @Override
    public int getTotalPointsAtTrial(Participant participant, Trial trial) {
        return resultRepository.getTotalPointsAtTrial(participant.getId(), trial.getId());
    }

    @Override
    public List<Participant> getParticipantsWithPointsAtTrial(Trial trial) {
        return resultRepository.getParticipantsWithPointsAtTrial(trial.getId());
    }
}
