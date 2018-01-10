package domain;

import javafx.util.Pair;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement(name = "option")
public class Option implements hasId<Pair<Integer, Integer>>{
    private int idCandidate;
    private int idSection;
    private short priority;

    private Date createDate;

    public Option(){}
    //constructor Option
    public Option(int idCandidate, int idSection, short priority){
        this.idCandidate = idCandidate;
        this.idSection = idSection;
        this.priority = priority;
        this.createDate = new Date();
    }

    public Option(int idCandidate, int idSection, short priority, Date date){
        this.idCandidate = idCandidate;
        this.idSection = idSection;
        this.priority = priority;
        this.createDate = date;
    }

    @XmlAttribute
    public int getIdCandidate() {
        return idCandidate;
    }

    public void setIdCandidate(int idCandidate) {
        this.idCandidate = idCandidate;
    }

    @XmlAttribute
    public int getIdSection() {
        return idSection;
    }

    @XmlAttribute
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setIdSection(int idSection) {
        this.idSection = idSection;
    }

    @XmlElement
    public short getPriority() {
        return priority;
    }

    public void setPriority(short priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return Integer.toString(idCandidate) + ';' + Integer.toString(idSection) + ';' + Short.toString(priority);
    }

    @Override
    public Pair<Integer, Integer> getId() {
        return new Pair<>(idCandidate, idSection);
    }
}
