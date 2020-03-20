package Practica4Unnoba.Services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

import Practica4Unnoba.Entities.Invite;
import Practica4Unnoba.Entities.Usuario;
import Practica4Unnoba.Repositories.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public List<Usuario> getAllUsers() {
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
	public Usuario getUserLogged() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserDetails loggedUser = (UserDetails) principal;
		return findUserByEmail(loggedUser.getUsername());		//tener en cuenta que principal guarda como username el email del login
	}
	
	//retorna una lista de usuarios a partir de una lista de invitaciones enviadas
	public List<Usuario> getUsersInvitated (List<Invite>invitationsSent){
		List<Usuario> usersInvitated = new ArrayList<Usuario> ();
		for(Invite i : invitationsSent) {
			usersInvitated.add(this.getUserById(i.getUser().getId()));
		}
		return usersInvitated;
	}
	
	//Encriptación y seteo de la clave al usuario que se registra.
	public Usuario encrypt(Usuario usuario) {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		String passwordEncrypted = bCryptPasswordEncoder.encode(usuario.getPassword());
		usuario.setPassword(passwordEncrypted);
		return usuario;
	}
	
	//retorna true si hay un usuario con ese mail/username
	public boolean isDuplicated(Usuario usuario) {
		if(findUserByEmail(usuario.getEmail())!=null) {
			return true;
		}
		if(this.findUserByUsername(usuario.getUsername()) != null) {
			return true;
		}
		return false;
	}
	
}