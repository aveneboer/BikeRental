package nl.anouk.bikerental.config;

import nl.anouk.bikerental.filter.JwtRequestFilter;
import nl.anouk.bikerental.services.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
    private final JwtRequestFilter jwtRequestFilter;

    public SpringSecurityConfig(CustomUserDetailsService customUserDetailsService, JwtRequestFilter jwtRequestFilter) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtRequestFilter = jwtRequestFilter;
    }




    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    protected SecurityFilterChain filter (HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .httpBasic().disable()
                .cors().and()
                .authorizeHttpRequests()

              //  .requestMatchers("/**").permitAll() // alleen nu om methodes te kunnen draaien in Postman */

                .requestMatchers(HttpMethod.POST, "/users/create_user").permitAll()
                .requestMatchers(HttpMethod.GET,"/users").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/users/**").hasRole("ADMIN")

                .requestMatchers(HttpMethod.GET,"bikes/checkAvailability").permitAll()
                .requestMatchers(HttpMethod.GET,"/bikes/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET,"/bikes/all").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST,"/bikes/add").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/bikes/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/bikes/{id}").hasRole("ADMIN")

                .requestMatchers(HttpMethod.GET,"cars/checkAvailability").permitAll()
                .requestMatchers(HttpMethod.GET,"cars/cars").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET,"cars/findCars/{capacity}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST,"cars/addCar").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE,"cars/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH,"cars/{id}").hasRole("ADMIN")

                .requestMatchers(HttpMethod.GET,"/customers/customers").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET,"/customers/customer/{lastName}").hasAnyRole("ADMIN", "USER")
                .requestMatchers(HttpMethod.POST,"/customers/customers/{id}").permitAll()
                .requestMatchers(HttpMethod.DELETE,"/customers/customers/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH,"/customers/customers/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT,"/customers/link-to-user").permitAll()

                .requestMatchers(HttpMethod.POST,"/driverLicense/upload").permitAll()
                .requestMatchers(HttpMethod.GET,"/driverLicense/download/{id}").hasRole("ADMIN")

                .requestMatchers(HttpMethod.GET,"/reservations").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET,"/reservations/reservation/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST,"/reservations/create_reservation").permitAll()
                .requestMatchers(HttpMethod.DELETE,"/reservations/reservation/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/reservations/reservation/{id}").hasRole("ADMIN")



                .requestMatchers(HttpMethod.POST,"/reservation-line").permitAll()
                .requestMatchers(HttpMethod.GET,"/reservation-line/{id}").permitAll()
                .requestMatchers(HttpMethod.GET,"/reservation-lines").hasRole("ADMIN")


                .requestMatchers(HttpMethod.POST,"/reservations/create_reservation").permitAll()
                .requestMatchers(HttpMethod.POST,"/customers/add-customer").permitAll()

                .requestMatchers("/authenticated").authenticated()
                .requestMatchers(HttpMethod.POST,"/authenticate").permitAll()
                .anyRequest().denyAll()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}