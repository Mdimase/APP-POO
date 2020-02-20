package Practica4Unnoba.Services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;

import org.springframework.stereotype.Component;

import Practica4Unnoba.Entities.Usuario;

import java.util.NoSuchElementException;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private UserService userService;
	
	@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
 
    	try { 
    		Usuario u = userService.findUserByUsername(username);
    		
    		List grantedAuthorities = new ArrayList();
    		GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(u.getRole());
    		grantedAuthorities.add(grantedAuthority);
    		
    		UserDetails user = (UserDetails) new User(u.getUsername(), u.getPassword(), grantedAuthorities);
    		
    		System.out.println("username = " + user.getUsername() + " " + "password = " + user.getPassword());
   
    		return user;
    	}	
    	catch(NoSuchElementException e) {
    		throw new UsernameNotFoundException("User not found!");
    	}
   
    }

}
