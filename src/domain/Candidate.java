package domain;


import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@XmlRootElement(name = "candidate")
public class Candidate implements hasId<Integer> {

    private static int currentNr;

    private Integer id;
    private String name;
    private Gender sex;
    private String phoneNr;
    private String mail;

    private Date createDate;

    public Candidate(){}

    //constructor Candidate
    public Candidate(String name, Gender sex, String phoneNr, String mail){
        this.id = ++currentNr;
        this.name = name;
        this.sex = sex;
        this.phoneNr = phoneNr;
        this.mail = mail;
        this.createDate = new Date();
    }

    public Candidate(Integer id, String name, Gender sex, String phoneNr, String mail){
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.phoneNr = phoneNr;
        this.mail = mail;
        this.createDate = new Date();
    }

    public Candidate(Integer id, String name, Gender sex, String phoneNr, String mail, Date date){
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.phoneNr = phoneNr;
        this.mail = mail;
        this.createDate = date;
    }


    public void setId(Integer id) {
        this.id = id;
    }

    @XmlElement
    public String getName() {
        return name;
    }

    @XmlElement
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement
    public Gender getSex() {
        return sex;
    }

    public void setSex(Gender sex) {
        this.sex = sex;
    }


    @XmlElement
    public String getPhoneNr() {
        return phoneNr;
    }

    public void setPhoneNr(String phoneNr) {
        this.phoneNr = phoneNr;
    }

    @XmlElement
    public String getMail() {
        return mail;
    }


    public void setMail(String mail) {
        this.mail = mail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Candidate candidat = (Candidate) o;

        return id == candidat.id || mail.equals(candidat.getMail());
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return Integer.toString(id) + ';' + name + ';' + sex.toString() + ';' + phoneNr + ';' + mail;
    }


    @XmlAttribute
    @Override
    public Integer getId() {
        return id;
    }

    public static void setCurrentNr(int nr){
        currentNr = nr;
    }

}
