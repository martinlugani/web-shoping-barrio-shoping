package com.edu.proyecto.controller;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.edu.proyecto.models.entity.Cliente;
import com.edu.proyecto.models.entity.Role;
import com.edu.proyecto.models.service.IClienteService;
import com.edu.proyecto.models.service.IComercioService;
import com.edu.proyecto.models.service.IProductoServices;

@Controller
@SessionAttributes("cliente")
@RequestMapping("/cliente")
public class ClienteController {

	private final Logger log = LoggerFactory.getLogger(getClass());
	@Autowired
	private IClienteService clienteService;
	@Autowired
	private IProductoServices productoService;
	@Autowired
	private IComercioService comercioService;

	@Autowired
	private MessageSource messageSource;

	@RequestMapping(value = "/form")
	public String crear(Map<String, Object> model, RedirectAttributes flash) {

		Cliente cliente = new Cliente();

		model.put("cliente", cliente);

		return "form";
	}

	@RequestMapping(value = "/form", method = RequestMethod.POST)
	public String guardar(@Valid Cliente cliente, BindingResult result, Model model , RedirectAttributes flash) {
		if (result.hasErrors()) {
			log.error("Error en el campo");
			return "form";
		}
		flash.addFlashAttribute("success","El cliente se ha creado con exito");
//		 userDetail.save(cliente.getUsuario());
		// guarda Cliente
		BCryptPasswordEncoder pass=new BCryptPasswordEncoder();
		cliente.setPassword(pass.encode(cliente.getPassword()));
		log.info("La contraseña es "+cliente.getPassword());
		List<Role>roles= new LinkedList<>();
		roles.add(new Role());
		roles.get(0).setAuthority("ROLE_CLIENTE");
		cliente.setRoles(roles);
		clienteService.save(cliente);
		return "redirect:/";
	}

	

}
