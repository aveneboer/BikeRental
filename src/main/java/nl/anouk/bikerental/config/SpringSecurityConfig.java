package nl.anouk.bikerental.config;

import nl.anouk.bikerental.filter.JwtRequestFilter;
import nl.anouk.bikerental.services.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {
private final CustomUserDetailsService customUserDetailsService;

    public SpringSecurityConfig(CustomUserDetailsService customUserDetailsService, JwtRequestFilter jwtRequestFilter) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    private final JwtRequestFilter jwtRequestFilter;

    /*inject customUserDetailService en jwtRequestFilter*/


    // Authenticatie met customUserDetailsService en passwordEncoder
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }


    // PasswordEncoderBean. Deze kun je overal in je applicatie injecteren waar nodig.
    // Je kunt dit ook in een aparte configuratie klasse zetten.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Authorizatie met jwt
    @Bean
    protected SecurityFilterChain filter (HttpSecurity http) throws Exception {
//paden zelf toevoegen, schrijf per post get en delete//
        //JWT token authentication
        http
                .csrf().disable()
                .httpBasic().disable()
                .cors().and()
                .authorizeHttpRequests()

                .requestMatchers("/**").permitAll() // alleen nu om methodes te kunnen draaien in Postman

//                .requestMatchers(HttpMethod.POST, "/users").permitAll()
//                .requestMatchers(HttpMethod.GET,"/users").hasRole("ADMIN")
//                .requestMatchers(HttpMethod.POST,"/users/**").hasRole("ADMIN")
//                .requestMatchers(HttpMethod.DELETE, "/users/**").hasRole("ADMIN")
//
//                .requestMatchers(HttpMethod.GET, "/bikes").permitAll()
//                .requestMatchers(HttpMethod.GET,"/bike/{id}").hasRole("ADMIN")
//                .requestMatchers(HttpMethod.POST,"/bikes").hasRole("ADMIN")
//                .requestMatchers(HttpMethod.DELETE, "/bikes/{id}").hasRole("ADMIN")
//
//                .requestMatchers(HttpMethod.GET,"/customers").hasRole("ADMIN")
//                .requestMatchers(HttpMethod.GET,"/reservations").hasRole("ADMIN")
//                .requestMatchers(HttpMethod.POST,"/customers/**").hasRole("ADMIN")
//                .requestMatchers(HttpMethod.DELETE, "/users/**").hasRole("ADMIN")
//                .requestMatchers(HttpMethod.POST, "/").permitAll()
//
//                .requestMatchers(HttpMethod.GET,"/bikes/all").hasRole("USER")
//                .requestMatchers(HttpMethod.GET,"/cars").hasRole("USER")
//
//                .requestMatchers(HttpMethod.POST,"/reservation/{id}").hasRole("USER")
//                .requestMatchers(HttpMethod.DELETE, "/reservation/{id}").hasRole("USER")
//
//                .requestMatchers(HttpMethod.GET,"/reservations").hasRole("USER")
//                .requestMatchers(HttpMethod.POST,"/reservation").hasRole("USER")
//                .requestMatchers(HttpMethod.DELETE, "/reservation/{id}").hasRole("USER")
//                .requestMatchers(HttpMethod.PATCH, "/reservations/{id}").hasRole("USER")
//
 //               .requestMatchers(HttpMethod.POST, "/single/uploadeDb").hasAnyRole("ADMIN", "USER")
//
//              //  .requestMatchers("/**").authenticated() is een wildcard, je mag overal bij zolang je maar ingelogd bent ongeacht user of admin
//                .requestMatchers("/authenticated").authenticated()
//                .requestMatchers("/authenticate").permitAll()/*allen dit punt mag toegankelijk zijn voor niet ingelogde gebruikers*/
                .anyRequest().denyAll()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}