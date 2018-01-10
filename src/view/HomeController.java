package view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import services.GeneralService;

public class HomeController {
    private GeneralService service;
    private StartMenuController mainController;


    @FXML
    Label candidatesTopText;

    @FXML
    Text candidatesBotText;

    @FXML
    Label sectionsTopText;

    @FXML
    Text sectionsBotText;

    @FXML
    Label optionsTopText;

    @FXML
    Text optionsBotText;

    public void setService(GeneralService service){
        this.service = service;
        refreshEvidence();
    }

    public void refreshSignal(){
        refreshEvidence();
    }

    public void setMainController(StartMenuController mainController){
        this.mainController = mainController;
    }

    @FXML
    public void goToCandidatesTab(MouseEvent ev){
        mainController.handleCandidateButton(ev);
    }

    @FXML
    public void goToSectionsTab(MouseEvent ev){
        mainController.handleSectionButton(ev);
    }

    @FXML
    public void goToOptionsTab(MouseEvent ev){
        mainController.handleCandidateButton(ev);
    }

    private void refreshEvidence(){
        int nrCandidates = service.getCandidateService().getAll().size();
        int nrSections = service.getSectionService().getAll().size();
        int nrOptions = service.getOptionService().getAll().size();

        candidatesTopText.setText(Integer.toString(nrCandidates));
        candidatesBotText.setText(candidatesTopText.getText() + " candidati inscrisi");

        sectionsTopText.setText(Integer.toString(nrSections));
        sectionsBotText.setText(sectionsTopText.getText() + " sectii disponibile");

        optionsTopText.setText(Integer.toString(nrOptions));
        optionsBotText.setText(optionsTopText.getText() + " optiuni inregistrate");
    }
}
