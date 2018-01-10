package view;

import domain.Candidate;
import domain.Gender;
import domain.ValidationException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import services.CandidateService;
import services.Service;
import utils.ListEvent;
import utils.Observer;
import utils.User;
import utils.UserType;


import javax.swing.text.html.ImageView;
import java.io.IOException;

public class AddCandidateController {

    private Service<Integer, Candidate> service;
    private Service<String, User> serviceUser;
    private Stage stage;
    private Candidate candidate;

    @FXML
    TextField nameTxt;

    @FXML
    ComboBox genderTxt;

    @FXML
    TextField phoneTxt;

    @FXML
    TextField mailTxt;

    @FXML
    public void initialize(){
        genderTxt.getItems().addAll(
                "Masculin",
                "Feminin"
        );
    }

    @FXML
    public void handleSave(MouseEvent ev){
        try {
            if(candidate != null){
                service.update(getCandidateForUpdate());
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Modificat !", "Candidat modificat cu succes !");
            }
            else{
                service.save(getCandidate());
                serviceUser.save(new User(getCandidate().getMail(), "parolaubb", UserType.Candidate));
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Adaugat", "Candidat adaugat cu succes !");
            }
            stage.close();
        } catch (ValidationException e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }

    public void setService(Service service, Service serviceUser, Candidate candidate, Stage stage){
        this.service = service;
        this.serviceUser = serviceUser;
        this.stage = stage;
        this.candidate = null;

        if(candidate != null) {
            this.candidate = candidate;
            setFields(candidate);
        }
    }


    private void setFields(Candidate candidate){
        nameTxt.setText(candidate.getName());
        genderTxt.setValue("Masculin");
        if(candidate.getSex().equals(Gender.F)){
            genderTxt.setValue("Feminin");
        }
        phoneTxt.setText(candidate.getPhoneNr());
        mailTxt.setText(candidate.getMail());
    }

    private Candidate getCandidate(){
        String name = nameTxt.getText();

        Gender gender;
        if(genderTxt.getSelectionModel().getSelectedItem() == null){
            gender = Gender.None;
        }
        else{
            gender = convertToGender(genderTxt.getSelectionModel().getSelectedItem().toString());
        }
        String phoneNr = phoneTxt.getText();
        String mail = mailTxt.getText();
        return new Candidate(name, gender, phoneNr, mail);
    }
    private Candidate getCandidateForUpdate(){
        String name = nameTxt.getText();
        Gender gender = convertToGender(genderTxt.getSelectionModel().getSelectedItem().toString());
        String phoneNr = phoneTxt.getText();
        String mail = mailTxt.getText();
        return new Candidate(this.candidate.getId(), name, gender, phoneNr, mail);
    }


    private Gender convertToGender(String gender){
        if(gender.equals("Masculin")){
            return Gender.M;
        }
        else{
            return Gender.F;
        }
    }

}
