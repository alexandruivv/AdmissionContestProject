package services;

import domain.Section;
import repositories.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SectionService extends Service<Integer, Section> {

    public SectionService(Repository repo) {
        super(repo);
    }

    /**
     * Returnes a section by id
     * @param id - the id of the section
     * @return section
     */
    public Section getSectieById(int id){
        return super.findById(id);
    }

    /**
     * Returnes a section by name
     * @param name - the name of the section
     * @return section
     */
    public Section getSectieByName(String name){
        List<Section> list = super.getAll();
        for(int i = 0; i < list.size(); i++){
            if(Objects.equals(list.get(i).getName(), name))
                return list.get(i);
        }
        return null;
    }


    public List<Section> filterByName(String name){
        return filterAndSorter(super.getAll(),
                (x)->x.getName().contains(name),
                (x,y)->x.getName().compareTo(y.getName()));
    }

    public List<Section> filterByNr(int nr){
        return filterAndSorter(super.getAll(),
                (x)->x.getFreePlaces() >= nr,
                (x,y)->x.getName().compareTo(y.getName()));
    }

    public List<Section> getSectionsWithMostFreePlaces(int numberToReturn){
        List<Section> toBeReturned = new ArrayList<>();
        if(super.getAll().size() >= numberToReturn) {
            toBeReturned = sorter(super.getAll(), (x, y) -> y.getFreePlaces() - x.getFreePlaces());
            return toBeReturned.subList(0, numberToReturn);
        }
        return toBeReturned;
    }

    public int numberOfSections(){
        return super.getAll().size();
    }
}
