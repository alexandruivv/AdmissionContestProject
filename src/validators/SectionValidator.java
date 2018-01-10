package validators;

import domain.Section;
import domain.ValidationException;

public class SectionValidator implements Validator<Section>{
    @Override
    public void validate(Section section) throws ValidationException {
        String err = "";
        if(section.getId() <= 0) err = err.concat("Id has to be positive !");
        if(section.getFreePlaces() < 0) err = err.concat("Number of places has to be positive !");
        if(section.getName() == "" ) err = err.concat("The name cannot be null !");
        if(err != "") throw new ValidationException(err);
    }
}
