package ro.mpp2024.repository;

import ro.mpp2024.domain.Referee;

import java.util.List;
import java.util.Optional;

public interface RefereeRepository extends Repository<Referee,Long> {
    @Override
    Optional<Referee> save(Referee referee);

    @Override
     Optional<Referee> getById(Long id);

    @Override
    Optional<Referee> update(Referee referee);

    @Override
    Optional<Referee> deleteById(Long id);

    @Override
    List<Referee> getAll();
}
