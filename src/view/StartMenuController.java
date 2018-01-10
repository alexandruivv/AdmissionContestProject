package view;

import domain.Candidate;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import repositories.CandidateFileRepository;
import repositories.OptionFileRepository;
import repositories.SectionFileRepository;
import services.CandidateService;
import services.OptionService;
import services.SectionService;
import utils.User;
import utils.UserType;
import validators.CandidateValidator;
import validators.OptionValidator;
import validators.SectionValidator;

import java.io.IOException;


public class StartMenuController {

    //services
    private OptionService optionService;
    private CandidateService candidateService;
    private SectionService sectionService;

    private HomeController homeController;

    //panes
    @FXML
    private Pane windowContent;

    private Pane homeView;
    private Pane candidateView;
    private Pane sectionView;
    private Pane reportView;
    private Pane contactView;

    private Node currentWindow;

    //log in information
    private User user;
    private Candidate candidate;

    //stages
    private Stage logInStage;
    private Stage currentStage;

    //button's components
    @FXML
    HBox homeButton;

    @FXML
    HBox candidatesButton;

    @FXML
    HBox sectionButton;

    @FXML
    HBox reportsButton;

    @FXML
    HBox contactButton;

    @FXML
    Pane homeBar;

    @FXML
    Pane candidatesBar;

    @FXML
    Pane sectionsBar;

    @FXML
    Pane reportsBar;

    @FXML
    Pane contactBar;
    //search
    @FXML
    TextField searchBar;

    //log data
    @FXML
    Text nameUser;

    @FXML
    Text userType;

    @FXML
    public void handleHomeButton(MouseEvent ev){

        disableCurrentButton();

        this.windowContent.getChildren().remove(currentWindow);

        this.currentWindow = homeView;

        this.windowContent.getChildren().add(currentWindow);

        mouseEnteredOnButton(homeButton, homeBar);
        homeController.refreshSignal();
    }

    @FXML
    public void handleCandidateButton(MouseEvent ev){

        disableCurrentButton();

        this.windowContent.getChildren().remove(currentWindow);

        this.currentWindow = candidateView;

        this.windowContent.getChildren().add(currentWindow);

        mouseEnteredOnButton(candidatesButton, candidatesBar);

    }

    @FXML
    public void handleSectionButton(MouseEvent event){
        disableCurrentButton();

        this.windowContent.getChildren().remove(currentWindow);

        this.currentWindow = sectionView;

        this.windowContent.getChildren().add(currentWindow);

        mouseEnteredOnButton(sectionButton, sectionsBar);
    }

    @FXML
    public void handleReportButton(MouseEvent event){
        disableCurrentButton();

        this.windowContent.getChildren().remove(currentWindow);

        this.currentWindow = reportView;

        this.windowContent.getChildren().add(currentWindow);

        mouseEnteredOnButton(reportsButton, reportsBar);
    }

    @FXML
    public void handleContactButton(MouseEvent event){
        disableCurrentButton();

        this.windowContent.getChildren().remove(currentWindow);

        this.currentWindow = contactView;

        this.windowContent.getChildren().add(currentWindow);

        mouseEnteredOnButton(contactButton, contactBar);
    }

    //on entered
    @FXML
    public void handleMouseOnHomeButton(MouseEvent ev){
        mouseEnteredOnButton(homeButton, homeBar);
    }

    @FXML
    public void handleMouseOnCandidateButton(MouseEvent ev){
        mouseEnteredOnButton(candidatesButton, candidatesBar);
    }

    @FXML
    public void handleMouseOnSectionButton(MouseEvent ev){
        mouseEnteredOnButton(sectionButton, sectionsBar);
    }

    @FXML
    public void handleMouseOnReportButton(MouseEvent ev){
        mouseEnteredOnButton(reportsButton, reportsBar);
    }

    @FXML
    public void handleMouseOnContactButton(MouseEvent ev){
        mouseEnteredOnButton(contactButton, contactBar);
    }


    //on exited
    @FXML
    public void handleMouseExitedHomeButton(MouseEvent ev){
        if(this.currentWindow != homeView) {
            mouseExitedButton(homeButton, homeBar);
        }
    }

    @FXML
    public void handleMouseExitedCandidateButton(MouseEvent ev){
        if(this.currentWindow != candidateView) {
            mouseExitedButton(candidatesButton, candidatesBar);
        }
    }

    @FXML
    public void handleMouseExitedSectionButton(MouseEvent ev){
        if(this.currentWindow != sectionView) {
            mouseExitedButton(sectionButton, sectionsBar);
        }
    }

    @FXML
    public void handleMouseExitedReportButton(MouseEvent ev){
        if(this.currentWindow != reportView) {
            mouseExitedButton(reportsButton, reportsBar);
        }
    }

    @FXML
    public void handleMouseExitedContactButton(MouseEvent ev){
        if(this.currentWindow != contactView) {
            mouseExitedButton(contactButton, contactBar);
        }
    }

    @FXML
    public void handleLogOut(MouseEvent ev){
        this.currentStage.close();
        this.logInStage.show();
    }

    private void disableCurrentButton(){
        if(this.currentWindow == homeView){
            mouseExitedButton(homeButton, homeBar);
        }
        else if(this.currentWindow == candidateView){
            mouseExitedButton(candidatesButton, candidatesBar);
        }
        else if(this.currentWindow == sectionView){
            mouseExitedButton(sectionButton, sectionsBar);
        }
        else if(this.currentWindow == reportView){
            mouseExitedButton(reportsButton, reportsBar);
        }
        else if(this.currentWindow == contactView){
            mouseExitedButton(contactButton, contactBar);
        }
    }

    public void setServices(OptionService optionService, CandidateService candidateService, SectionService sectionService){
        this.optionService = optionService;
        this.candidateService = candidateService;
        this.sectionService = sectionService;
    }

    public void setCurrentUser(User user){
        this.user = user;
        this.candidate = getCandidateByUser(user);
        setDataForUserLoggedIn(candidate, user);
    }

    public void setPane(Pane homeView, Pane candidateView, Pane sectionView, Pane reportsView, Pane contactView){
        this.currentWindow = homeView;

        this.homeView = homeView;
        this.candidateView = candidateView;
        this.sectionView = sectionView;
        this.reportView = reportsView;
        this.contactView = contactView;

        this.windowContent.getChildren().add(currentWindow);

        mouseEnteredOnButton(homeButton, homeBar);

        searchBar.setFocusTraversable(false);
    }

    public void setLogInAndCurrentStage(Stage logInStage, Stage currentStage){
        this.logInStage = logInStage;
        this.currentStage = currentStage;
    }

    private void mouseEnteredOnButton(HBox button, Pane borderButton){
        button.setOpacity(1.0);
        borderButton.setVisible(true);
    }

    private void mouseExitedButton(HBox button, Pane borderButton){
        button.setOpacity(0.5);
        borderButton.setVisible(false);
    }

    private Candidate getCandidateByUser(User user){
        return candidateService.getCandidateByMail(user.getUserMail());
    }

    private void setDataForUserLoggedIn(Candidate candidate, User user){
        if(candidate != null) {
            this.nameUser.setText(candidate.getName());
            this.userType.setText(user.getUserType().toString());
        }
        else{
            this.nameUser.setText("Admin");
            this.userType.setText(UserType.Admin.toString());
        }
    }

    public void setHomeController(HomeController homeController){
        this.homeController = homeController;
    }
}
