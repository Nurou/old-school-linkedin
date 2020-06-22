package projekti.Skill;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import projekti.Account.AccountService;

@Controller
public class SkillController {
  @Autowired
  SkillRepository skillRepository;

  @PostMapping("/skills/new")
  public String addSkill(@RequestParam String profileName, Long profileId, String skillName) {

    // add skill
    System.out.println(skillName);

    Skill newSkill = new Skill();
    newSkill.setName(skillName);
    newSkill.setProfileId(profileId);

    skillRepository.save(newSkill);

    return "redirect:/profile/" + profileName;
  }

}