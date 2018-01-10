package view;

import domain.Section;
import domain.ValidationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import services.Service;

public class AddSectionController {
    private Service<Integer, Section> service;
    private Stage stage;
    private Section section;


    @FXML
    TextField nameTxt;

    @FXML
    TextField nrPlacesTxt;


    @FXML
    public void handleSave(MouseEvent ev){
        try {
            if(this.section != null){
                service.update(getSectionUpdate(section.getId()));
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Modificat !", "Sectie modificata cu succes !");
            }
            else{
                service.save(getSection());
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Adaugat !", "Sectie adaugata cu succes !");
            }
            stage.close();
        } catch (ValidationException e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }

    public void setService(Service service, Section section, Stage stage){
        this.service = service;
        this.stage = stage;
        this.section = section;

        if(section != null){
            setFields(section);
        }
    }

    private Section getSectionUpdate(int sectionId){
        String name = nameTxt.getText();
        int nrFreePlaces = Integer.parseInt(nrPlacesTxt.getText());
        return new Section(sectionId, name, nrFreePlaces);
    }

    private Section getSection(){
        String name = nameTxt.getText();
        int nrFreePlaces = Integer.parseInt(nrPlacesTxt.getText());
        return new Section(name, nrFreePlaces);
    }


    private void setFields(Section section){
        nameTxt.setText(section.getName());
        nrPlacesTxt.setText(Integer.toString(section.getFreePlaces()));
    }


}
