package it.uniroma3.siw.authenticator;

import static it.uniroma3.siw.model.Credentials.ADMIN_ROLE;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class AuthConfiguration {
 @Autowired
    private DataSource dataSource;

    // Configurazione per l'autenticazione basata su JDBC
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
            .dataSource(dataSource)
            .authoritiesByUsernameQuery("SELECT username, role FROM credentials WHERE username=?")
            .usersByUsernameQuery("SELECT username, password, 1 as enabled FROM credentials WHERE username=?");
    }

    // Bean per la codifica delle password
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Configurazione della catena di sicurezza
    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.disable())
            .authorizeHttpRequests(authz -> authz
                 .requestMatchers(HttpMethod.GET, "/", "/home","/commenti/**", "/register", "/login","/prodotti","/prodotto/**","/categoria/**", "/login/**", "/css/**", "/images/**", "/favicon.ico").permitAll()
                 .requestMatchers(HttpMethod.POST, "/register", "/login").permitAll()
                 .requestMatchers(HttpMethod.GET, "/user/").authenticated()                
                 .requestMatchers(HttpMethod.GET, "/admin/**").hasAuthority(ADMIN_ROLE)
                 .requestMatchers(HttpMethod.POST, "/admin/**").hasAuthority(ADMIN_ROLE)
                 .anyRequest().authenticated()
            	
            )

            .formLogin(form -> form
                .loginPage("/login")
                .permitAll()
                .defaultSuccessUrl("/home", true)
                .failureUrl("/login?error=true")
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
            );
        return http.build();
        //return
    }
}