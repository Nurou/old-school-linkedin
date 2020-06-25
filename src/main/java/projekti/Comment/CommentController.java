package projekti.Comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import projekti.Post.PostRepository;

@Controller
public class CommentController {

  @Autowired
  CommentRepository commentRepository;

  @Autowired
  PostRepository postRepository;

  @PostMapping("/posts/{id}/comment")
  public String newComment(@RequestParam Long postId, @PathVariable Long id, @RequestParam String content) {

    // get comment content
    Comment comment = new Comment();
    comment.setContent(content);
    comment.setPost(postRepository.getOne(postId));
    commentRepository.save(comment);

    return "redirect:/posts";
  }

}