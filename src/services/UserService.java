package services;

import repositories.Repository;
import utils.User;

public class UserService extends Service<String, User> {
    public UserService(Repository repo) {
        super(repo);
    }

    public User getUserByMail(String mail){
        for(User user: super.getAll()){
            if(user.getId().equals(mail)){
                return user;
            }
        }
        return null;
    }
}
