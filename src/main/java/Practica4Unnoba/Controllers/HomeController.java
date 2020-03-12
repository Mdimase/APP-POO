package Practica4Unnoba.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
	
	@GetMapping({"/", "/login"})
	public String userLogin() {
		return "login";
	}
	
	@GetMapping("/home")
	public String mainMenu() {
		return "home";
	}
}
