package validators;

import domain.ValidationException;

public interface Validator<E> {
    void validate(E elem) throws ValidationException;
}
