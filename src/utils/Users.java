package utils;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class Users {
    private List<User> users = null;

    @XmlElement(name = "user")
    public List<User> getUsers(){
        return this.users;
    }

    public void setUsers(List<User> users){
        this.users = users;
    }
}
