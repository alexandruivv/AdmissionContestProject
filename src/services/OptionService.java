package services;

import domain.Candidate;
import domain.Option;
import domain.Section;
import javafx.util.Pair;
import repositories.Repository;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class OptionService extends Service<Pair<Integer, Integer>, Option> {

    public OptionService(Repository repository) {
        super(repository);
    }

    /**
     * Returns the option which contains a section by a given id
     * @param id - id for the section
     * @return option
     */
    public Option getOptionBySectionId(int id){
        List<Option> list = (List<Option>) super.getAll();
        for(Option option: list){
            if(option.getIdSection() == id){
                return option;
            }
        }
        return null;
    }

    /**
     * Get the option who contains a candidate and a section given
     * @param candidateId - the id of the candidate
     * @param sectionId - the id of the section
     * @return option
     */
    public Option getOptionByCandidateIdAndSection(int candidateId, int sectionId){
        List<Option> list = (List<Option>) super.getAll();
        for(Option option: list){
            if(option.getIdCandidate() == candidateId && option.getIdSection() == sectionId){
                return option;
            }
        }
        return null;
    }


}
