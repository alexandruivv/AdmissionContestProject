package utils;

import domain.hasId;
import javafx.scene.image.Image;
import javafx.scene.shape.Path;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="user")
public class User implements hasId<String> {
    private String userMail;
    private String password;

    private UserType userType;
    private String imagePath;

    public User(){

    }
    public User(String userMail, String password, UserType userType) {
        this.userMail = userMail;
        this.password = password;
        this.userType = userType;
        this.imagePath = "/images/person.png";
    }

    public User(String userMail, String password, UserType userType, String imagePath) {
        this.userMail = userMail;
        this.password = password;
        this.userType = userType;
        this.imagePath = imagePath;
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

    @XmlElement
    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String getId() {
        return userMail;
    }
}
