package Practica4Unnoba.Controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;

import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import Practica4Unnoba.Entities.Usuario;
import Practica4Unnoba.Services.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;
	
	@GetMapping("/signup")
	public String showSignUpForm(Usuario usuario) {
		return "add-user";
	}
	
	@PostMapping("/adduser")
	public String addUser(@Valid Usuario usuario, BindingResult result, Model model) {
		 if (result.hasErrors()) {
			 return "add-user";
	     }
		 
		 /*
		  * Encriptación y seteo de la clave al usuario que se registra.
		  */
		 BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		 String passwordEncrypted = bCryptPasswordEncoder.encode(usuario.getPassword());
		 usuario.setPassword(passwordEncrypted);
		 
		 userService.addUser(usuario);
		 
		 //System.out.println("usernane = " + usuario.getUsername() + ", password = " + usuario.getPassword() + ", fistname = " + usuario.getFirstName() + ", lastname = " + usuario.getLastName());
		 
	     return "index";
	}
	
	@GetMapping("/users")
	public List<Usuario> retrieveAllUsers() {
		return userService.retrieveAllUsers();
	}
	/*
	@PostMapping("/users")
	public void addUser(@RequestBody Usuario user) {
		userService.addUser(user);
	}
	*/
	@GetMapping("/users/edit/{id}")
	public Usuario getUser(@PathVariable Long id) {
	    return userService.getUser(id);
	}
	
	@PutMapping("/users/update/{id}")
	public Usuario replaceUser(@RequestBody Usuario user, @PathVariable Long id) {
	    return userService.replaceUser(user, id);
	}

	@DeleteMapping("/users/delete/{id}")
	void deleteUser(@PathVariable Long id) {
		  userService.deleteUser(id);
	  }
}