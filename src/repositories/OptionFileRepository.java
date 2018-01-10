package repositories;

import domain.Option;
import domain.Options;
import domain.ValidationException;
import javafx.util.Pair;
import validators.Validator;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class OptionFileRepository extends AbstractFileRepository<Option, Pair<Integer, Integer>> {
    public OptionFileRepository(Validator<Option> vali, String fileName) {
        super(vali, fileName);
    }

    @Override
    public void loadData() {
        try{
            File file = new File(fileName);
            if(file.length() != 0){
                JAXBContext jaxbContext = JAXBContext.newInstance(Options.class);
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                Options options = (Options) jaxbUnmarshaller.unmarshal(file);
                if(options.getOptions() != null) {
                    for (Option option : options.getOptions()) {
                        super.save(option);
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
        Options options = new Options();

        options.setOptions(StreamSupport.stream(super.getAll().spliterator(), false).collect(Collectors.toList()));

        try{
            File file = new File(fileName);
            JAXBContext jaxbContext = JAXBContext.newInstance(Options.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            jaxbMarshaller.marshal(options, file);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

}
