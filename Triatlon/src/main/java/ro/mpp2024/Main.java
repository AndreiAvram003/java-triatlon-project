package ro.mpp2024;

import ro.mpp2024.domain.Participant;
import ro.mpp2024.repository.ParticipantDBRepo;
import ro.mpp2024.repository.ParticipantRepository;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        Properties props=new Properties();
        try {
            props.load(new FileReader("C:\\Users\\Andrei\\Desktop\\Faculta\\MPP\\mpp-proiect-java-AndreiAvram003\\Triatlon\\bd.config"));
        } catch (IOException e) {
            System.out.println("Cannot find bd.config "+e);
        }

        ParticipantRepository participantRepository=new ParticipantDBRepo(props);
            participantRepository.save(new Participant("Ion", 10));
            System.out.println("Toti participantii din db");
            for(Participant participant:participantRepository.getAll())
                System.out.println(participant);

    }

}
