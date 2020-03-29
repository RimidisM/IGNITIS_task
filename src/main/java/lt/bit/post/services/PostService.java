package lt.bit.post.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lt.bit.post.exceptions.ForbiddenException;
import lt.bit.post.exceptions.RecordNotFoundException;
import lt.bit.post.models.User;
import lt.bit.post.models.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import lt.bit.post.dao.PostDAO;

/**
 *
 * @author rimid
 */
@Service
public class PostService {

    @Autowired
    private PostDAO postDao;

    @Autowired
    private UserService userService;


    // If there would be a lot of data in the application, we should filter this in SQL
    // WHERE user_id = currentUserId
    public List getAllPosts() {
        User currentUser = getCurrentUser();
        List posts = new ArrayList<>();
        postDao.findAll().forEach(post -> {
            if (currentUser.getId() == post.getUserId()) {
                posts.add(post);
            }
        });
        return posts;
    }

    public Post getPost(Integer id) {
        User currentUser = getCurrentUser();
        Optional<Post> userPost = postDao.findById(id);
        if(userPost.isEmpty()){
            throw new RecordNotFoundException("Post not found");
        }
        if (userPost.get().getUserId() != currentUser.getId()) {
            throw new ForbiddenException("Post does not belong to the user");
        }
        return userPost.get();    
    }

    public Post addPost(Post post) {
        User currentUser = getCurrentUser();
        post.setUserId(currentUser.getId());
        Post newPost = postDao.save(post);
        return newPost;
    }

    public Post updatePost(Integer id, Post post) {
        Post userPost = getPost(id);
        userPost.setText(post.getText());
        userPost.setTitle(post.getTitle());
        Post updatedPost = postDao.save(userPost);
        return updatedPost;
    }

    public void deletePost(Integer id) {
        Integer userPostId = getPost(id).getId();
        postDao.deleteById(userPostId);
    }

    private User getCurrentUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        if (userDetails == null) {
            throw new RecordNotFoundException("User does not exist");
        }
        String email = userDetails.getUsername();
        return userService.getUserByUsername(email);
    }
}
