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
		String view = "login";
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
			 view="login";
		 }
		 else {
			 String ckViolation = "ERROR:ya existe un usuario con ese email/username";
			 model.addAttribute("ckViolation",ckViolation);
			 view="add-user";
		 }
		 return view;
	}
}