package repositories;

import java.util.HashMap;

import domain.hasId;
import domain.ValidationException;
import validators.Validator;

import java.util.HashMap;

public abstract class AbstractRepository<E extends hasId<ID>, ID> implements Repository<E,ID> {
    private HashMap<ID, E> entities;
    private Validator<E> vali;

    public AbstractRepository(Validator<E> vali) {
        entities=new HashMap<ID, E>();
        this.vali = vali;
    }

    @Override
    public long size() {
        return entities.size();
    }

    @Override
    public E save(E entity) throws ValidationException {
        vali.validate(entity);
        if (entities.containsKey(entity.getId())){
            return entities.get(entity.getId());
        }
        if(entities.containsValue(entity)){
            return entity;
        }

        entities.put(entity.getId(),entity);
        return null;
    }

    @Override
    public E update(E entity) throws ValidationException {
        if (entities.containsKey(entity.getId())){
            vali.validate(entity);
            return entities.put(entity.getId(),entity);
        }
        return null;
    }

    @Override
    public E delete(ID id) {
        return entities.remove(id);
    }

    @Override
    public E getOneById(ID id) {
        if(entities.containsKey(id))
            return entities.get(id);
        return null;
    }

    @Override
    public boolean containsElem(E elem){
        return entities.containsKey(elem.getId()) || entities.containsValue(elem);
    }

    @Override
    public Iterable<E> getAll() {
        return entities.values();
    }
}
