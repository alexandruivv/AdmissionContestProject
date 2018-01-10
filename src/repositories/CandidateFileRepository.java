package repositories;

import domain.Candidate;
import domain.Candidates;
import domain.ValidationException;
import validators.Validator;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.Calendar;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class CandidateFileRepository extends AbstractFileRepository<Candidate, Integer> {
    public CandidateFileRepository(Validator<Candidate> vali, String fileName) {
        super(vali, fileName);
    }

    @Override
    public void loadData() {
        Candidate.setCurrentNr(0);

        try {
            File file = new File(fileName);
            if(file.length() != 0) {
                JAXBContext jaxbContext = JAXBContext.newInstance(Candidates.class);
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

                Candidates candidates = (Candidates) jaxbUnmarshaller.unmarshal(file);
                if(candidates.getCandidates() !=null) {
                    for (Candidate candidate : candidates.getCandidates()) {
                        super.save(candidate);
                        Candidate.setCurrentNr(candidate.getId());
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

        Candidates candidates = new Candidates();
        candidates.setCandidates(StreamSupport.stream(super.getAll().spliterator(), false).collect(Collectors.toList()));

        try{
            File file = new File(fileName);
            JAXBContext jaxbContext = JAXBContext.newInstance(Candidates.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);


            jaxbMarshaller.marshal(candidates, file);

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}
