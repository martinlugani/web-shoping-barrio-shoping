package com.edu.proyecto.controller;

import java.net.MalformedURLException;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.edu.proyecto.models.entity.Cliente;
import com.edu.proyecto.models.entity.Comercio;
import com.edu.proyecto.models.service.IClienteService;
import com.edu.proyecto.models.service.IComercioService;
import com.edu.proyecto.models.service.IProductoServices;
import com.edu.proyecto.models.service.IUploadFileService;

@Controller
@SessionAttributes("comercio")
@RequestMapping("/")
public class ComercioControler {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private IComercioService comercioService;
	@Autowired
	private IUploadFileService uploadFileService;
	@Autowired
	private IClienteService clienteService;

	@GetMapping("/")
	public String listar(@RequestParam(value="error", required=false) String error,Model mode, Authentication authentication, HttpServletRequest request, HttpSession sesion) {
		if (error!= null) {
		mode.addAttribute("error",error);
		}
		if (request.isUserInRole("ROLE_COMERCIO")) {

			Comercio comercio = comercioService.findByUsername(authentication.getName());

			log.info("Forma  role_comercio  tienes acceso!".concat(comercio.toString()));
			return "redirect:/pedido/" + comercio.getId().toString();

		}
		List<Comercio> comercios = comercioService.findAll();
		if (authentication != null) {

			log.info("Hola usuario autenticado, tu username es: ".concat(authentication.getName()) + " getAuthoryties "
					+ authentication.getAuthorities() + " credentials " + authentication.getCredentials() + "   ");

		} else {
			log.info("Su usuario no esta autenticado");
		}
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (request.isUserInRole("ROLE_CLIENTE")) {
			log.info("Forma usando HttpServletRequest: Hola ".concat(auth.getName()).concat(" tienes acceso!"));
		} else {
			log.info("Forma usando HttpServletRequest: Hola ".concat(auth.getName()).concat(" NO tienes acceso!"));
		}
		Cliente cliente = clienteService.findByName(auth.getName());
		if (authentication != null) {
			if(cliente.getImagen()==null) {
				sesion.setAttribute("imagencli", null);
			}else {
			sesion.setAttribute("imagencli", cliente.getImagen());
			}
		}else {
			sesion.setAttribute("imagencli",null);
		}
		mode.addAttribute("comercios", comercios);
		return "comercio/listado-comercios";
//		return "index";
	}

	@GetMapping("/carga")
	public String starter(Model model) {
		return "pruevas";
	}

	@PostMapping("/starter")
	public String cambiosPrueva(Model model, @RequestParam(value = "texto") String texto) {

		log.info("texto recibido".concat(texto));

		return "redirect:/carga";
	}

	private boolean hasRole(String role) {

		SecurityContext context = SecurityContextHolder.getContext();

		if (context == null) {
			return false;
		}

		Authentication auth = context.getAuthentication();

		if (auth == null) {
			return false;
		}

		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();

		return authorities.contains(new SimpleGrantedAuthority(role));

	}

	@GetMapping(value = "/uploads/{filename:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable(value = "filename") String filename) {

		Resource recurso = null;

		try {
			recurso = uploadFileService.load(filename, null);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
				.body(recurso);
	}
}
