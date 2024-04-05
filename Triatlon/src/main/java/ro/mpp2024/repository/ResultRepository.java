package ro.mpp2024.repository;

import ro.mpp2024.domain.Participant;
import ro.mpp2024.domain.Referee;
import ro.mpp2024.domain.Result;

import java.util.List;
import java.util.Optional;

public interface ResultRepository extends Repository<Result, Long> {

    int getTotalPointsAtTrial(Long id, Long id1);

    List<Participant> getParticipantsWithPointsAtTrial(Long id);

}
