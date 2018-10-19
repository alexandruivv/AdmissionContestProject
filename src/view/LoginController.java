package view;

import domain.Candidate;
import domain.Gender;
import domain.ValidationException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import services.GeneralService;
import utils.User;
import utils.UserType;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Properties;
import java.util.UUID;

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
    Button registerButton;

    @FXML
    Pane sendMailPane;

    @FXML
    TextField mailToSend;

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
        mailToSend.setStyle("-fx-text-inner-color: white;-fx-background-color:  #26004d;-fx-border-color:  #666666");

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

    @FXML
    public void handleEnterLogin(KeyEvent ev){
        if(ev.getCode() == KeyCode.ENTER){
            attemptLogin();
        }
    }

    @FXML
    public void handleEnterRegister(KeyEvent ev){
        if(ev.getCode() == KeyCode.ENTER){
            saveUser(getRegisterData());
        }
    }

    @FXML
    public void handleEnterSendMail(KeyEvent ev){
        if(ev.getCode() == KeyCode.ENTER){
            attemptSendMail();
        }
    }

    @FXML
    public void handleSendMail(ActionEvent ev){
        attemptSendMail();
    }

    @FXML
    public void handleSetVisibleMailSend(MouseEvent ev){
        mailToSend.clear();
        sendMailPane.setVisible(true);
    }


    public void attemptSendMail(){
        String mailToSendd = mailToSend.getText();
        User user = service.getUserService().getUserByMail(mailToSendd);
        if(user != null){
            sendMail(user);
        }
        else{
            MessageAlert.showErrorMessage(null, "Nu exista cont cu mail-ul introdus !");
        }
        sendMailPane.setVisible(false);
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


                if(validatePassword(password, correctPassword)){
                    loginIn(user);
                    clearLoginFields();
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
                service.getUserService().save(new User(mail, generateStrongPasswordHash(password), UserType.Candidate));
                clearRegisterFields();
                setVisibleSignUp(false);
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Salvat !", "Cont inregistrat ! Te poti loga !");
            }
            else{
                MessageAlert.showErrorMessage(null, "Mail-ul este folosit de alt candidat !");
            }
        } catch (ValidationException e) {
            MessageAlert.showErrorMessage(null, e.toString());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }

    private void sendMail(User toUser){
        try {
            String randomString = generateRandomString();
            String to = toUser.getUserMail();
            String subject = "Parola Admitere Concurs";
            String msg = "Parola ta este: " + randomString;
            final String from = "admitereubb@gmail.com";
            final String password = "larlarlar98";


            Properties props = new Properties();
            props.setProperty("mail.transport.protocol", "smtp");
            props.setProperty("mail.host", "smtp.gmail.com");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "465");
            props.put("mail.debug", "true");
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.socketFactory.fallback", "false");
            Session session = Session.getDefaultInstance(props,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(from, password);
                        }
                    });

            //session.setDebug(true);
            Transport transport = session.getTransport();
            InternetAddress addressFrom = new InternetAddress(from);

            MimeMessage message = new MimeMessage(session);
            message.setSender(addressFrom);
            message.setSubject(subject);
            message.setContent(msg, "text/plain");
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            transport.connect();
            Transport.send(message);
            transport.close();

            toUser.setPassword(generateStrongPasswordHash(randomString));
            service.getUserService().update(toUser);
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Trimis !", "Parola a fost resetata si trimisa pe mail !");
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (ValidationException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }

    private String generateRandomString(){
        String uuid = UUID.randomUUID().toString();
        uuid = uuid.replace("-", "");
        return uuid.substring(0, 15);
    }

    public static String generateStrongPasswordHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        int iterations = 1000;
        char[] chars = password.toCharArray();
        byte[] salt = getSalt();

        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return iterations + ":" + toHex(salt) + ":" + toHex(hash);
    }

    public static byte[] getSalt() throws NoSuchAlgorithmException
    {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

    public static String toHex(byte[] array) throws NoSuchAlgorithmException
    {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0)
        {
            return String.format("%0"  +paddingLength + "d", 0) + hex;
        }else{
            return hex;
        }
    }

    public static boolean validatePassword(String originalPassword, String storedPassword)
    {
        try {
            String[] parts = storedPassword.split(":");
            int iterations = Integer.parseInt(parts[0]);
            byte[] salt = fromHex(parts[1]);
            byte[] hash = fromHex(parts[2]);

            PBEKeySpec spec = new PBEKeySpec(originalPassword.toCharArray(), salt, iterations, hash.length * 8);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] testHash = skf.generateSecret(spec).getEncoded();

            int diff = hash.length ^ testHash.length;
            for (int i = 0; i < hash.length && i < testHash.length; i++) {
                diff |= hash[i] ^ testHash[i];
            }
            return diff == 0;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return false;
    }
    public static byte[] fromHex(String hex) throws NoSuchAlgorithmException
    {
        byte[] bytes = new byte[hex.length() / 2];
        for(int i = 0; i<bytes.length ;i++)
        {
            bytes[i] = (byte)Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
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

    private void clearLoginFields(){
        loginUser.clear();
        loginPassword.clear();
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

            FXMLLoader passwordLoader = new FXMLLoader();
            passwordLoader.setLocation(getClass().getResource("/view/ChangePasswordView.fxml"));
            Pane passwordView = (Pane) passwordLoader.load();
            ChangePasswordController changePasswordController = passwordLoader.getController();
            changePasswordController.setUser(user);

            FXMLLoader mainLoader = new FXMLLoader();
            mainLoader.setLocation(getClass().getResource("/view/CandidateStartMenuView.fxml"));
            BorderPane mainView = (BorderPane) mainLoader.load();
            CandidateStartMenuController mainController = mainLoader.getController();

            mainController.setService(service);
            mainController.setCurrentUser(user);
            mainController.setPane(registerView, contactView, passwordView);


            registerController.setCandidate(service.getCandidateService().getCandidateByMail(user.getUserMail()), user);
            registerController.setService(service);


            changePasswordController.setService(service);


            Stage stage = new Stage();
            stage.setScene(new Scene(mainView));
            registerController.setStage(stage);
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

            FXMLLoader passwordLoader = new FXMLLoader();
            passwordLoader.setLocation(getClass().getResource("/view/ChangePasswordView.fxml"));
            Pane passwordView = (Pane) passwordLoader.load();
            ChangePasswordController changePasswordController = passwordLoader.getController();
            changePasswordController.setUser(user);


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
            mainController.setPane(homeView, candidateView, sectionView, reportsView, contactView, passwordView);

            homeController.setService(service);
            homeController.setMainController(mainController);

            reportsController.setService(service);

            changePasswordController.setService(service);

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
