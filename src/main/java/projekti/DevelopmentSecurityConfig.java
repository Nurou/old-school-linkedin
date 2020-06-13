package projekti;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Profile("!prod")
@Configuration
@EnableWebSecurity
public class DevelopmentSecurityConfig extends WebSecurityConfigurerAdapter {

  @Override
  public void configure(HttpSecurity http) throws Exception {
    // poistetaan csrf-tarkistus käytöstä h2-konsolin vuoksi
    http.csrf().disable();
    // sallitaan framejen käyttö
    http.headers().frameOptions().sameOrigin();

    http.authorizeRequests().antMatchers("/h2-console", "/h2-console/**").permitAll().antMatchers("/").permitAll()
        .antMatchers("/users/**").authenticated().antMatchers("/register").permitAll().anyRequest().authenticated();
    http.formLogin().permitAll();

  }
}