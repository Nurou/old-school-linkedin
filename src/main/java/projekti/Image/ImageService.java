package projekti.Image;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import projekti.Account.Account;

@Service
public class ImageService {

  @Autowired
  ImageRepository imageRepository;

  public Long getProfileImageId(Account account) {
    return imageRepository.findByAccount(account).getId();
  }
}