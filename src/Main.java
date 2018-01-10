import domain.Candidate;
import domain.Section;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import repositories.CandidateFileRepository;
import repositories.OptionFileRepository;
import repositories.SectionFileRepository;
import repositories.UsersFileRepository;
import services.*;
import utils.DayAndCandidates;
import validators.CandidateValidator;
import validators.OptionValidator;
import validators.SectionValidator;
import validators.UserValidator;
import view.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage){

        CandidateFileRepository repositoryCandidat = new CandidateFileRepository(new CandidateValidator(), ".\\src\\data\\candidati.xml");
        CandidateService serviceCandidat = new CandidateService(repositoryCandidat);
        SectionFileRepository repositorySectie = new SectionFileRepository(new SectionValidator(), ".\\src\\data\\sectii.xml");
        SectionService serviceSection = new SectionService(repositorySectie);
        OptionFileRepository repositoryOption = new OptionFileRepository(new OptionValidator(repositoryCandidat, repositorySectie), ".\\src\\data\\optiuni.xml");
        OptionService serviceOption = new OptionService(repositoryOption);
        UsersFileRepository repositoryUser = new UsersFileRepository(new UserValidator(), ".\\src\\data\\users.xml");
        UserService serviceUser = new UserService(repositoryUser);

        GeneralService service = new GeneralService(serviceCandidat, serviceSection, serviceOption, serviceUser);
        try {

            FXMLLoader loginLoader = new FXMLLoader();
            loginLoader.setLocation(getClass().getResource("/view/LoginScreen.fxml"));
            Pane loginView = (Pane)loginLoader.load();
            LoginController loginController = loginLoader.getController();

            loginController.setServiceAndStage(service, primaryStage);
            primaryStage.setScene(new Scene(loginView));
            primaryStage.setTitle("Login");
            primaryStage.getIcons().add(new Image("/images/icon-app.png"));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static void main(String[] args) {
        launch(args);
    }
}
