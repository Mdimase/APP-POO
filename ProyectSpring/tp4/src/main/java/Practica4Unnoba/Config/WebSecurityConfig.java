package Practica4Unnoba.Config;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import Practica4Unnoba.Services.UserDetailsServiceImpl;

//indico a spring que esta clase es de configuracion
@Configuration
//indica a la configuración de Spring que se va a redefinir métodos de las clase WebSecurityConfigurerAdapter
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/", "/index", "/signup", "/adduser", "add-user").permitAll()		//estas rutas entran sin loguearse
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .loginPage("/login")			//esta ruta tiene acceso publico, donde se loguean los usuarios
                .permitAll()
                .and()
            .logout()
            	.logoutSuccessUrl("/index")		//cierro sesion y lo mando al index
                .permitAll();
    }

    //inyeccion de dependencia del Bcrypct que se encarga de encriptar las claves
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
    }
    
    //genero las credenciales
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    	auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder()); 
    }

}
