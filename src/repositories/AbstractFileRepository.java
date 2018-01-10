package repositories;

import domain.hasId;
import domain.ValidationException;
import validators.Validator;


abstract class AbstractFileRepository<E extends hasId<ID>, ID> extends AbstractRepository<E, ID> {
    protected String fileName;

    public AbstractFileRepository(Validator<E> vali, String fileName) {
        super(vali);
        this.fileName=fileName;
        loadData();
    }

    @Override
    public E save(E entity) throws ValidationException {
        E e=super.save(entity);
        if (e==null)
        {
            saveToFile();
        }
        return e;
    }

    @Override
    public E update(E entity) throws ValidationException {
        E e=super.update(entity);
        if (e!=null)
        {
            saveToFile();
        }
        return e;
    }

    @Override
    public E delete(ID id) {
        E r=super.delete(id);
        saveToFile();
        return r;
    }

    abstract void loadData();
    abstract void saveToFile();

}
