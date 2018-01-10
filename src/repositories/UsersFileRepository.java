package repositories;

import domain.ValidationException;
import utils.User;
import utils.Users;
import validators.Validator;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UsersFileRepository extends AbstractFileRepository<User, String> {
    public UsersFileRepository(Validator<User> vali, String fileName) {
        super(vali, fileName);
    }

    @Override
    void loadData() {
        try {
            File file = new File(fileName);
            if(file.length() != 0) {
                JAXBContext jaxbContext = JAXBContext.newInstance(Users.class);
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                Users users = (Users) jaxbUnmarshaller.unmarshal(file);
                if(users.getUsers() != null) {
                    for (User user : users.getUsers()) {
                        super.save(user);
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
    void saveToFile() {
        Users users = new Users();
        users.setUsers(StreamSupport.stream(super.getAll().spliterator(), false).collect(Collectors.toList()));

        try{
            File file = new File(fileName);
            JAXBContext jaxbContext = JAXBContext.newInstance(Users.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);


            jaxbMarshaller.marshal(users, file);

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}
