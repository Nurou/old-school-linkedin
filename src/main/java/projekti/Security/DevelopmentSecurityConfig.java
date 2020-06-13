package projekti.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Profile("!prod")
@Configuration
@EnableWebSecurity
public class DevelopmentSecurityConfig extends WebSecurityConfigurerAdapter {

  private final PasswordEncoder passwordEncoder;

  @Autowired
  public DevelopmentSecurityConfig(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  // @Autowired
  // private CustomUserDetailsService customUserDetailsService;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // remove csrf for H2 console
    http.csrf().disable();
    // permit frame use
    http.headers().frameOptions().sameOrigin();
    http.authorizeRequests().antMatchers("/h2-console", "/h2-console/**").permitAll().antMatchers("/", "/register")
        .permitAll().antMatchers("/users/**").authenticated().anyRequest().authenticated();
    http.formLogin().permitAll();
  }

  // @Autowired
  // public void configureGlobal(AuthenticationManagerBuilder auth) throws
  // Exception {
  // auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
  // }

  // @Bean
  // public PasswordEncoder passwordEncoder() {
  // return new BCryptPasswordEncoder();
  // }

  @Override
  @Bean
  protected UserDetailsService userDetailsService() {
    UserDetails fake = User.builder().username("fake").password(passwordEncoder.encode("password")).roles("").build();

    return new InMemoryUserDetailsManager(fake);

  }
}