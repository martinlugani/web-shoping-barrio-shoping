package com.edu.proyecto;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.edu.proyecto.app.auth.handler.LoginSuccessHandler;
import com.edu.proyecto.models.service.JpaUserDetailsService;




@EnableGlobalMethodSecurity(securedEnabled=true, prePostEnabled=true)
//Clase de configuracion de seguridad
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private LoginSuccessHandler successHandler;
	
	@Autowired
	private JpaUserDetailsService userDetailsService;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	/**
	 * Metodo que sirve para autorizar las rutas para el acceso de los roles
	 * @param HttpSecurity 
	 */
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//Rutas publicas agregar aca
		http.authorizeRequests().antMatchers("/", "/css/**", "/js/**", "/images/**", "/listar", "/cliente/**","/productos/**","/uploads/**","/js/adminlte").permitAll()
		//Rutas Privadas
		/*.antMatchers("/ver/**").hasAnyRole("USER")*/
		/*.antMatchers("/uploads/**").hasAnyRole("USER")*/
		/*.antMatchers("/form/**").hasAnyRole("ADMIN")*/
		/*.antMatchers("/eliminar/**").hasAnyRole("ADMIN")*/
		/*.antMatchers("/factura/**").hasAnyRole("ADMIN")*/
		.anyRequest().authenticated()
		.and()
	    .formLogin()
	        .successHandler(successHandler)
	        .loginPage("/login")
	    .permitAll()
	.and()
	.logout().permitAll()
	.and()
	.exceptionHandling().accessDeniedPage("/error_403");
		;
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder builder) throws Exception {
		
		
			builder.userDetailsService(userDetailsService)
			.passwordEncoder(passwordEncoder);
		
		

	}
}
