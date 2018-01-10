package services;

import domain.Candidate;
import domain.Option;
import domain.Section;
import utils.DayAndCandidates;
import utils.DayAndMonth;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class GeneralService {
    private CandidateService candidateService;
    private SectionService sectionService;
    private OptionService optionService;
    private UserService userService;

    public GeneralService(CandidateService candidateService, SectionService sectionService, OptionService optionService, UserService userService){
        this.candidateService = candidateService;
        this.sectionService = sectionService;
        this.optionService = optionService;
        this.userService = userService;
    }

    public CandidateService getCandidateService(){
        return this.candidateService;
    }

    public SectionService getSectionService(){
        return this.sectionService;
    }

    public OptionService getOptionService(){
        return this.optionService;
    }

    public UserService getUserService() {
        return this.userService;
    }

    /**
     * Number of candidates that applied
     * @return integer
     */
    public int nrOfCandidatesThatApplied(){
        List<Integer> candidatesEvidence = new ArrayList<>();
        for(Option option: optionService.getAll()){
            int idCandidate = option.getIdCandidate();
            if(!candidatesEvidence.contains(idCandidate)){
                candidatesEvidence.add(idCandidate);
            }
        }
        return candidatesEvidence.size();
    }


    /**
     * Verifies if a candidate or a section doesn't exist anymore. If is true, it deletes all the options that contain
     * the given candidate or section
     */
    public void maintenance(){
        List<Option> optionList =  optionService.getAll();
        for(Option option: optionList){
            if(candidateService.getCandidateById(option.getIdCandidate()) == null || sectionService.getSectieById(option.getIdSection()) == null){
                optionService.delete(option.getId());
            }
        }
    }

    public void maintenancePriority(int candidateId){
        short priority = 1;
        for(Option option: optionService.getAll()){
            if(option.getIdCandidate() == candidateId){
                option.setPriority(priority);
                priority++;
            }
        }
    }

    /**
     * Get the candidates who applied for a section
     * @param sectionId - the id of the section
     * @return list of candidates
     */
    public List<Candidate> getCandidatesByGivenSection(int sectionId){
        List<Candidate> candidates = new ArrayList<>();
        optionService.filterAndSorter(optionService.getAll(),
                (x)->x.getIdSection() == sectionId,
                (x,y)->y.getPriority() - x.getPriority()).forEach(option -> {
            candidates.add(candidateService.getCandidateById(option.getIdCandidate()));
        });
        return candidates;
    }

    /**
     * Returns the options which contain a section given (it can be a substring)
     * @param section - substr to be searched or str
     * @return list of options
     */
    public List<Option> getOptionsBySection(String section){
        return optionService.filterAndSorter(optionService.getAll(), (x) -> {return sectionService.getSectieById(x.getIdSection()).getName().contains(section);},
                (x,y) -> y.getPriority() - x.getPriority());
    }

    /**
     * Returns the options which have a candidate whose name is given (can be a substring)
     * @param name - the substr to be searched or str
     * @return list of options
     */
    public List<Option> getOptionsByName(String name){
        return optionService.filterAndSorter(optionService.getAll(),
                (x) -> {return candidateService.getCandidateById(x.getIdCandidate()).getName().contains(name);},
                (x,y) -> {return y.getPriority() - x.getPriority();});
    }

    /**
     * Returns the options which have a candidate whose phone number is given (can be a substr)
     * @param phoneNr - the substr to be searched or str
     * @return list of options
     */
    public List<Option> getOptionsByPhoneNr(String phoneNr){
        return optionService.filterAndSorter(
                optionService.getAll(), (x) -> {return candidateService.getCandidateById(x.getIdCandidate()).getPhoneNr().contains(phoneNr);},
                (x,y) -> {return y.getPriority() - x.getPriority();});
    }



    /**
     * Get all the options which contains an id of a candidate
     * @param id - the id of the candidate
     * @return list of optionss
     */
    public List<Option> getOptionsByCandidateId(int id){
        return optionService.filterAndSorter(optionService.getAll(),
                (x)->x.getIdCandidate() == id,
                (x,y)->x.getPriority()-y.getPriority());
    }

    /**
     * Get the sections applied by a candidate given
     * @param id - the id of the candidate
     * @return list of sections
     */
    public List<Section> getSectionsByCandidateId(int id){
        List<Section> result = new ArrayList<>();
        optionService.filterAndSorter(optionService.getAll(),
                (x)->x.getIdCandidate() == id,
                (x,y)->x.getPriority()-y.getPriority()).forEach(option -> {result.add(sectionService.getSectieById(option.getIdSection()));});
        return result;
    }

    /**
     * Get the sections that nobody applied for
     * @param idCandidate - the id of the candidate
     * @return list of sections
     */
    public List<Section> getSectionsNotUsedByCandidate(int idCandidate){
        List<Section> sections = new ArrayList<>();
        List<Integer> usedNames = new ArrayList<>();
        optionService.filterAndSorter(optionService.getAll(),
                (x)->x.getIdCandidate() == idCandidate,
                (x,y)->y.getPriority()-x.getPriority()).forEach(option -> {usedNames.add(option.getIdSection());});
        for(Section section: sectionService.getAll()) {
            if (!usedNames.contains(section.getId())) {
                sections.add(section);
            }
        }
        return sections;
    }

    public List<Candidate> getCandidatesByNumberOfOptions(int nrCandidatesToReturn, int numberOfOptions){
        List<Candidate> toReturn = new ArrayList<>();

        Map<Integer, Integer> candidatesId = new HashMap<>();
        for(Option option: optionService.getAll()){
            if(candidatesId.containsKey(option.getIdCandidate())){
                candidatesId.put(option.getIdCandidate(), candidatesId.get(option.getIdCandidate()) + 1);
            }
            else{
                candidatesId.put(option.getIdCandidate(), 1);
            }
        }

        int number = 0;

        for (Map.Entry<Integer, Integer> entry : candidatesId.entrySet()) {
            if (entry.getValue() == 3) {
                number++;
                toReturn.add(candidateService.getCandidateById(entry.getKey()));
                if(number == nrCandidatesToReturn){
                    break;
                }
            }
        }

        return toReturn;
    }

    public List<Candidate> getCandidatesByNumberOfOptions(int numberOfOptions){
        List<Candidate> toReturn = new ArrayList<>();

        Map<Integer, Integer> candidatesId = new HashMap<>();
        for(Option option: optionService.getAll()){
            if(candidatesId.containsKey(option.getIdCandidate())){
                candidatesId.put(option.getIdCandidate(), candidatesId.get(option.getIdCandidate()) + 1);
            }
            else{
                candidatesId.put(option.getIdCandidate(), 1);
            }
        }

        for (Map.Entry<Integer, Integer> entry : candidatesId.entrySet()) {
            if (entry.getValue() == numberOfOptions) {
                toReturn.add(candidateService.getCandidateById(entry.getKey()));
            }
        }

        return toReturn;
    }

    public List<Candidate> getCandidatesWithNoOptions(){
        List<Candidate> toReturn = new ArrayList<>();

        Set<Integer> candidatesIdWithOptions = new TreeSet<>();
        for(Option option: getOptionService().getAll()){
            candidatesIdWithOptions.add(option.getIdCandidate());
        }
        for(Candidate candidate: getCandidateService().getAll()){
            if(!candidatesIdWithOptions.contains(candidate.getId())){
                toReturn.add(candidate);
            }
        }
        return toReturn;
    }

    public DayAndCandidates getDayWithMostEnrolments(){
        Set<Integer> candidatesEvidence = new TreeSet<>();

        Map<DayAndMonth, List<Candidate>> dayWithNumberOfCandidates = new HashMap<>();

        for(Option option: getOptionService().getAll()){
            if(!candidatesEvidence.contains(option.getIdCandidate())){
                candidatesEvidence.add(option.getIdCandidate());
                DayAndMonth dayAndMonth = getOptionCreateDayAndMonth(option);
                if(!dayWithNumberOfCandidates.containsKey(dayAndMonth)){
                    List<Candidate> newList = new ArrayList<>();
                    newList.add(getCandidateService().getCandidateById(option.getIdCandidate()));
                    dayWithNumberOfCandidates.put(dayAndMonth, newList);
                }
                else{
                    Candidate candidate = getCandidateService().getCandidateById(option.getIdCandidate());
                    List<Candidate> listOfCandidates = dayWithNumberOfCandidates.get(dayAndMonth);
                    listOfCandidates.add(candidate);
                    dayWithNumberOfCandidates.put(dayAndMonth, listOfCandidates);
                }
            }
        }

        DayAndMonth dayAndMonth = new DayAndMonth();
        List<Candidate> candidates = new ArrayList<>();
        int max = 0;

        for(Map.Entry<DayAndMonth, List<Candidate>> entry: dayWithNumberOfCandidates.entrySet()){
            List<Candidate> currentCandidates = entry.getValue();
            if(currentCandidates.size() >= max){
                max = currentCandidates.size();
                dayAndMonth = entry.getKey();
                candidates = currentCandidates;
            }
        }
        return new DayAndCandidates(dayAndMonth, candidates);
    }

    public List<Section> mostUsedSectionsForEnrolment(int numberToReturn, boolean most){

        List<Section> toBeReturned = new ArrayList<>();

        Map<Integer, Integer> sectionsWithNumberOfUses = new HashMap<>();

        for(Option option: getOptionService().getAll()){
            if(sectionsWithNumberOfUses.containsKey(option.getIdSection())){
                sectionsWithNumberOfUses.put(option.getIdSection(), sectionsWithNumberOfUses.get(option.getIdSection()) + 1);
            }
            else{
                sectionsWithNumberOfUses.put(option.getIdSection(), 1);
            }
        }

        List<Integer> useNumbers = StreamSupport.stream(sectionsWithNumberOfUses.values().spliterator(), false).collect(Collectors.toList());
        if(most) {
            useNumbers = useNumbers.stream().sorted((x, y) -> y - x).collect(Collectors.toList());
        }
        else{
            useNumbers = useNumbers.stream().sorted((x, y) -> x - y).collect(Collectors.toList());
        }
        if(useNumbers.size() >= numberToReturn)
            for(int nr: useNumbers.subList(0, numberToReturn)) {
                for (Map.Entry<Integer, Integer> entry : sectionsWithNumberOfUses.entrySet()) {
                    if(entry.getValue() == nr) {
                        if(!toBeReturned.contains(getSectionService().getSectieById(entry.getKey()))) {
                            toBeReturned.add(getSectionService().getSectieById(entry.getKey()));
                            break;
                        }
                    }
                }
            }


        return toBeReturned;
    }


    private DayAndMonth getOptionCreateDayAndMonth(Option option){
        Date date = option.getCreateDate();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return new DayAndMonth(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH));
    }


    public short getPriority(int candidateId){
         return (short) (getOptionsByCandidateId(candidateId).size() + 1);
    }
}
