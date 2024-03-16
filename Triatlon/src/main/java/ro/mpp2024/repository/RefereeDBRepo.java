package ro.mpp2024.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2024.JdbcUtils;
import ro.mpp2024.domain.Referee;
import ro.mpp2024.domain.Trial;

import java.sql.*;
import java.util.*;

public class RefereeDBRepo implements RefereeRepository {

    private JdbcUtils dbUtils;

    private TrialRepository trialRepository;

    private static final Logger logger = LogManager.getLogger();

    public RefereeDBRepo(Properties props) {
        logger.info("Initializing RefereeDBRepository with properties: {} ", props);
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public Optional<Referee> save(Referee referee) {
        logger.traceEntry("Saving referee {} ", referee);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("INSERT INTO referees(name, password, trial_id) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            preStmt.setString(1, referee.getName());
            preStmt.setString(2, referee.getPassword());
            preStmt.setLong(3, referee.getTrial().getId());
            int result = preStmt.executeUpdate();
            if (result > 0) {
                ResultSet generatedKeys = preStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    Long id = generatedKeys.getLong(1);
                    referee.setId(id);
                    logger.trace("Saved {} instances", result);
                    logger.traceExit("Referee saved successfully: {}", referee);
                    return Optional.of(referee);
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
    public Optional<Referee> getById(Long id) {
        logger.traceEntry("Getting referee with id {}", id);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("SELECT * FROM referees WHERE id = ?")) {
            preStmt.setLong(1, id);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    String name = result.getString("name");
                    String password = result.getString("password");
                    Long trialId = result.getLong("trial_id");
                    // Assuming you have a TrialRepository and a method getById to fetch Trial object by id
                    Trial trial = trialRepository.getById(trialId).orElse(null);
                    Referee referee = new Referee(id, name, password, trial);
                    logger.trace("Found referee {}", referee);
                    return Optional.of(referee);
                } else {
                    logger.traceExit("Referee with id {} not found", id);
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
    public Optional<Referee> update(Referee referee) {
        logger.traceEntry("Updating referee {}", referee);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("UPDATE referees SET name = ?, password = ?, trial_id = ? WHERE id = ?")) {
            preStmt.setString(1, referee.getName());
            preStmt.setString(2, referee.getPassword());
            preStmt.setLong(3, referee.getTrial().getId());
            preStmt.setLong(4, referee.getId());
            int result = preStmt.executeUpdate();
            if (result > 0) {
                logger.trace("Updated {} instances", result);
                logger.traceExit("Referee updated successfully: {}", referee);
                return Optional.of(referee);
            } else {
                logger.trace("No instances updated");
                logger.traceExit("Failed to update referee: {}", referee);
                return Optional.empty();
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB " + ex);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Referee> deleteById(Long id) {
        logger.traceEntry("Deleting referee with id {}", id);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("DELETE FROM referees WHERE id = ?")) {
            preStmt.setLong(1, id);
            int result = preStmt.executeUpdate();
            if (result > 0) {
                logger.trace("Deleted {} instances", result);
                logger.traceExit("Referee with id {} deleted successfully", id);
                return Optional.of(new Referee(id,null,null,null)); // Returning a placeholder Referee object
            } else {
                logger.trace("No instances deleted");
                logger.traceExit("Failed to delete referee with id {}", id);
                return Optional.empty();
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB " + ex);
            return Optional.empty();
        }
    }

    @Override
    public List<Referee> getAll() {
        logger.traceEntry("Getting all referees");
        List<Referee> referees = new ArrayList<>();
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("SELECT * FROM referees");
             ResultSet resultSet = preStmt.executeQuery()) {
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String password = resultSet.getString("password");
                Long trialId = resultSet.getLong("trial_id");
                // Assuming you have a TrialRepository and a method getById to fetch Trial object by id
                Trial trial = trialRepository.getById(trialId).orElse(null);
                Referee referee = new Referee(id, name, password, trial);
                referees.add(referee);
            }
            logger.traceExit("Retrieved {} referees", referees.size());
            return referees;
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB " + ex);
            return Collections.emptyList();
        }
    }
}
