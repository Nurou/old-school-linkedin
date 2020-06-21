package projekti.Image;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import projekti.Account.Account;
import projekti.Account.AccountRepository;
import projekti.Account.AccountService;

@Service
public class ImageService {

  @Autowired
  ImageRepository imageRepository;
  @Autowired
  AccountService accountService;

  public Long getProfileImageId(Account account) {
    return imageRepository.findByAccount(account).getId();
  }

  public Image getImageByProfileName(String profileName) {
    return imageRepository.findByAccount(accountService.getAccountByProfileName(profileName));
  }

  public void addImage(Image image) {
    imageRepository.save(image);
  }

  public void addUserProfileImage(Image image, Account currentUser) {
    addImage(image);
    currentUser.setProfileImage(image);
    accountService.saveAccount(currentUser);
  }
}