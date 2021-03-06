package com.example.demo.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableAutoConfiguration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	DataSource dataSource;
 
	@Autowired
	public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource)
				.usersByUsernameQuery("select username, password, enabled from user where username=?")
				.authoritiesByUsernameQuery("select username, role from user_role where username=?");
	}
 
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
		.authorizeRequests()
			.antMatchers("/user/create").permitAll()
			.antMatchers("/kiosk").hasAuthority("ROLE_KIOSK")		
			.antMatchers("/kiosk/inquiry").hasAuthority("ROLE_KIOSK")
			.antMatchers("/user/get/id").hasAuthority("ROLE_KIOSK")			
			.antMatchers("/admin").hasRole("ADMIN")
			.anyRequest().authenticated().and().formLogin().loginPage("/login").permitAll().and().logout().permitAll();
		http.exceptionHandling().accessDeniedPage("/403");
	}
	
}
