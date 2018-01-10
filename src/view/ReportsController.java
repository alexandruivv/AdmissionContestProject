package view;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import domain.Candidate;
import domain.Section;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import services.GeneralService;
import utils.DayAndCandidates;

import java.awt.*;
import java.io.File;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class ReportsController {
    private GeneralService service;
    private ObservableList<Candidate> modelCandidates;
    private ObservableList<Section> modelSections;

    private static String currentButton;

    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 14,
            Font.BOLD);


    @FXML
    Button buttonPDF;

    //---------------Option input-----------------
    @FXML
    Pane candidateOptionInput;

    @FXML
    TextField candidateOptionField;
    //---------------------------------------

    //-----------------------Reports candidates view---------------
    @FXML
    Pane candidatesReportsView;

    @FXML
    Text candidatesNameReport;

    @FXML
    Text candidatesDateReport;

    @FXML
    Text candidatesReportDayInform;

    @FXML
    TableColumn nameColumn;

    @FXML
    TableColumn genderColumn;

    @FXML
    TableColumn phoneColumn;

    @FXML
    TableColumn mailColumn;

    @FXML
    TableView candidatesTable;

    //----------------------Section report view----------------------------

    @FXML
    TableView sectionsTable;

    @FXML
    TableColumn sectionNameColumn;

    @FXML
    TableColumn sectionFreePlacesColumn;

    @FXML
    Pane sectionReportsView;

    @FXML
    Text sectionNameReport;

    @FXML
    Text sectionReportDayInform;

    @FXML
    Pane sectionTopSelect;

    @FXML
    ComboBox sectionComboBoxSelect;


    @FXML
    public void initialize(){
        ImageView imageView = new ImageView(new javafx.scene.image.Image("/images/pdf-icon.png"));
        imageView.setFitHeight(30);
        imageView.setFitWidth(30);
        buttonPDF.setGraphic(imageView);

        for(int i = 1; i <= 10; i++){
            sectionComboBoxSelect.getItems().add(Integer.toString(i));
        }
    }

    @FXML
    public void handleReportCandidate1(MouseEvent ev){
        setVisibleReportSection(false);
        setVisibleReportCandidate(true);
        candidatesReportDayInform.setVisible(true);
        generateReport1();
    }

    @FXML
    public void handleReportCandidate2(MouseEvent ev){
        setVisibleReportSection(false);
        setVisibleReportCandidate(true);
        candidateOptionInput.setVisible(true);
        clearCandidatesView();
    }

    @FXML
    public void handleButtonGenerate(MouseEvent ev){
        buttonPDF.setVisible(true);
        setGenerateInfo("Candidati care au numarul de optiuni " + candidateOptionField.getText());
        candidateOptionField.clear();
    }

    @FXML
    public void handleReportCandidate3(MouseEvent ev){
        setVisibleReportSection(false);
        setVisibleReportCandidate(true);
        generateReport3();
    }

    @FXML
    public void handleReportSection1(MouseEvent ev){
        setVisibleReportCandidate(false);
        setVisibleReportSection(true);
        sectionTopSelect.setVisible(true);
        this.currentButton = "1";
        clearSectionsView();
    }

    @FXML
    public void handleReportSection2(MouseEvent ev){
        setVisibleReportCandidate(false);
        setVisibleReportSection(true);
        sectionTopSelect.setVisible(true);
        this.currentButton = "2";
        clearSectionsView();
    }

    @FXML
    public void handleReportSection3(MouseEvent ev){
        setVisibleReportCandidate(false);
        setVisibleReportSection(true);
        sectionTopSelect.setVisible(true);
        this.currentButton = "3";
        clearSectionsView();
    }

    @FXML
    public void handleGenerateSections(MouseEvent ev){
        buttonPDF.setVisible(true);
        if(this.currentButton == "1") {
            generateReport4();
        }
        else if(this.currentButton == "2"){
            generateReport5();
        }
        else{
            generateReport6();
        }
        sectionComboBoxSelect.setValue("1");
    }



    private void setVisibleReportSection(boolean bool){
        sectionReportsView.setVisible(bool);
        sectionTopSelect.setVisible(false);
    }

    private void setVisibleReportCandidate(boolean bool){
        candidatesReportsView.setVisible(bool);
        buttonPDF.setVisible(false);
        candidatesReportDayInform.setVisible(false);
        candidateOptionInput.setVisible(false);
    }


    private void generateReport1(){
        setDataReport1("Ziua cu cele mai multe inscrieri + candidatii");
    }

    private void generateReport3(){
        setDataReport3("Candidati care nu si-au selectat optiunile");
    }

    private void generateReport4(){
        setSectionsReportData("Cele mai solicitate sectii. Top " + sectionComboBoxSelect.getSelectionModel().getSelectedItem().toString(),
                                service.mostUsedSectionsForEnrolment(Integer.parseInt(sectionComboBoxSelect.getSelectionModel().getSelectedItem().toString()), true));
    }

    private void generateReport5(){
        setSectionsReportData("Cele mai putin solicitate sectii. Top " + sectionComboBoxSelect.getSelectionModel().getSelectedItem().toString(),
                service.mostUsedSectionsForEnrolment(Integer.parseInt(sectionComboBoxSelect.getSelectionModel().getSelectedItem().toString()), false));
    }

    private void generateReport6(){
        setSectionsReportData("Sectii cu cele mai multe locuri. Top " + sectionComboBoxSelect.getSelectionModel().getSelectedItem().toString(),
                service.getSectionService().getSectionsWithMostFreePlaces(Integer.parseInt(sectionComboBoxSelect.getSelectionModel().getSelectedItem().toString())));
    }

    private void setSectionsReportData(String title, List<Section> list){
        if(list.size() != 0) {

            sectionNameReport.setText("Nume raport: " + title);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            sectionReportDayInform.setText("Data generarii: " + sdf.format(date));


            modelSections = FXCollections.observableArrayList(list);
            sectionsTable.setItems(modelSections);

        }
        else{
            MessageAlert.showErrorMessage(null, "Nu au fost gasite sectii pentru raport !");
            clearSectionsView();
        }
    }




    public void setService(GeneralService service){
        this.service = service;


        nameColumn.setCellValueFactory(new PropertyValueFactory<Candidate, String>("name"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<Candidate, String>("sex"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<Candidate, String>("phoneNr"));
        mailColumn.setCellValueFactory(new PropertyValueFactory<Candidate, String>("mail"));

        sectionNameColumn.setCellValueFactory(new PropertyValueFactory<Section, String>("name"));
        sectionFreePlacesColumn.setCellValueFactory(new PropertyValueFactory<Section, String>("freePlaces"));
    }

    private void setDataReport1(String title){
        DayAndCandidates dayAndCandidates = service.getDayWithMostEnrolments();
        if(dayAndCandidates.getCandidates().size() != 0) {
            candidatesNameReport.setText("Nume raport: " + title);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            candidatesDateReport.setText("Data generarii: " + sdf.format(date));
            int day = dayAndCandidates.getDayAndMonth().getDay();
            int month = dayAndCandidates.getDayAndMonth().getMonth() + 1;
            candidatesReportDayInform.setText("Ziua: " + Integer.toString(day) +  "   Luna: " +
                                                        Integer.toString(month));

            modelCandidates = FXCollections.observableArrayList(dayAndCandidates.getCandidates());
            candidatesTable.setItems(modelCandidates);

            buttonPDF.setVisible(true);

        }
        else{
            MessageAlert.showErrorMessage(null, "Nu au fost gasiti candidati pentru raport !");
        }
    }

    private void setDataReport3(String title){
        if(service.getCandidatesWithNoOptions().size() != 0) {
            candidatesNameReport.setText("Nume raport: " + title);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            candidatesDateReport.setText("Data generarii: " + sdf.format(date));

            modelCandidates = FXCollections.observableArrayList(service.getCandidatesWithNoOptions());
            candidatesTable.setItems(modelCandidates);

            buttonPDF.setVisible(true);
        }
        else{
            MessageAlert.showErrorMessage(null, "Nu au fost gasiti candidati pentru raport !");
        }
    }

    private void setGenerateInfo(String title){
        int numberOfOptions = Integer.parseInt(candidateOptionField.getText());
        if(service.getCandidatesByNumberOfOptions(numberOfOptions).size() != 0) {

            candidatesNameReport.setText("Nume raport: " + title);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            candidatesDateReport.setText("Data generarii: " + sdf.format(date));


            modelCandidates = FXCollections.observableArrayList(service.getCandidatesByNumberOfOptions(numberOfOptions));
            candidatesTable.setItems(modelCandidates);
        }
        else{
            MessageAlert.showErrorMessage(null, "Nu au fost gasiti candidati pentru raport !");
            clearCandidatesView();
        }
    }

    private void clearCandidatesView(){
        candidatesTable.getItems().clear();
        candidatesNameReport.setText("Nume raport:");
        candidatesDateReport.setText("Data generarii:");
        buttonPDF.setVisible(false);
    }

    private void clearSectionsView(){
        sectionsTable.getItems().clear();
        sectionNameReport.setText("Nume raport:");
        sectionReportDayInform.setText("Data generarii:");
        buttonPDF.setVisible(false);
    }

    @FXML
    private void handleSavePdf(MouseEvent ev){
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("./pdfDocuments/raportPDF.pdf"));

            document.open();

            addContent(document);

            document.close();

            File myFile = new File("./pdfDocuments/raportPDF.pdf");
            Desktop.getDesktop().open(myFile);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void addContent(Document document) throws DocumentException, IOException {

        Paragraph p1 = new Paragraph();
        p1.add(Image.getInstance("./src/images/ubb-logoRez.png"));
        addEmptyLine(p1, 2);
        p1.setAlignment(Element.ALIGN_CENTER);
        p1.setIndentationLeft(36);
        document.add(p1);

        Paragraph paragraph = new Paragraph();

        if(candidatesReportsView.isVisible()) {
            paragraph.add(new Phrase(this.candidatesNameReport.getText(), smallBold));
            addEmptyLine(paragraph, 2);
            paragraph.add(new Phrase(this.candidatesDateReport.getText(), smallBold));
        }
        else{
            paragraph.add(new Phrase(this.sectionNameReport.getText(), smallBold));
            addEmptyLine(paragraph, 2);
            paragraph.add(new Phrase(this.sectionReportDayInform.getText(), smallBold));
        }
        addEmptyLine(paragraph, 5);
        createTable(paragraph);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        document.add(paragraph);

    }

    private void createTable(Paragraph paragraph)
            throws BadElementException {
        if(candidatesReportsView.isVisible()){
            createTableCandidates(paragraph);
        }
        else{
            createTableSections(paragraph);
        }

    }

    private void createTableCandidates(Paragraph paragraph){
        PdfPTable table = new PdfPTable(4);

        PdfPCell c1 = new PdfPCell(new Phrase("Nume"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Sex"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Nr.telefon"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Mail"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        table.setHeaderRows(1);


        for(Object object: candidatesTable.getItems()){
            Candidate candidate = (Candidate) object;
            table.addCell(candidate.getName());
            table.addCell(candidate.getSex().toString());
            table.addCell(candidate.getPhoneNr());
            table.addCell(candidate.getMail());
        }

        paragraph.add(table);
    }

    private void createTableSections(Paragraph paragraph){
        PdfPTable table = new PdfPTable(2);

        PdfPCell c1 = new PdfPCell(new Phrase("Nume"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Nr.locuri"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        table.setHeaderRows(1);


        for(Object object: sectionsTable.getItems()){
            Section section = (Section) object;
            table.addCell(section.getName());
            table.addCell(Integer.toString(section.getFreePlaces()));
        }

        paragraph.add(table);
    }

    private void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

}
