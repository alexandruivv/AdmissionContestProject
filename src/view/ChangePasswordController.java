package view;

import domain.ValidationException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import services.GeneralService;
import utils.User;


import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class ChangePasswordController {
    private GeneralService service;
    private User user;

    @FXML
    PasswordField currentPassword;

    @FXML
    PasswordField newPassword;

    @FXML
    public void handleSavePassword(ActionEvent ev){
        attemptSavePassword();
    }

    private void attemptSavePassword(){
        String password = currentPassword.getText();
        String newPass = newPassword.getText();


        if(LoginController.validatePassword(password, this.user.getPassword())){
            try {
                user.setPassword(LoginController.generateStrongPasswordHash(newPass));
                service.getUserService().update(user);
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Salvat !", "Parola salvata cu succes !");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            } catch (ValidationException e) {
                e.printStackTrace();
            }
        }
        else{
            MessageAlert.showErrorMessage(null, "Parola curenta a contului este gresita !");
        }

        clearFields();
    }

    private void clearFields(){
        this.currentPassword.clear();
        this.newPassword.clear();
    }

    public void setUser(User user){
        this.user = user;
    }

    public void setService(GeneralService service){
        this.service = service;
    }
}
