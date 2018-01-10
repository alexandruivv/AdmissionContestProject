package utils;

import domain.hasId;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="user")
public class User implements hasId<String> {
    private String userMail;
    private String password;

    private UserType userType;

    public User(){

    }
    public User(String userMail, String password, UserType userType) {
        this.userMail = userMail;
        this.password = password;
        this.userType = userType;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    @XmlAttribute
    public String getUserMail(){return this.userMail;}

    @XmlElement
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @XmlElement
    public UserType getUserType(){ return this.userType;}

    public void setUserType(UserType userType){ this.userType = userType;}


    @Override
    public String getId() {
        return userMail;
    }
}
