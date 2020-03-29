package lt.bit.post.controllers;

import java.util.ArrayList;
import lt.bit.post.models.Post;
import lt.bit.post.services.PostService;
import java.util.List;
import lt.bit.post.dto.PostDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author rimid
 */
@RestController
@RequestMapping("/v1/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<List<PostDTO>> getAllPosts() {
        List<PostDTO> postsDTO = new ArrayList<>();
        List<Post> posts = postService.getAllPosts();
        posts.forEach(post -> {
            postsDTO.add(Map(post));
        });
        return ResponseEntity.ok(postsDTO);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<PostDTO> getPost(@PathVariable Integer id) throws Exception {
        PostDTO postDTO = Map(postService.getPost(id));
        return ResponseEntity.ok(postDTO);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<PostDTO> addPost(@RequestBody PostDTO postDTO) {
        Post post = Map(postDTO);
        PostDTO newPost = Map(postService.addPost(post));
        return ResponseEntity.ok(newPost);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<PostDTO> updatePost(@RequestBody PostDTO postDTO, @PathVariable Integer id) {
        Post post = Map(postDTO);
        PostDTO updatedPost = Map(postService.updatePost(id, post));
        return ResponseEntity.ok(updatedPost);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deletePost(@PathVariable Integer id) {
        postService.deletePost(id);
    }

    private Post Map(PostDTO postDTO) {
        Post post = new Post();
        post.setText(postDTO.getText());
        post.setTitle(postDTO.getTitle());
        return post;
    }

    private PostDTO Map(Post post) {
        PostDTO postDTO = new PostDTO();
        postDTO.setId(post.getId());
        postDTO.setText(post.getText());
        postDTO.setTitle(post.getTitle());
        return postDTO;
    }
}
