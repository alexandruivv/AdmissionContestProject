package view;

import domain.Candidate;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import services.GeneralService;
import utils.User;

public class CandidateStartMenuController {
    //services
    private GeneralService service;


    //panes
    @FXML
    private Pane windowContent;

    private Pane registerView;
    private Pane contactView;

    private Node currentWindow;

    //log in information
    private Candidate candidate;

    //stages
    private Stage logInStage;
    private Stage currentStage;

    //button's components
    @FXML
    HBox registerButton;

    @FXML
    HBox contactButton;

    @FXML
    Pane registerBar;

    @FXML
    Pane contactBar;

    //search
    @FXML
    TextField searchBar;

    //log data
    @FXML
    Text nameUser;


    @FXML
    public void handleRegisterButton(MouseEvent ev){

        disableCurrentButton();

        this.windowContent.getChildren().remove(currentWindow);

        this.currentWindow = registerView;

        this.windowContent.getChildren().add(currentWindow);

        mouseEnteredOnButton(registerButton, registerBar);
    }

    @FXML
    public void handleContactButton(MouseEvent ev){

        disableCurrentButton();

        this.windowContent.getChildren().remove(currentWindow);

        this.currentWindow = contactView;

        this.windowContent.getChildren().add(currentWindow);

        mouseEnteredOnButton(contactButton, contactBar);

    }

    //on entered

    @FXML
    public void handleMouseOnRegisterButton(MouseEvent ev){
        mouseEnteredOnButton(registerButton, registerBar);
    }

    @FXML
    public void handleMouseOnContactButton(MouseEvent ev){
        mouseEnteredOnButton(contactButton, contactBar);
    }

    //on exited
    @FXML
    public void handleMouseExitedRegisterButton(MouseEvent ev){
        if(this.currentWindow != registerView) {
            mouseExitedButton(registerButton, registerBar);
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
        if(this.currentWindow == registerView){
            mouseExitedButton(registerButton, registerBar);
        }
        else if(this.currentWindow == contactView){
            mouseExitedButton(contactButton, contactBar);
        }
    }

    public void setService(GeneralService service){
        this.service = service;
    }

    public void setCurrentUser(User user){
        this.candidate = getCandidateByUser(user);
        setDataForUserLoggedIn(candidate);
    }

    public void setPane(Pane registerView, Pane contactView){
        this.currentWindow = registerView;

        this.registerView = registerView;
        this.contactView = contactView;


        this.windowContent.getChildren().add(currentWindow);

        mouseEnteredOnButton(registerButton, registerBar);

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
        return service.getCandidateService().getCandidateByMail(user.getUserMail());
    }

    private void setDataForUserLoggedIn(Candidate candidate){
        this.nameUser.setText(candidate.getName());
    }
}
