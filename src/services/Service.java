package services;

import domain.ValidationException;
import repositories.Repository;
import utils.ListEvent;
import utils.ListEventType;
import utils.Observable;
import utils.Observer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public abstract class Service<Id, E> implements Observable<E>{
    private Repository<E, Id> repo;
    ArrayList<Observer<E>> observersList = new ArrayList<>();

    public Service(Repository repo){
        this.repo = repo;
    }

    public E save(E elem) throws ValidationException {
        E returned = repo.save(elem);
        if(returned == null){
            ListEvent<E> event = createEvent(ListEventType.ADD, returned, getAll());
            notifyObservers(event);
        }
        return returned;
    }

    public E delete(Id id){
        E returned = repo.delete(id);
        if(returned != null){
            ListEvent<E> event = createEvent(ListEventType.REMOVE, returned, getAll());
            notifyObservers(event);
        }
        return returned;
    }

    public E update(E elem) throws ValidationException {
        E returned = repo.update(elem);
        if(returned != null){
            ListEvent<E> event = createEvent(ListEventType.UPDATE, returned, getAll());
            notifyObservers(event);
        }
        return returned;
    }

    public E findById(Id id){
        return repo.getOneById(id);
    }


    @Override
    public void addObserver(Observer<E> o) {
        observersList.add(o);
    }

    @Override
    public void removeObserver(Observer<E> o) {
        observersList.remove(o);
    }

    @Override
    public void notifyObservers(ListEvent<E> event) {
        observersList.forEach(x->x.notifyEvent(event));
    }

    public int size(){
        return getAll().size();
    }

    public List<E> getAll(){
        return StreamSupport.stream(repo.getAll().spliterator(), false).collect(Collectors.toList());
    }

    /**
     * Filters and sorts a list by a given predicate and a comparator
     * @param list - the list to be filtered and sorted
     * @param predicate - the predicate
     * @param comparator - the comparator
     * @param <T> Type of the element
     * @return the list filtered and sorted
     */
    public <T> List<T> filterAndSorter(List<T> list, Predicate<T> predicate, Comparator<T> comparator){
        List<T> filtered = new ArrayList<>();
        filtered = list.stream().filter(predicate).sorted(comparator).collect(Collectors.toList());
        return filtered;
    }

    public <T> List<T> sorter(List<T> list, Comparator<T> comparator){
        List<T> sorted = new ArrayList<>();
        sorted = list.stream().sorted(comparator).collect(Collectors.toList());
        return sorted;
    }

    private <E> ListEvent<E> createEvent(ListEventType type, final E elem, List<E> list){
        return new ListEvent<E>(type) {
            @Override
            public List<E> getAll() {
                return list;
            }

            @Override
            public E getElement() {
                return elem;
            }
        };
    }
}
