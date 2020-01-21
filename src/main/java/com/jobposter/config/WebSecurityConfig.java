package com.jobposter.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.jobposter.config.JwtAuthenticationEntryPoint;
import com.jobposter.config.JwtRequestFilter;
import com.jobposter.service.UserService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
//
//	@Autowired
//	private UserDetailsService jwtUserDetailsService;
//
	@Autowired
	private UserService applicantService;
	
	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		// configure AuthenticationManager so that it knows from where to load
		// user for matching credentials
		// Use BCryptPasswordEncoder
		//auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
		auth.userDetailsService(applicantService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		// We don't need CSRF for this example
		httpSecurity.csrf().disable()
				// dont authenticate this particular request
				.authorizeRequests().antMatchers("/admin/role","/admin/role/{id}" ,"/admin/city/{id}","/admin/province/{id}", "/apl/register", "/admin/city",
						"/admin/province",  "/apl/login", "/apl/user/{id}", "/apl/user" , "/admin/apl-edu", "/admin/apl-edu/{id}", 
						"/admin/apl-work-exp", "/admin/apl-work-exp/{id}", "/admin/apl-proj", "/admin/apl-proj/{id}",
						"/admin/apl-skill", "/admin/apl-skill/{id}", "/admin/application", "/admin/application/{id}", 
						"/admin/edu-level", "/admin/edu-level/{id}", "/admin/job-category", "/admin/job-category/{id}", 
						"/admin/job-description", "/admin/job-description/{id}", "/admin/job-level", "/admin/job-level/{id}",
						"/admin/job-position", "/admin/job-position/{id}", "/admin/job-posting", "/admin/job-posting/{id}",
						"/admin/job-posting/filter" , "/admin/job-requirement", "/admin/job-requirement/{id}" , 
						"/admin/skill-level", "/admin/skill-level/{id}").permitAll().
				// all other requests need to be authenticated
				anyRequest().authenticated().and().
				// make sure we use stateless session; session won't be used to
				// store user's state.
				exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		// Add a filter to validate the tokens with every request
		httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}
}
