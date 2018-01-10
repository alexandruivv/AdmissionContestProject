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
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import services.CandidateService;
import services.GeneralService;
import services.OptionService;
import services.SectionService;
import utils.ListEvent;
import utils.ListEventType;
import utils.Observer;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class XCell extends ListCell<String> {
    HBox hbox = new HBox();
    Label label = new Label("");
    Pane pane = new Pane();
    Button button = new Button();
    CandidateController candidateController;


    public XCell(CandidateController candidateController) {
        super();
        this.candidateController = candidateController;
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
                Section section = this.candidateController.getGeneralService().getSectionService().getSectieByName(fields[1]);
                Candidate candidate = (Candidate) this.candidateController.getCandidatesTable().getSelectionModel().getSelectedItem();
                Option optionToDelete = this.candidateController.getGeneralService().getOptionService().getOptionByCandidateIdAndSection(candidate.getId(), section.getId());
                if(this.candidateController.getGeneralService().getOptionService().delete(optionToDelete.getId()) != null){
                    //getListView().getItems().remove(getIndex());
                    this.candidateController.getGeneralService().maintenancePriority(candidate.getId());
                    //getListView().getItems().clear();
                    //for(Option option: this.candidateController.getGeneralService().getOptionsByCandidateId(candidate.getId())) {
                      //  Section sectionFromOption = this.candidateController.getGeneralService().getSectionService().getSectieById(option.getIdSection());
                        //getListView().getItems().add(Short.toString(option.getPriority()) + "." + sectionFromOption.getName());
                    //}
                    List<Option> list = this.candidateController.getGeneralService().getOptionsByCandidateId(candidate.getId());
                    this.candidateController.getOptionsPagination().setPageCount(list.size() / candidateController.rowsOptionsPerPage() + 1);
                    this.candidateController.getOptionsPagination().setPageFactory(candidateController::createPageOptions);
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


public class CandidateController implements Observer<Candidate> {

    //services
    private GeneralService service;
    private Candidate candidate;

    //view components
    @FXML
    TableView candidatesTable;

    @FXML
    ListView optionsView;

    @FXML
    TableColumn nameColumn;

    @FXML
    TableColumn genderColumn;

    @FXML
    TableColumn phoneColumn;

    @FXML
    TableColumn mailColumn;

    @FXML
    TableColumn<Candidate, String> deleteColumn;

    @FXML
    TableColumn<Candidate, String> updateColumn;

    @FXML
    ComboBox<String> filterBox;

    @FXML
    TextField filterField;

    @FXML
    Pane addOptionPane;

    @FXML
    Pagination candidatesPagination;

    @FXML
    Pagination optionsPagination;


    @FXML
    public void initialize(){
        candidatesTable.getStylesheets().add("/view/TableViewStyle.css");
        optionsView.setCellFactory(param -> new XCell(this));
    }


    @FXML
    public void handleAddCandidateButton(MouseEvent ev){
        showAddView(null, "Adaugare candidat");
    }


    @FXML
    public void handleAddOption(MouseEvent ev){
        Candidate candidate =(Candidate) candidatesTable.getSelectionModel().getSelectedItem();
        if(candidate == null){
            MessageAlert.showErrorMessage(null, "Nu a fost selectat un candidat pentru a inscrie o optiune !");
        }
        else {
            showAddOptionView(candidate);
            optionsPagination.setPageCount(service.getOptionsByCandidateId(candidate.getId()).size() / rowsOptionsPerPage() + 1);
            optionsPagination.setPageFactory(this::createPageOptions);
        }
    }


    @FXML
    public void handleFilter(KeyEvent keyEvent){
        if(!filterField.getText().equals("")){
            ObservableList<Candidate> model = FXCollections.observableArrayList(filterTag());
            candidatesTable.setItems(model);
        }
        else{
            candidatesTable.getItems().setAll(service.getCandidateService().getAll());
            candidatesPagination.setPageFactory(this::createPage);
        }
    }


    private List<Candidate> filterTag(){
        String choice = filterBox.getSelectionModel().getSelectedItem();
        if(choice != null){
            switch (choice){
                case "Nume":
                    return service.getCandidateService().filterByName(filterField.getText());
                case "Sex":
                    return service.getCandidateService().filterByGender(filterField.getText());
                case "Telefon":
                    return service.getCandidateService().filterByPhone(filterField.getText());
                case "Mail":
                    return service.getCandidateService().filterByMail(filterField.getText());
            }
        }
        return service.getCandidateService().getAll();
    }

    @FXML
    public void showOptions(MouseEvent ev){
        setVisibleCandidateGroup(true);
        optionsView.setMinHeight(125);

        Candidate candidate = (Candidate)candidatesTable.getSelectionModel().getSelectedItem();
        if(candidate != null) {
            optionsPagination.setPageCount(service.getOptionsByCandidateId(candidate.getId()).size() / rowsOptionsPerPage() + 1);
            optionsPagination.setCurrentPageIndex(0);
            this.candidate = candidate;
            optionsPagination.setPageFactory(this::createPageOptions);
        }
        else{
            setVisibleCandidateGroup(false);
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


    public void showAddView(Candidate candidat, String title){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/AddCandidateView.fxml"));
        Pane root = null;
        try {
            root = (Pane)loader.load();

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.getIcons().add(new Image("/images/icon-app.png"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            AddCandidateController addController = loader.getController();
            addController.setService(service.getCandidateService(), service.getUserService(), candidat, stage);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setVisibleCandidateGroup(boolean bool){
        addOptionPane.setVisible(bool);
    }

    public void setServices(GeneralService generalService){
        this.service = generalService;

        setVisibleCandidateGroup(false);


        nameColumn.setCellValueFactory(new PropertyValueFactory<Candidate, String>("name"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<Candidate, String>("sex"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<Candidate, String>("phoneNr"));
        mailColumn.setCellValueFactory(new PropertyValueFactory<Candidate, String>("mail"));

        Callback<TableColumn<Candidate, String>, TableCell<Candidate, String>> cellFactory
                = //
                new Callback<TableColumn<Candidate, String>, TableCell<Candidate, String>>() {
                    @Override
                    public TableCell call(final TableColumn<Candidate, String> param) {
                        final TableCell<Candidate, String> cell = new TableCell<Candidate, String>() {

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
                                        Candidate person = getTableView().getItems().get(getIndex());
                                        Optional<ButtonType> result = MessageAlert.showConfirmation(null, "Confirmare", "Esti sigur ca vrei sa stergi candidatul selectat ?");
                                        if(result.get() == ButtonType.OK) {
                                            service.getCandidateService().delete(person.getId());
                                            service.getUserService().delete(person.getMail());
                                            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Sters !", "Candidat sters cu succes !");
                                            service.maintenance();
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

        deleteColumn.setCellFactory(cellFactory);

        Callback<TableColumn<Candidate, String>, TableCell<Candidate, String>> cellFactory2
                = //
                new Callback<TableColumn<Candidate, String>, TableCell<Candidate, String>>() {
                    @Override
                    public TableCell call(final TableColumn<Candidate, String> param) {
                        final TableCell<Candidate, String> cell = new TableCell<Candidate, String>() {

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
                                        getTableView().getSelectionModel().select(getIndex());
                                        Candidate person = getTableView().getItems().get(getIndex());
                                        showAddView(person, "Modificare candidat");
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

        updateColumn.setCellFactory(cellFactory2);

        //candidatesTable.setItems(model);
        candidatesTable.setMinHeight(358);
        candidatesPagination.setPageCount(service.getCandidateService().getAll().size() / rowsPerPage() + 1);
        candidatesPagination.setCurrentPageIndex(0);
        candidatesPagination.setPageFactory(this::createPage);

        filterBox.getItems().addAll("Nume", "Sex", "Telefon", "Mail");
    }

    @Override
    public void notifyEvent(ListEvent<Candidate> event) {
        candidatesTable.getItems().setAll(event);
        if(service.getCandidateService().getAll().size() % rowsPerPage() == 0){
            candidatesPagination.setPageCount(service.getCandidateService().getAll().size() / rowsPerPage());
        }
        else {
            candidatesPagination.setPageCount(service.getCandidateService().getAll().size() / rowsPerPage() + 1);
        }
        candidatesPagination.setPageFactory(this::createPage);
    }


    public int rowsPerPage() {
        return 5;
    }

    public int rowsOptionsPerPage(){
        return 3;
    }

    public Node createPage(int pageIndex) {

        int fromIndex = pageIndex * rowsPerPage();
        int toIndex = Math.min(fromIndex + rowsPerPage(), service.getCandidateService().getAll().size());
        candidatesTable.setItems(FXCollections.observableList(service.getCandidateService().getAll().subList(fromIndex, toIndex)));

        return new VBox(candidatesTable);
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

    public GeneralService getGeneralService(){
        return this.service;
    }

    public TableView getCandidatesTable(){
        return this.candidatesTable;
    }

    public Pagination getOptionsPagination() {
        return optionsPagination;
    }
}
