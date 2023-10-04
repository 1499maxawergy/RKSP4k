package ru.maxawergy.authservice.config;

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import ru.maxawergy.authservice.model.User;
import ru.maxawergy.authservice.repository.UserRepository;

import java.util.UUID;

@Configuration
@EnableWebSecurity
@EnableOAuth2Sso
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .requestMatchers(new AntPathRequestMatcher("/")).permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .csrf().disable()
                .cors().disable();
    }

    @Bean
    public PrincipalExtractor principalExtractor(UserRepository userDetailsRepo) {
        return map -> {
            UUID uuid = UUID.randomUUID();
            System.out.println(uuid);

            User user = userDetailsRepo.findUserByEmail((String) map.get("email"));
            if (user == null){
                User newUser = new User((String) map.get("name"), (String) map.get("email"), uuid.toString());
                return userDetailsRepo.save(newUser);
            }

            user.setToken(uuid.toString());
            return userDetailsRepo.save(user);
        };
    }
}

