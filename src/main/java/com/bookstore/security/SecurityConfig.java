package com.bookstore.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	

	    @Bean
	    public BCryptPasswordEncoder bCryptPasswordEncoder(){
	        return new BCryptPasswordEncoder();
	    }

	    @Bean
	    public UserDetailsService userDetailsService(){
	        UserDetails user1 = User.builder()
	                .username("user")
	                .password(bCryptPasswordEncoder().encode("1234"))
	                .authorities("ROLE_USER")
	                .build();

	        UserDetails admin = User.builder()
	                .username("admin")
	                .password(bCryptPasswordEncoder().encode("1234"))
	                .authorities("ROLE_ADMIN")
	                .build();

	        return new InMemoryUserDetailsManager(user1,admin);
	    }

	    @Bean
	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
	        http
	                .headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
	                .csrf(AbstractHttpConfigurer::disable)
	                .formLogin(AbstractHttpConfigurer::disable)
	                .authorizeHttpRequests(req -> req.antMatchers("/h2/**").permitAll())
	                //.authorizeHttpRequests(req -> req.antMatchers("/bookstore/delete/*").hasRole("ROLE_ADMIN"))
	                .authorizeHttpRequests(req -> req.anyRequest().authenticated())
	                .httpBasic(Customizer.withDefaults());
	        return http.build();
	    }
   
}