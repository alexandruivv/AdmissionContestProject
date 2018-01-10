package view;

import domain.Candidate;
import domain.Gender;
import domain.ValidationException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import services.GeneralService;
import utils.User;
import utils.UserType;

import java.io.IOException;

public class LoginController {
    private GeneralService service;

    private Stage primaryStage;

    @FXML
    Pane registerPane;

    @FXML
    TextField registerNameField;

    @FXML
    TextField registerPhoneField;

    @FXML
    TextField registerMailField;

    @FXML
    ComboBox registerGenderBox;

    @FXML
    PasswordField registerPasswordField;

    @FXML
    TextField loginUser;

    @FXML
    PasswordField loginPassword;

    @FXML
    public void initialize(){
        formatFields();
    }

    private void formatFields(){
        registerGenderBox.getItems().setAll("Masculin", "Feminin");
        loginUser.setStyle("-fx-text-inner-color: white;-fx-background-color:  #26004d;-fx-border-color:  #666666");
        loginPassword.setStyle("-fx-text-inner-color: white;-fx-background-color:  #26004d;-fx-border-color:  #666666");
        registerNameField.setStyle("-fx-text-inner-color: white;-fx-background-color:  #26004d;-fx-border-color:  #666666");
        registerPhoneField.setStyle("-fx-text-inner-color: white;-fx-background-color:  #26004d;-fx-border-color:  #666666");
        registerMailField.setStyle("-fx-text-inner-color: white;-fx-background-color:  #26004d;-fx-border-color:  #666666");
        registerPasswordField.setStyle("-fx-text-inner-color: white;-fx-background-color:  #26004d;-fx-border-color:  #666666");
    }

    @FXML
    public void handleSetVisibleSignUp(ActionEvent ev){
        setVisibleSignUp(true);
    }

    @FXML
    public void handleLogin(ActionEvent ev){
        attemptLogin();
    }

    @FXML
    public void handleRegister(ActionEvent ev){
        saveUser(getRegisterData());
    }

    public void setServiceAndStage(GeneralService service, Stage stage){
        this.service = service;
        this.primaryStage = stage;
    }

    private void attemptLogin(){
        String userMail = loginUser.getText();
        String password = loginPassword.getText();
        User user = service.getUserService().getUserByMail(userMail);
        if(user != null){
            String correctPassword = user.getPassword();
            if(correctPassword.equals(password)){
                loginIn(user);
            }
            else{
                MessageAlert.showErrorMessage(null, "Utilizator si parola incorecte !");
            }
        }
        else{
            MessageAlert.showErrorMessage(null, "Contul nu exista !");
        }
    }

    private void loginIn(User user){
        if(user.getUserType().equals(UserType.Admin)){
            loginAsAdmin(user);
        }
        else{
            loginAsCandidate(user);
        }
    }

    private void saveUser(Candidate candidate){
        try {
            if(service.getCandidateService().save(candidate) == null){
                String mail = candidate.getMail();
                String password = registerPasswordField.getText();
                service.getUserService().save(new User(mail, password, UserType.Candidate));
                clearRegisterFields();
                setVisibleSignUp(false);
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Salvat !", "Cont inregistrat ! Te poti loga !");
            }
            else{
                MessageAlert.showErrorMessage(null, "Mail-ul este folosit de alt candidat !");
            }
        } catch (ValidationException e) {
            MessageAlert.showErrorMessage(null, e.toString());
        }
    }

    private Candidate getRegisterData(){
        String name = registerNameField.getText();
        Gender gender = convertToGender(registerGenderBox.getSelectionModel().getSelectedItem());
        String phoneNr = registerPhoneField.getText();
        String mail = registerMailField.getText();
        return new Candidate(name, gender, phoneNr, mail);
    }

    private Gender convertToGender(Object o){
        if(o == null){
            return Gender.None;
        }
        String gender = (String) o;
        if(gender.equals("Masculin")){
            return Gender.M;
        }
        else{
            return Gender.F;
        }
    }

    private void clearRegisterFields(){
        registerNameField.setText("");
        registerGenderBox.setValue(null);
        registerGenderBox.setPromptText("Sex");
        registerPhoneField.setText("");
        registerMailField.setText("");
    }


    private void setVisibleSignUp(boolean bool){
        registerPane.setVisible(bool);
    }

    private void loginAsCandidate(User user){
        try{
            FXMLLoader registerLoader = new FXMLLoader();
            registerLoader.setLocation(getClass().getResource("/view/RegisterView.fxml"));
            Pane registerView = (Pane) registerLoader.load();
            RegisterController registerController = registerLoader.getController();

            FXMLLoader contactLoader = new FXMLLoader();
            contactLoader.setLocation(getClass().getResource("/view/ContactView.fxml"));
            Pane contactView = (Pane) contactLoader.load();
            ContactController contactController = contactLoader.getController();

            FXMLLoader mainLoader = new FXMLLoader();
            mainLoader.setLocation(getClass().getResource("/view/CandidateStartMenuView.fxml"));
            BorderPane mainView = (BorderPane) mainLoader.load();
            CandidateStartMenuController mainController = mainLoader.getController();

            mainController.setService(service);
            mainController.setCurrentUser(user);
            mainController.setPane(registerView, contactView);

            registerController.setCandidate(service.getCandidateService().getCandidateByMail(user.getUserMail()));
            registerController.setService(service);


            Stage stage = new Stage();
            stage.setScene(new Scene(mainView));
            stage.setTitle("Concurs admitere");
            stage.getIcons().add(new Image("/images/icon-app.png"));
            mainController.setLogInAndCurrentStage(primaryStage, stage);
            primaryStage.close();
            stage.show();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loginAsAdmin(User user){
        try {
            FXMLLoader candidateLoader = new FXMLLoader();
            candidateLoader.setLocation(getClass().getResource("/view/CandidateView.fxml"));
            Pane candidateView = (Pane) candidateLoader.load();
            CandidateController candidateController = candidateLoader.getController();

            FXMLLoader sectionLoader = new FXMLLoader();
            sectionLoader.setLocation(getClass().getResource("/view/SectionView.fxml"));
            Pane sectionView = (Pane) sectionLoader.load();
            SectionController sectionController = sectionLoader.getController();

            FXMLLoader homeLoader = new FXMLLoader();
            homeLoader.setLocation(getClass().getResource("/view/HomeView.fxml"));
            Pane homeView = (Pane) homeLoader.load();
            HomeController homeController = homeLoader.getController();

            FXMLLoader reportsLoader = new FXMLLoader();
            reportsLoader.setLocation(getClass().getResource("/view/ReportsView.fxml"));
            Pane reportsView = (Pane) reportsLoader.load();
            ReportsController reportsController = reportsLoader.getController();

            FXMLLoader contactLoader = new FXMLLoader();
            contactLoader.setLocation(getClass().getResource("/view/ContactView.fxml"));
            Pane contactView = (Pane) contactLoader.load();

            FXMLLoader mainLoader = new FXMLLoader();
            mainLoader.setLocation(getClass().getResource("/view/StartMenuView.fxml"));
            Pane mainView = (Pane) mainLoader.load();
            StartMenuController mainController = mainLoader.getController();


            service.getCandidateService().addObserver(candidateController);
            service.getSectionService().addObserver(sectionController);

            candidateController.setServices(service);
            sectionController.setServices(service);

            mainController.setServices(service.getOptionService(), service.getCandidateService(), service.getSectionService());
            mainController.setHomeController(homeController);
            mainController.setPane(homeView, candidateView, sectionView, reportsView, contactView);

            homeController.setService(service);
            homeController.setMainController(mainController);

            reportsController.setService(service);

            Stage stage = new Stage();
            stage.setScene(new Scene(mainView));
            stage.setTitle("Concurs admitere");
            stage.getIcons().add(new Image("/images/icon-app.png"));
            mainController.setLogInAndCurrentStage(primaryStage, stage);
            primaryStage.close();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
