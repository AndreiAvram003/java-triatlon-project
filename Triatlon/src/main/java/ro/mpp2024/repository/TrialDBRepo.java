package ro.mpp2024.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2024.JdbcUtils;
import ro.mpp2024.domain.Participant;
import ro.mpp2024.domain.Referee;
import ro.mpp2024.domain.Trial;

import java.sql.*;
import java.util.*;

public class TrialDBRepo implements TrialRepository {

    private JdbcUtils dbUtils;

    private RefereeRepository refereeRepository;

    private static final Logger logger = LogManager.getLogger();

    public TrialDBRepo(Properties props) {
        logger.info("Initializing TrialDBRepository with properties: {} ", props);
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public Optional<Trial> save(Trial trial) {
        logger.traceEntry("Saving trial {} ", trial);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("INSERT INTO trials(name, referee_id) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            preStmt.setString(1, trial.getName());
            preStmt.setLong(2, trial.getReferee().getId());
            int result = preStmt.executeUpdate();
            if (result > 0) {
                ResultSet generatedKeys = preStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    Long id = generatedKeys.getLong(1);
                    trial.setId(id);
                    logger.trace("Saved {} instances", result);
                    logger.traceExit("Trial saved successfully: {}", trial);
                    return Optional.of(trial);
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit();
        return Optional.empty();
    }

    @Override
    public Optional<Trial> getById(Long id) {
        logger.traceEntry("Getting trial with id {}", id);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("SELECT * FROM trials WHERE id = ?")) {
            preStmt.setLong(1, id);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    String name = result.getString("name");
                    Long refereeId = result.getLong("referee_id");
                    List<Participant> participants = getParticipantsForTrial(id);
                    Referee referee = refereeRepository.getById(refereeId).orElse(null);
                    Trial trial = new Trial(id, referee,participants, name);
                    logger.trace("Found trial {}", trial);
                    return Optional.of(trial);
                } else {
                    logger.traceExit("Trial with id {} not found", id);
                    return Optional.empty();
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB " + ex);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Trial> update(Trial trial) {
        logger.traceEntry("Updating trial {}", trial);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("UPDATE trials SET name = ?, referee_id = ? WHERE id = ?")) {
            preStmt.setString(1, trial.getName());
            preStmt.setLong(2, trial.getReferee().getId());
            preStmt.setLong(3, trial.getId());
            int result = preStmt.executeUpdate();
            if (result > 0) {
                logger.trace("Updated {} instances", result);
                logger.traceExit("Trial updated successfully: {}", trial);
                return Optional.of(trial);
            } else {
                logger.trace("No instances updated");
                logger.traceExit("Failed to update trial: {}", trial);
                return Optional.empty();
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB " + ex);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Trial> deleteById(Long id) {
        logger.traceEntry("Deleting trial with id {}", id);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("DELETE FROM trials WHERE id = ?")) {
            preStmt.setLong(1, id);
            int result = preStmt.executeUpdate();
            if (result > 0) {
                logger.trace("Deleted {} instances", result);
                logger.traceExit("Trial with id {} deleted successfully", id);
                return Optional.of(new Trial(id, null, null,null)); // Returning a placeholder Trial object
            } else {
                logger.trace("No instances deleted");
                logger.traceExit("Failed to delete trial with id {}", id);
                return Optional.empty();
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB " + ex);
            return Optional.empty();
        }
    }

    @Override
    public List<Trial> getAll() {
        logger.traceEntry("Getting all trials");
        List<Trial> trials = new ArrayList<>();
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("SELECT * FROM trials");
             ResultSet resultSet = preStmt.executeQuery()) {
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                Long refereeId = resultSet.getLong("referee_id");
                List<Participant> participants = getParticipantsForTrial(id);
                Referee referee = refereeRepository.getById(refereeId).orElse(null);
                Trial trial = new Trial(id, referee,participants,name);
                trials.add(trial);
            }
            logger.traceExit("Retrieved {} trials", trials.size());
            return trials;
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB " + ex);
            return Collections.emptyList();
        }
    }
    private List<Participant> getParticipantsForTrial(Long trialId) throws SQLException {
        List<Participant> participants = new ArrayList<>();
        try (Connection con = dbUtils.getConnection();
             PreparedStatement preStmt = con.prepareStatement("SELECT p.* FROM participants p " +
                     "INNER JOIN trial_participants tp ON p.id = tp.participant_id " +
                     "WHERE tp.trial_id = ?")) {
            preStmt.setLong(1, trialId);
            try (ResultSet resultSet = preStmt.executeQuery()) {
                while (resultSet.next()) {
                    Long id = resultSet.getLong("id");
                    String name = resultSet.getString("name");
                    Integer points = resultSet.getInt("points");
                    participants.add(new Participant(id, name, points));
                }
            }
        }
        return participants;
    }
}
