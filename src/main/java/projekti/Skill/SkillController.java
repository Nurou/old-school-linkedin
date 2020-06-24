package projekti.Skill;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import projekti.Account.Account;
import projekti.Account.AccountService;

@Controller
public class SkillController {
  @Autowired
  SkillRepository skillRepository;
  @Autowired
  AccountService accountService;

  @PostMapping("/skills/new")
  public String addSkill(@RequestParam Long profileId, @RequestParam String skillName) {

    Skill newSkill = new Skill();
    newSkill.setName(skillName);
    Account account = accountService.getById(profileId);
    newSkill.setAccount(account);

    skillRepository.save(newSkill);

    return "redirect:/profile/" + account.getProfileName();
  }

}