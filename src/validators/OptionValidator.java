package validators;


import domain.*;
import repositories.Repository;

public class OptionValidator implements Validator<Option> {
    private Repository<Candidate, Integer> candidateRepo;
    private Repository<Section, Integer> sectionRepo;

    public OptionValidator(Repository candidateRepo, Repository sectionRepo){
        this.candidateRepo = candidateRepo;
        this.sectionRepo = sectionRepo;
    }

    @Override
    public void validate(Option option) throws ValidationException {
        String err = "";
        if(option.getIdCandidate() <= 0) err = err.concat("Option's id has to be positive !");
        if(!candidateRepo.containsElem(new Candidate(option.getIdCandidate(),"", Gender.M,"", ""))) err = err.concat("Candidate's id does not exist !! ");
        if(!sectionRepo.containsElem(new Section(option.getIdSection(), "", -1))) err = err.concat("Section's name does not exist !");
        if(err != "") throw new ValidationException(err);
    }
}
