package repositories;

import domain.Section;
import domain.Sections;
import domain.ValidationException;

import validators.Validator;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class SectionFileRepository extends AbstractFileRepository<Section, Integer> {
    public SectionFileRepository(Validator<Section> vali, String fileName) {
        super(vali, fileName);
    }

    @Override
    public void loadData() {
        Section.setCurrentNr(0);
        try {
            File file = new File(fileName);
            if(file.length() != 0) {
                JAXBContext jaxbContext = JAXBContext.newInstance(Sections.class);
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                Sections sections = (Sections) jaxbUnmarshaller.unmarshal(file);
                if(sections.getSections() != null) {
                    for (Section candidate : sections.getSections()) {
                        super.save(candidate);
                        Section.setCurrentNr(candidate.getId());
                    }
                }
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void saveToFile(){
        Sections sections = new Sections();
        sections.setSections(StreamSupport.stream(super.getAll().spliterator(), false).collect(Collectors.toList()));

        try{
            File file = new File(fileName);
            JAXBContext jaxbContext = JAXBContext.newInstance(Sections.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);


            jaxbMarshaller.marshal(sections, file);

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}
