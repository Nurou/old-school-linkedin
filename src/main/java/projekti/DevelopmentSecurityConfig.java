package projekti;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Profile("!prod")
@Configuration
@EnableWebSecurity
public class DevelopmentSecurityConfig extends WebSecurityConfigurerAdapter {

  @Override
  public void configure(WebSecurity sec) throws Exception {
    sec.ignoring().antMatchers("/**");
  }
}