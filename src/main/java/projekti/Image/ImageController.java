package projekti.Image;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import projekti.Account.Account;
import projekti.Account.AccountService;

@Controller
public class ImageController {
  // TODO: refactor to use service
  @Autowired
  private ImageRepository imageRepository;

  @Autowired
  private AccountService accountService;

  @PostMapping("/images")
  public String addImage(@RequestParam("file") MultipartFile file) throws IOException {
    Image img = new Image();

    img.setName(file.getOriginalFilename());
    img.setMediaType(file.getContentType());
    img.setSize(file.getSize());
    img.setContent(file.getBytes());

    // TODO: set image to specific user
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    Account currentUser = accountService.getAccountByUsername(auth.getName());
    currentUser.setProfileImage(img);

    accountService.saveAccount(currentUser);

    imageRepository.save(img);

    return "redirect:/users/profile/" + currentUser.getProfileName();
  }

  @GetMapping("/images")
  public String view() {
    return "redirect:/images/1";
  }

  @GetMapping("/images/{id}")
  public ResponseEntity<byte[]> viewFile(@PathVariable Long id) {
    Image img = imageRepository.getOne(id);

    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.parseMediaType(img.getMediaType()));
    headers.setContentLength(img.getSize());
    return new ResponseEntity<>(img.getContent(), headers, HttpStatus.CREATED);
  }

  @GetMapping(path = "/images/{id}/content", produces = "image/jpeg")
  @ResponseBody
  public byte[] getContent(@PathVariable Long id) {
    return imageRepository.getOne(id).getContent();
  }

}