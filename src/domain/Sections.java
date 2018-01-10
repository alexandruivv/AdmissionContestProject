package domain;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class Sections {
    private List<Section> sections = null;

    @XmlElement(name = "section")
    public List<Section> getSections(){
        return this.sections;
    }

    public void setSections(List<Section> sections){
        this.sections = sections;
    }
}
