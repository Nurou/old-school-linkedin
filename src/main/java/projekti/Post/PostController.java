package projekti.Post;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import javax.validation.Valid;

import org.apache.xpath.SourceTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import projekti.Account.Account;
import projekti.Account.AccountRepository;
import projekti.Account.AccountService;
import projekti.Connection.Connection;
import projekti.Connection.ConnectionRepository;
import projekti.Connection.ConnectionService;
import projekti.Image.ImageService;
import projekti.Skill.SkillRepository;

@Controller
public class PostController {

  @Autowired
  PostRepository postRepository;

  @Autowired
  AccountService accountService;

  @Autowired
  ConnectionService connectionService;

  @GetMapping("/posts")
  public String viewFeed(Model model) {
    // logged in user
    final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    final String username = auth.getName();
    Account currentUser = accountService.getAccountByUsername(username);
    model.addAttribute("posts", postRepository.findAll());
    model.addAttribute("currentUser", currentUser);
    model.addAttribute("userConnections", connectionService.getConnectedAccountsByUserId(currentUser.getId()));
    return "posts";
  }

  @PostMapping("/posts")
  public String newPost(@RequestParam String content, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date,
      final Long accountId) {

    Post post = new Post();
    post.setContent(content);
    post.setAccount(accountService.getById(accountId));

    System.out.println(post);
    postRepository.save(post);

    return "redirect:/posts";
  }

  @PostMapping("/posts/{id}/upvote")
  public String likePost(@RequestParam Long postId, @PathVariable Long id) {

    Post post = postRepository.getOne(postId);
    Account account = accountService.getById(id);
    if (!post.getLikes().contains(account)) {
      List<Account> upVoters = post.getLikes();
      upVoters.add(account);

      List<Post> likedPosts = account.getLikedPosts();

      likedPosts.add(post);

      account.setLikedPosts(likedPosts);
      post.setLikes(upVoters);

      accountService.saveAccount(account);
      postRepository.save(post);
    }

    return "redirect:/posts";
  }

}