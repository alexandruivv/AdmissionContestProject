package validators;

import domain.Candidate;
import domain.Gender;
import domain.ValidationException;

import java.util.Objects;

public class CandidateValidator implements Validator<Candidate> {

    private boolean containsLetter(String string){
        return string.matches(".*[a-zA-Z]+.*");
    }

    private boolean containsDigits(String string){
        return string.matches(".*[0-9]+.*");
    }

    private boolean validEmailAddress(String string){
        return string.matches(".*[a-zA-Z]+.*@+[a-zA-Z]+.*");
    }

    @Override
    public void validate(Candidate elem) throws ValidationException {
        String errors = "";

        //id
        if(elem.getId() <= 0) errors += "Id has to be positive !\n";

        //name
        if(!containsLetter(elem.getName())) errors += "Name has to contain at least one letter ! !\n";

        if(elem.getSex().equals(Gender.None)) errors+= "You have to pick a gender !\n";

        //phone number
        if(elem.getPhoneNr().length() < 5) errors += "Phone number length has to contain at least 5 digits !\n";
        try{
            Double.parseDouble(elem.getPhoneNr());
        }catch(NumberFormatException e){
            errors += "Phone number has to contain only digits\n";
        }
        if(!containsDigits(elem.getPhoneNr())) errors += "Phone number has digits !\n";

        //mail
        if(!validEmailAddress(elem.getMail())) errors += "Invalid mail address !\n";
        if(errors.length() > 0) throw new ValidationException(errors);
    }
}
