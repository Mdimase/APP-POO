package Practica4Unnoba.Controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;

import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
	public String addUser(@Valid Usuario usuario, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		//Agregado por lepo.
		String message = null;
		String classMessage = null;
		
		//errores en la validacion de formulario
		if (result.hasErrors()) {
			return "add-user";
	    }	
		 
		userService.encrypt(usuario);
		
		//restriccion acepto email no duplicados (solo acepto si no esta duplicado)
		if(userService.isDuplicated(usuario)) {
			message = "<b>Error:</b> ya existe un usuario con ese email/username.";
			classMessage = "alert-danger";
			redirectAttributes.addFlashAttribute("message", message);
			redirectAttributes.addFlashAttribute("classMessage", classMessage);
			return "redirect:/signup";
		}
		
		//todo bien, lo registro
		userService.addUser(usuario);
		//Agregado por lepo.
		message = "Se registro correctamente el nuevo usuario.";
		classMessage = "alert-success";
		redirectAttributes.addFlashAttribute("message", message);
		redirectAttributes.addFlashAttribute("classMessage", classMessage);
		
		return "redirect:/login";
	}
}