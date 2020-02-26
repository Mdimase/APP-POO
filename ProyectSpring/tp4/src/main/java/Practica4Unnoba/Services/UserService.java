package Practica4Unnoba.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

import Practica4Unnoba.Entities.Usuario;
import Practica4Unnoba.Repositories.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public List<Usuario> retrieveAllUsers() {
		return userRepository.findAll();
	}
	
	public void addUser(Usuario user) {		
	    userRepository.save(user);
	}
	
	public Usuario getUserById(Long id) {
	    return userRepository.findById(id).orElseThrow(() -> new NoSuchElementException());
	}
	
	public Usuario replaceUser(Usuario user, Long id) {
	    return userRepository.findById(id)
	      .map(u -> {
	        u.setEmail(user.getEmail());
	        u.setPassword(user.getPassword());
	        u.setFirstName(user.getFirstName());
	        u.setLastName(user.getLastName());
	        return userRepository.save(u);
	      })
	      .orElseGet(() -> {
	        return userRepository.save(user);
	      });
	}

	public void deleteUser(@PathVariable Long id) {
		userRepository.deleteById(id);
	}
	
	public Usuario findUserByUsername(String username) {
		return userRepository.findUserByUsername(username);
	}
	
	public Usuario findUserByEmail(String email) {
		return userRepository.findUserbyEmail(email);
	}
	
	//Obtengo el usuario logueado
	public Usuario getUser() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserDetails loggedUser = (UserDetails) principal;
		return findUserByEmail(loggedUser.getUsername());		//tener en cuenta que principal guarda como username el email del login
	}
	
}