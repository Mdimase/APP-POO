package Practica4Unnoba.Controllers;

import java.sql.SQLException;
import java.util.List;

import javax.validation.Valid;

import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;

import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.WebDataBinder;
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
		String view = "index";
		//errores en la validacion de formulario
		if (result.hasErrors()) {
			view="add-user";
	     }
		 
		 //Encriptación y seteo de la clave al usuario que se registra.
		 BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		 String passwordEncrypted = bCryptPasswordEncoder.encode(usuario.getPassword());
		 usuario.setPassword(passwordEncrypted);
		 
		 //restriccion acepto email y username no duplicados 
		 if(userService.findUserByUsername(usuario.getUsername())==null && userService.findUserByEmail(usuario.getEmail())==null) {
			 userService.addUser(usuario);
			 view="index";
		 }
		 else {
			 System.out.println("CK Violation");
			 model.addAttribute("ckViolation",new Boolean(true));
			 view="add-user";
		 }
		 return view;
	}
	
	@GetMapping("/users")
	public List<Usuario> retrieveAllUsers() {
		return userService.retrieveAllUsers();
	}
	
	@GetMapping("/users/edit/{id}")
	public Usuario getUser(@PathVariable Long id) {
	    return userService.getUserById(id);
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