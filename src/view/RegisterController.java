package view;

import domain.Candidate;
import domain.Option;
import domain.Section;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import services.GeneralService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


class XCellRegisterController extends ListCell<String> {
    HBox hbox = new HBox();
    Label label = new Label("");
    Pane pane = new Pane();
    Button button = new Button();
    RegisterController registerController;


    public XCellRegisterController(RegisterController registerController) {
        super();
        this.registerController = registerController;
        ImageView imageView = new ImageView(new Image("/images/delete-icon.png"));
        imageView.setFitWidth(15);
        imageView.setFitHeight(15);
        button.setGraphic(imageView);
        button.getStylesheets().add("/view/ButtonStyle.css");
        hbox.getChildren().addAll(label, pane, button);
        HBox.setHgrow(pane, Priority.ALWAYS);
        button.setOnAction(event -> {
            Optional<ButtonType> result = MessageAlert.showConfirmation(null, "Confirmare", "Esti sigur ca vrei sa stergi optiunea selectata ?");
            if(result.get() == ButtonType.OK) {
                String row = getListView().getItems().get(getIndex());
                String[] fields = row.split("\\.");
                Section section = this.registerController.getGeneralService().getSectionService().getSectieByName(fields[1]);
                Candidate candidate = (Candidate) this.registerController.getCandidate();
                Option optionToDelete = this.registerController.getGeneralService().getOptionService().getOptionByCandidateIdAndSection(candidate.getId(), section.getId());
                if(this.registerController.getGeneralService().getOptionService().delete(optionToDelete.getId()) != null){
                    this.registerController.getGeneralService().maintenancePriority(candidate.getId());
                    List<Option> list = this.registerController.getGeneralService().getOptionsByCandidateId(candidate.getId());
                    this.registerController.getOptionsPagination().setPageCount(list.size() / registerController.rowsOptionsPerPage() + 1);
                    this.registerController.getOptionsPagination().setPageFactory(registerController::createPageOptions);
                }
                else{
                    MessageAlert.showErrorMessage(null, "A intervenit o eroare ! Optiunea nu a putut fi stearsa !");
                }
            }
        });
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);
        setGraphic(null);

        if (item != null && !empty) {
            label.setText(item);
            setGraphic(hbox);
        }
    }
}



public class RegisterController {
    private GeneralService service;
    private Candidate candidate;

    @FXML
    Label candidateName;

    @FXML
    Label candidateGender;

    @FXML
    Label candidatePhone;

    @FXML
    Label candidateMail;

    @FXML
    ListView optionsView;

    @FXML
    TableView sectionsTable;

    @FXML
    TableColumn<Section, String> sectionNameColumn;

    @FXML
    TableColumn<Section, String> sectionNrPlacesColumn;

    @FXML
    TextField filterField;

    @FXML
    ComboBox filterBox;

    @FXML
    Pagination optionsPagination;

    @FXML
    Pagination sectionPagination2;

    @FXML
    public void initialize(){
        optionsView.setCellFactory(param -> new XCellRegisterController(this));
    }

    @FXML
    public void handleAddOption(MouseEvent ev){
        showAddOptionView(this.candidate);
        optionsPagination.setPageCount(service.getOptionsByCandidateId(candidate.getId()).size() / rowsOptionsPerPage() + 1);
        optionsPagination.setPageFactory(this::createPageOptions);

    }

    @FXML
    public void handleFilter(KeyEvent keyEvent){
        if(!filterField.getText().equals("")){
            ObservableList<Section> model = FXCollections.observableArrayList(filterTag());
            sectionsTable.setItems(model);
        }
        else{
            sectionsTable.getItems().setAll(service.getSectionService().getAll());
            sectionPagination2.setPageFactory(this::createPage);
        }
    }

    public void showAddOptionView(Candidate candidat){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/AddOptionView.fxml"));
        Pane root = null;
        try {
            root = (Pane)loader.load();
            Stage stage = new Stage();
            stage.setTitle("Inscriere optiune");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.getIcons().add(new Image("/images/icon-app.png"));
            Scene newScene = new Scene(root);
            stage.setScene(newScene);
            AddOptionController addOptionController = loader.getController();
            addOptionController.setCandidate(candidat);
            addOptionController.setService(service, stage);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int rowsPerPage() {
        return 6;
    }

    public int rowsOptionsPerPage(){
        return 3;
    }

    public Node createPage(int pageIndex) {

        int fromIndex = pageIndex * rowsPerPage();
        int toIndex = Math.min(fromIndex + rowsPerPage(), service.getSectionService().getAll().size());
        sectionsTable.setItems(FXCollections.observableList(service.getSectionService().getAll().subList(fromIndex, toIndex)));

        return new VBox(sectionsTable);
    }

    public Node createPageOptions(int pageIndex) {

        int fromIndex = pageIndex * rowsOptionsPerPage();
        int toIndex = Math.min(fromIndex + rowsOptionsPerPage(), service.getOptionsByCandidateId(candidate.getId()).size());
        optionsView.setItems(FXCollections.observableList(optionsToStringList(service.getOptionsByCandidateId(candidate.getId())).subList(fromIndex, toIndex)));
        return new VBox(optionsView);
    }

    private List<String> optionsToStringList(List<Option> options){
        List<String> list = new ArrayList<>();
        for(Option option: options) {
            Section section = service.getSectionService().getSectieById(option.getIdSection());
            list.add(Short.toString(option.getPriority()) + "." + section.getName());
        }
        return list;
    }

    private List<Section> filterTag(){
        String choice = (String)filterBox.getSelectionModel().getSelectedItem();
        if(choice != null){
            switch (choice){
                case "Nume":
                    return service.getSectionService().filterByName(filterField.getText());
                case "Locuri":
                   // return service.getCandidateService().filterByGender(filterField.getText()); TODO

            }
        }
        return service.getSectionService().getAll();
    }

    private void setCandidateData(){
        this.candidateName.setText(this.candidate.getName());
        this.candidateGender.setText(this.candidate.getSex().toString());
        this.candidatePhone.setText(this.candidate.getPhoneNr());
        this.candidateMail.setText(this.candidate.getMail());
    }


    public void setService(GeneralService generalService){
        this.service = generalService;

        sectionNameColumn.setCellValueFactory(new PropertyValueFactory<Section, String>("name"));
        sectionNrPlacesColumn.setCellValueFactory(new PropertyValueFactory<Section, String>("freePlaces"));


        sectionsTable.setMinHeight(250);
        sectionPagination2.setPageCount(service.getSectionService().getAll().size() / rowsPerPage() + 1);
        sectionPagination2.setCurrentPageIndex(0);
        sectionPagination2.setPageFactory(this::createPage);

        filterBox.getItems().addAll("Nume", "> Nr locuri");
        optionsView.setMinHeight(125);
        optionsPagination.setPageCount(service.getOptionsByCandidateId(candidate.getId()).size() / rowsOptionsPerPage() + 1);
        optionsPagination.setCurrentPageIndex(0);
        optionsPagination.setPageFactory(this::createPageOptions);
    }

    public void setCandidate(Candidate candidate){
        this.candidate = candidate;
        setCandidateData();
    }

    public GeneralService getGeneralService(){
        return this.service;
    }

    public Candidate getCandidate(){
        return this.candidate;
    }

    public Pagination getOptionsPagination(){
        return this.optionsPagination;
    }
}
