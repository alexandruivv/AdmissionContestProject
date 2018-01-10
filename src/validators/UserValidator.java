package validators;

import domain.ValidationException;
import utils.User;

public class UserValidator implements Validator<User> {
    private boolean validEmailAddress(String string){
        return string.matches(".*[a-zA-Z]+.*@+[a-zA-Z]+.*");
    }

    private boolean validPassword(String password){
        return password.matches("(?=.*[a-z])(?=\\S+$).{5,}");
    }


    @Override
    public void validate(User elem) throws ValidationException {
        String errors = "";

        if(!validEmailAddress(elem.getId())) errors += "Mail-ul nu este valid !\n";
        if(!validPassword(elem.getPassword())) errors += "Parola trebuie sa contina minim 5 caractere, fara spatii.\n";

        if(errors.length() > 0) throw new ValidationException(errors);
    }
}
