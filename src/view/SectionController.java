package view;

import domain.Candidate;
import domain.Section;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import repositories.CandidateFileRepository;
import repositories.OptionFileRepository;
import repositories.SectionFileRepository;
import services.CandidateService;
import services.GeneralService;
import services.OptionService;
import services.SectionService;
import utils.ListEvent;
import utils.Observer;
import validators.CandidateValidator;
import validators.OptionValidator;
import validators.SectionValidator;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


public class SectionController implements Observer<Section> {

    private GeneralService service;

    @FXML
    TableView sectionsTable;

    @FXML
    TableColumn sectionNameColumn;

    @FXML
    TableColumn sectionPlacesColumn;

    @FXML
    TableColumn sectionDeleteButton;

    @FXML
    TableColumn sectionUpdateButton;

    @FXML
    TableView candidatesTable;

    @FXML
    TableColumn candidateNameColumn;

    @FXML
    TableColumn candidateGenderColumn;

    @FXML
    TableColumn candidatePhoneColumn;

    @FXML
    TableColumn candidateMailColumn;

    @FXML
    TextField fieldFilter;

    @FXML
    Pagination candidatesPagination;

    @FXML
    Pagination sectionsPagination;

    @FXML
    public void initialize(){
        candidatesTable.getStylesheets().add("/view/TableViewStyle.css");
        candidatesTable.setMinHeight(277);
        sectionsTable.getStylesheets().add("/view/TableViewStyle.css");
        sectionsTable.setMinHeight(224);
    }


    @FXML
    public void handleFilter(KeyEvent keyEvent){
        try {
            String sectionName = fieldFilter.getText();
            if (!sectionName.equals("")) {
                ObservableList<Section> model = FXCollections.observableArrayList(service.getSectionService().filterByName(sectionName));
                sectionsTable.setItems(model);
            } else {
                sectionsTable.getItems().setAll(service.getSectionService().getAll());
                sectionsPagination.setPageFactory(this::createSectionPage);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    public void handleAddButton(MouseEvent ev){
        showAddSection(null, "Adaugare sectie");
    }

    @FXML
    public void populateCandidatesTable(MouseEvent ev){
        candidatesPagination.setPageCount(service.getCandidateService().size() / rowsPerCandidatePage() + 1);
        candidatesPagination.setCurrentPageIndex(0);
        candidatesPagination.setPageFactory(this::createCandidatePage);
    }



    private void showAddSection(Section section, String title){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/AddSectionView.fxml"));
        Pane root = null;
        try {
            root = (Pane)loader.load();
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.getIcons().add(new Image("/images/icon-app.png"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            AddSectionController addSectionController = loader.getController();
            addSectionController.setService(service.getSectionService(), section, stage);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void notifyEvent(ListEvent<Section> event) {
        sectionsTable.getItems().setAll(service.getSectionService().getAll());
        sectionsPagination.setPageCount(service.getSectionService().size() / rowsPerSectionPage() + 1);
        sectionsPagination.setPageFactory(this::createSectionPage);
    }

    public void setServices(GeneralService service) {
        this.service = service;

        sectionNameColumn.setCellValueFactory(new PropertyValueFactory<Section, String>("name"));
        sectionPlacesColumn.setCellValueFactory(new PropertyValueFactory<Section, String>("freePlaces"));

        candidateNameColumn.setCellValueFactory(new PropertyValueFactory<Candidate, String>("name"));
        candidateGenderColumn.setCellValueFactory(new PropertyValueFactory<Candidate, String>("sex"));
        candidatePhoneColumn.setCellValueFactory(new PropertyValueFactory<Candidate, String>("phoneNr"));
        candidateMailColumn.setCellValueFactory(new PropertyValueFactory<Candidate, String>("mail"));

        Callback<TableColumn<Section, String>, TableCell<Section, String>> cellFactory
                = //
                new Callback<TableColumn<Section, String>, TableCell<Section, String>>() {
                    @Override
                    public TableCell call(final TableColumn<Section, String> param) {
                        final TableCell<Section, String> cell = new TableCell<Section, String>() {

                            final ImageView image = new ImageView(new Image("/images/delete-icon.png"));
                            Image imageOk = new Image(getClass().getResourceAsStream("/images/delete-icon.png"));
                            ImageView imageView = new ImageView(imageOk);
                            final Button btn = new Button("", imageView);


                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.getStylesheets().add("/view/ButtonStyle.css");
                                    btn.setOnAction(event -> {
                                        Section section = getTableView().getItems().get(getIndex());
                                        Optional<ButtonType> result = MessageAlert.showConfirmation(null, "Confirmare", "Esti sigur ca vrei sa stergi candidatul selectat ?");
                                        if(result.get() == ButtonType.OK) {
                                            service.getSectionService().delete(section.getId());
                                            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Sters !", "Candidat sters cu succes !");
                                            service.maintenance();
                                            candidatesTable.getItems().clear();
                                        }
                                    });

                                    imageView.setFitHeight(15);
                                    imageView.setFitWidth(15);
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };

        sectionDeleteButton.setCellFactory(cellFactory);

        Callback<TableColumn<Section, String>, TableCell<Section, String>> cellFactory2
                = //
                new Callback<TableColumn<Section, String>, TableCell<Section, String>>() {
                    @Override
                    public TableCell call(final TableColumn<Section, String> param) {
                        final TableCell<Section, String> cell = new TableCell<Section, String>() {

                            //final ImageView image = new ImageView(new Image("/images/edit-icon.png"));
                            Image imageOk = new Image(getClass().getResourceAsStream("/images/edit-icon.png"));
                            ImageView imageView = new ImageView(imageOk);
                            final Button btn = new Button("", imageView);


                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {

                                    btn.setOnAction(event -> {
                                        Section section = getTableView().getItems().get(getIndex());
                                        showAddSection(section, "Modificare sectie");
                                    });
                                    btn.getStylesheets().add("/view/ButtonStyle.css");
                                    imageView.setFitHeight(15);
                                    imageView.setFitWidth(15);
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };

        sectionUpdateButton.setCellFactory(cellFactory2);

        sectionsPagination.setPageCount(service.getSectionService().size() / rowsPerSectionPage() + 1);
        sectionsPagination.setCurrentPageIndex(0);
        sectionsPagination.setPageFactory(this::createSectionPage);

    }

    public int rowsPerCandidatePage() {
        return 9;
    }
    public int rowsPerSectionPage() {
        return 5;
    }

    public VBox createCandidatePage(int pageIndex) {
        Section section = (Section)sectionsTable.getSelectionModel().getSelectedItem();
        int fromIndex = pageIndex * rowsPerCandidatePage();
        int toIndex = Math.min(fromIndex + rowsPerCandidatePage(), service.getCandidatesByGivenSection(section.getId()).size());
        candidatesTable.setItems(FXCollections.observableList(service.getCandidatesByGivenSection(section.getId()).subList(fromIndex, toIndex)));
        return new VBox(candidatesTable);
    }

    public VBox createSectionPage(int pageIndex) {
        int fromIndex = pageIndex * rowsPerSectionPage();
        int toIndex = Math.min(fromIndex + rowsPerSectionPage(), service.getSectionService().getAll().size());
        sectionsTable.setItems(FXCollections.observableList(service.getSectionService().getAll().subList(fromIndex, toIndex)));
        return new VBox(sectionsTable);
    }
}
