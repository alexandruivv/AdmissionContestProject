package view;

import domain.Candidate;
import domain.Option;
import domain.Section;
import domain.ValidationException;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;
import repositories.CandidateFileRepository;
import repositories.OptionFileRepository;
import repositories.SectionFileRepository;
import services.*;
import validators.CandidateValidator;
import validators.OptionValidator;
import validators.SectionValidator;

import java.util.ArrayList;


public class AddOptionController {

    private GeneralService service;
    private Candidate candidate;
    private Stage stage;

    @FXML
    ListView sectionList;


    @FXML
    public void handleAddOptionButton(){
        Section section = (Section) sectionList.getSelectionModel().getSelectedItem();
        if(section == null){
            MessageAlert.showErrorMessage(null, "Selectati o sectie pentru inscriere !");
        }
        else {

            try {
                service.getOptionService().save(new Option(candidate.getId(), section.getId(), service.getPriority(candidate.getId())));

            } catch (ValidationException e) {
                MessageAlert.showErrorMessage(null, e.getMessage());
            }

            stage.close();
        }
    }


    public void setService(GeneralService service, Stage stage){
        this.service = service;
        this.stage = stage;
        sectionList.getItems().setAll(service.getSectionsNotUsedByCandidate(candidate.getId()));
    }

    public void setCandidate(Candidate candidate){
        this.candidate = candidate;
    }

}
