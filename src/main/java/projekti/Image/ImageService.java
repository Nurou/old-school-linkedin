package projekti.Image;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import projekti.Account.Account;
import projekti.Account.AccountRepository;
import projekti.Account.AccountService;

@Service
public class ImageService {

  @Autowired
  ImageRepository imageRepository;
  @Autowired
  AccountService accountService;

  @Transactional
  public Long getProfileImageId(Account account) {

    Image image = imageRepository.findByAccount(account);

    if (image == null) {
      return 0L;
    }

    Long imageId = image.getId();
    return imageId;
  }

  @Transactional
  public Image getImageByProfileName(String profileName) {
    return imageRepository.findByAccount(accountService.getAccountByProfileName(profileName));
  }

  public void addImage(Image image) {
    imageRepository.save(image);
  }

  @Transactional
  public void addUserProfileImage(Image image, Account currentUser) {
    addImage(image);
    currentUser.setProfileImage(image);
    accountService.saveAccount(currentUser);
  }
}