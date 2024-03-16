package ro.mpp2024.repository;

import ro.mpp2024.domain.Participant;

import java.util.List;
import java.util.Optional;

public interface ParticipantRepository extends Repository<Participant,Long> {


    @Override
    Optional<Participant> save(Participant participant);

    @Override
    Optional<Participant> getById(Long id);

    @Override
    Optional<Participant> update(Participant participant);

    @Override
    Optional<Participant> deleteById(Long id);

    @Override
    List<Participant> getAll();
}
