package sn.esmt.gesb.tpo_manager.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
//@EnableWebSecurity
public class CustomWebSecurityConfigurerAdapter {

    @Autowired
    private RestAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("user1")
                .password(passwordEncoder().encode("user1Pass"))
                .authorities("ROLE_USER");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(expressionInterceptUrlRegistry ->
                        expressionInterceptUrlRegistry.requestMatchers("/api/tpo-manager").permitAll()
                                .anyRequest().authenticated())
                .httpBasic(httpSecurityHttpBasicConfigurer -> httpSecurityHttpBasicConfigurer.authenticationEntryPoint(authenticationEntryPoint));
//        http.addFilterAfter(new CustomFilter(), BasicAuthenticationFilter.class);
        return http.build();
    }

}
