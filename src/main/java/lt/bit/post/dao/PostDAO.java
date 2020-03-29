package lt.bit.post.dao;


import lt.bit.post.models.Post;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author rimid
 */
public interface PostDAO extends CrudRepository<Post, Integer> {

}
