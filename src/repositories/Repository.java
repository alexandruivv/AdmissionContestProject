package repositories;

import domain.ValidationException;

public interface Repository<E, Id> {
    long size();
    E save(E entity) throws ValidationException;
    E update(E entity) throws ValidationException;
    E delete(Id id);
    E getOneById(Id id);
    boolean containsElem(E elem);
    Iterable<E> getAll();
}
