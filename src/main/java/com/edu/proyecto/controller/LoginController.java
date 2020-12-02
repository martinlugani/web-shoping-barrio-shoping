package com.edu.proyecto.controller;

import java.security.Principal;
import java.util.Locale;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

	@Autowired
    private MessageSource messageSource;
	
	@GetMapping("/login")
	public String login(@RequestParam(value="error", required=false) String error,
			@RequestParam(value="logout", required = false) String logout,
			Model model, Principal principal, RedirectAttributes flash, Locale locale,HttpSession session) {
		session.setAttribute("imagencli", "");
		if(principal != null) {
			
			flash.addFlashAttribute("info", "Te has logueado satisfactoriamente");
			return "redirect:/";
		}
		
		if(error != null) {
			model.addAttribute("error", "El usuario o la contraseña es incorrecta");
		}
		
		if(logout != null) {
			
			model.addAttribute("success", "Ha salido del sistema");
		}
		
		return "login";
	}
}
