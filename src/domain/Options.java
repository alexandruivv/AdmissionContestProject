package domain;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class Options {
    private List<Option> options = null;

    @XmlElement(name = "option")
    public List<Option> getOptions(){
        return this.options;
    }

    public void setOptions(List<Option> options){
        this.options = options;
    }
}
