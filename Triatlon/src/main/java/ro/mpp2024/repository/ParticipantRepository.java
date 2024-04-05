package ro.mpp2024.repository;

import ro.mpp2024.domain.Participant;

import java.util.List;
import java.util.Optional;

public interface ParticipantRepository extends Repository<Participant,Long> {



    List<Participant> getAllSorted();
}
