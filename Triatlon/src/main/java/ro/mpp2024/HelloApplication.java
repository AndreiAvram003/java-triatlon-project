package ro.mpp2024;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ro.mpp2024.presentation.LoginController;
import ro.mpp2024.repository.*;
import ro.mpp2024.service.Service;
import ro.mpp2024.service.ServiceImpl;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class HelloApplication extends Application {

    private Service service;


    @Override
    public void start(Stage stage) throws IOException {
        Properties props = new Properties();

        try {
            props.load(new FileReader("bd.config"));
        } catch (IOException e) {
            System.out.println("Cannot find bd.config " + e);
        }
        ParticipantRepository participantRepository = new ParticipantDBRepo(props);
        TrialRepository trialRepository = new TrialDBRepo(props);
        RefereeRepository refereeRepository = new RefereeDBRepo(props);

        ResultRepository resultRepository = new ResultDBRepo(props);
        service = new ServiceImpl(participantRepository, trialRepository, resultRepository, refereeRepository);
        initView(stage);

        stage.setTitle("Triatlon");
        stage.show();

    }

    private void initView(Stage primaryStage) throws IOException {

        FXMLLoader userLoader = new FXMLLoader();
        userLoader.setLocation(getClass().getResource("/login-view.fxml"));
        primaryStage.setScene(new Scene(userLoader.load()));

        LoginController loginController = userLoader.getController();
        loginController.setService(service);
    }
}

