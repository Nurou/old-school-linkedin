package projekti.Skill;

import java.util.List;

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

  @PostMapping("/{profileName}/skills")
  public String addSkill(@PathVariable String profileName, @RequestParam String skillName) {

    Skill newSkill = new Skill();
    newSkill.setName(skillName);
    Account account = accountService.getAccountByProfileName(profileName);
    newSkill.setAccount(account);

    skillRepository.save(newSkill);

    return "redirect:/profile/" + profileName;
  }

  @PostMapping("/skills/endorse")
  public String endorse(@RequestParam Long endorsementSourceId, @RequestParam Long endorsementTargetId,
      @RequestParam Long skillId) {

    Skill skillEndorsed = skillRepository.getOne(skillId);
    Account endorsed = accountService.getById(endorsementTargetId);
    Account endorser = accountService.getById(endorsementSourceId);

    // check if endorsement already exists
    if (!skillEndorsed.getEndorsers().contains(endorser)) {

      List<Account> endorsers = skillEndorsed.getEndorsers();
      endorsers.add(endorser);

      List<Skill> currEndorsements = endorser.getEndorsements();
      currEndorsements.add(skillEndorsed);

      skillEndorsed.setEndorsers(endorsers);
      skillRepository.save(skillEndorsed);
      accountService.saveAccount(endorsed);
      accountService.saveAccount(endorser);
    }

    return "redirect:/users/" + endorsed.getProfileName();
  }

}