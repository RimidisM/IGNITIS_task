package lt.bit.post.dao;

import lt.bit.post.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author rimid
 */
@Repository
public interface UserDAO extends CrudRepository<User, Integer> {
    User findByEmail(String email);
}
