package ro.mpp2024.repository;

import ro.mpp2024.domain.Trial;

import java.util.List;
import java.util.Optional;

public interface TrialRepository extends Repository<Trial,Long>{

    @Override
    Optional<Trial> save(Trial trial);

    @Override
    Optional<Trial> getById(Long id);

    @Override
    Optional<Trial> update(Trial trial);

    @Override
    Optional<Trial> deleteById(Long id);

    @Override
    List<Trial> getAll();
}
