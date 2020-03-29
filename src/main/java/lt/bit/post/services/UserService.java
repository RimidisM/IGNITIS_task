
package lt.bit.post.services;


import lt.bit.post.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lt.bit.post.dao.UserDAO;
import lt.bit.post.exceptions.RecordNotFoundException;

/**
 *
 * @author rimid
 */
@Service
public class UserService {

    @Autowired
    private UserDAO userDao;
    
     public User getUserByUsername(String email) {
        User user = userDao.findByEmail(email);
        if (user == null) {
            throw new RecordNotFoundException("User does not exist");
        }
        return user;
    }

    public void addUser(User user) {
        userDao.save(user);
    }
}
