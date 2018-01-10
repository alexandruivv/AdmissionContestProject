package services;

import domain.Candidate;
import repositories.Repository;
import utils.DayCounter;
import utils.Observable;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class CandidateService extends Service<Integer, Candidate> {
    public CandidateService(Repository repo) {
        super(repo);
    }

    /**
     * Returnes a candidate by id
     * @param id - the id of the candidate
     * @return candidate
     */
    public Candidate getCandidateById(int id){
        return super.findById(id);
    }

    public Candidate getCandidateByMail(String mail){
        for(Candidate candidate: super.getAll()){
            if(candidate.getMail().equals(mail)){
                return candidate;
            }
        }
        return null;
    }

    public List<Candidate> filterByName(String name){
        return filterAndSorter(super.getAll(),
                (x)->x.getName().contains(name),
                (x,y)->x.getName().compareTo(y.getName()));
    }

    public List<Candidate> filterByGender(String gender){
        return filterAndSorter(super.getAll(),
                (x)->x.getSex().equals(gender),
                (x,y)->x.getName().compareTo(y.getName()));
    }

    public List<Candidate> filterByPhone(String phone){
        return filterAndSorter(super.getAll(),
                (x)->x.getPhoneNr().contains(phone),
                (x,y)->x.getName().compareTo(y.getName()));
    }

    public List<Candidate> filterByMail(String mail){
        return filterAndSorter(super.getAll(),
                (x)->x.getMail().contains(mail),
                (x,y)->x.getName().compareTo(y.getName()));
    }

    public DayCounter getMaxRegistersDay(){
        if(super.getAll().size() != 0){
            //initialize with first
            DayCounter previous = getDayFromDate(super.getAll().get(0).getCreateDate());

            //to return
            DayCounter toReturn = new DayCounter(previous.getDay(), previous.getMonth(), previous.getCount());

            //iterate through the rest of the list

            for(Candidate candidate: super.getAll().subList(1, super.getAll().size())){
                //get current
                DayCounter current = getDayFromDate(candidate.getCreateDate());

                if(current.getDay() == previous.getDay() && current.getMonth() == previous.getMonth()){
                    int previousCount = previous.getCount() + 1;
                    previous.setCount(previousCount);
                }
                else{
                    if(toReturn.getCount() <= previous.getCount()){
                        toReturn.setDay(previous.getDay());
                        toReturn.setMonth(previous.getMonth());
                        toReturn.setCount(previous.getCount());
                    }
                    previous.setDay(current.getDay());
                    previous.setMonth(current.getMonth());
                    previous.setCount(current.getCount());
                }
            }

            if(previous.getCount() >= toReturn.getCount()){
                toReturn.setDay(previous.getDay());
                toReturn.setMonth(previous.getMonth() + 1);
                toReturn.setCount(previous.getCount());
            }
            return toReturn;
        }
        return null;
    }

    private DayCounter getDayFromDate(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        return new DayCounter(day, month, 1);
    }

}
