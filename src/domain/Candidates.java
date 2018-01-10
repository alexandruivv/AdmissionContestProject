package domain;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class Candidates {

    private List<Candidate> candidates = null;
    @XmlElement(name = "candidate")
    public List<Candidate> getCandidates(){
        return this.candidates;
    }

    public void setCandidates(List<Candidate> candidates){
        this.candidates = candidates;
    }
}
