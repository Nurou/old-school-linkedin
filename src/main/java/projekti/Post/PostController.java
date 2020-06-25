package projekti.Post;

import java.sql.Date;

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

  @GetMapping("/posts")
  public String viewFeed(Model model) {
    // logged in user
    final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    final String username = auth.getName();
    model.addAttribute("posts", postRepository.findAll());
    model.addAttribute("currentUser", accountService.getAccountByUsername(username));
    return "posts";
  }

  @PostMapping("/posts")
  public String newPost(@RequestParam(required = false) String content,
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date, final Long accountId) {

    // Account account = accountService.getById(id)

    Post post = new Post();
    post.setContent(content);
    post.setAccount(accountService.getById(accountId));

    System.out.println(post);

    postRepository.save(post);

    return "redirect:/posts";
  }

}