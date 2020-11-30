package com.edu.proyecto.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.edu.proyecto.models.entity.Cliente;
import com.edu.proyecto.models.entity.Comercio;
import com.edu.proyecto.models.entity.Role;
import com.edu.proyecto.models.service.IClienteService;
import com.edu.proyecto.models.service.IComercioService;
import com.edu.proyecto.models.service.IUploadFileService;

@Controller

@RequestMapping("/")
public class ComercioControler {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private IComercioService comercioService;
	@Autowired
	private IUploadFileService uploadFileService;
	@Autowired
	private IClienteService clienteService;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	private Comercio comercio;
	private Cliente cliente;

	@Secured("ROLE_COMERCIO")
	@GetMapping("/comercio/ver")
	public String mostrarCliente(Model model, Authentication auth, HttpSession session) {
		log.info("imagen session ".concat(session.getAttribute("imagencli").toString()));
		comercio = comercioService.findByUsername(auth.getName());

		comercio.setPassword("");

		model.addAttribute("titulo", "Ver cliente");
		model.addAttribute("cliente", comercio);
		return "comercio/ver";
	}

	@GetMapping("/")
	public String listar(@RequestParam(value = "error", required = false) String error, Model mode,
			Authentication authentication, HttpServletRequest request, HttpSession sesion) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		comercio = comercioService.findByUsername(auth.getName());
		cliente = clienteService.findByName(auth.getName());
		sesion.setAttribute("imagencli",
				(cargaImagen(auth, authentication) == null) ? "" : cargaImagen(auth, authentication));
		if (error != null) {
			mode.addAttribute("error", error);
		}
		if (request.isUserInRole("ROLE_COMERCIO")) {

			log.info("Forma  role_comercio  tienes acceso!".concat(comercio.toString()));
//			return "redirect:/pedido/" + comercio.getId().toString();
			mode.addAttribute("comercio", comercio.getId());
			
			mode.addAttribute("productos", comercio.getProductos());

			return "producto/listado-productos";
		}
		List<Comercio> comercios = comercioService.findAll();
		if (authentication != null) {

			log.info("Hola usuario autenticado, tu username es: ".concat(authentication.getName()) + " getAuthoryties "
					+ authentication.getAuthorities() + " credentials " + authentication.getCredentials() + "   ");

		} else {
			log.info("Su usuario no esta autenticado");
		}

		if (request.isUserInRole("ROLE_CLIENTE")) {
			log.info("Forma usando HttpServletRequest: Hola ".concat(auth.getName()).concat(" tienes acceso!"));
		} else {
			log.info("Forma usando HttpServletRequest: Hola ".concat(auth.getName()).concat(" NO tienes acceso!"));
		}

//		log.info(sesion.getAttribute("imagencli").toString());

		mode.addAttribute("comercios", comercios);
		return "comercio/listado-comercios";
//		return "index";
	}

	@RequestMapping(value = "/comercio/form", method = RequestMethod.POST)
	public String guardar(@Valid @ModelAttribute Comercio comercio, BindingResult result, Model model,
			RedirectAttributes flash, @RequestParam("file") MultipartFile foto)
			throws SQLIntegrityConstraintViolationException {
		log.info("Entro Sssss");

//		log.error(foto.getOriginalFilename());
		if (validarComercio(comercio)) {

			if (modificaComercio(comercio, foto)) {
				flash.addFlashAttribute("success", "Se ha actualizado el usuario");
			} else {
				flash.addFlashAttribute("error", "No se han actualizado los cambios");
				return "redirect:/";
			}

			return "redirect:/";
		}
		if (result.hasErrors()) {
			log.error("Error en el campo");
//	
		}
		comercio = guardaFoto(foto, comercio);

//		 userDetail.save(cliente.getUsuario());
		// guarda Cliente

		List<Role> roles = new LinkedList<>();
		roles.add(new Role());
		roles.get(0).setAuthority("ROLE_COMERCIO");
		comercio.setRoles(roles);
		try {
			comercioService.saveError(comercio);

		} catch (Exception e) {
			// TODO: handle exception
			log.error("Error  " + e.getMessage());
			model.addAttribute("dupliErr", e.getMessage());
			return "form";
		}
		flash.addFlashAttribute("success", "El cliente se ha creado con exito");
		return "redirect:/login";
	}

	private @Valid Comercio guardaFoto(MultipartFile foto, @Valid Comercio comercio) {
		if (!foto.isEmpty()) {

			String uniqueFile = null;
			try {
				uniqueFile = uploadFileService.copy(foto, comercio.getId());
			} catch (IOException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			comercio.setImage(uniqueFile);

		}
		return comercio;
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

	@GetMapping(value = "/uploads/{filename:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable(value = "filename") String filename) {
		comercio=comercioService.findByImage(filename);
		Resource recurso = null;
		try {
			recurso = uploadFileService.load(filename, comercio.getId());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
				.body(recurso);
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

	/**
	 * busca la foto de el cliente o de el comercio
	 * 
	 * @param auth           de Authentication
	 * @param authentication
	 * @return String retorna el nombre de la foto
	 */
	private String cargaImagen(Authentication auth, Authentication authentication) {
		String imagen = null;
		if (authentication != null) {

			imagen = (comercio != null && comercio.getImage() != null) ? comercio.getImage() : null;
			if (imagen == null)
				imagen = (cliente != null && cliente.getImagen() != null) ? cliente.getImagen() : null;
		}
		return imagen;

	}

	private boolean validarComercio(@Valid Comercio comercio) {
		// comprueva que exista el usuario
		if (comercio.getId() == null) {

			return false;
		}
		return true;
	}

	private boolean modificaComercio(@Valid Comercio comercio, MultipartFile foto) {

		// comprueva que exista el usuario

		Comercio comercioOriginal = comercioService.findById(comercio.getId());

//				Comprueva que el paswor sea valido
		if (passwordEncoder.matches(comercio.getPassword(), comercioOriginal.getPassword())) {

			// si la contraseña nueva tiene valores
			if (comercio.getRepeatPass().length() > 0) {
//						comprueba que la original y la nueva sean diferentes
				if (!passwordEncoder.matches(comercio.getRepeatPass(), comercioOriginal.getPassword())) {

//					asigna la nueva contraseña
					comercioOriginal.setPassword(passwordEncoder.encode(comercio.getRepeatPass()));

				}
			}

			eliminarFoto(comercioOriginal, comercio, foto);
//			Aca  guarda ya sea si es null guarda la original si no guarda la nueva
			comercioOriginal
					.setEmail((comercio.getEmail() == null) ? comercioOriginal.getEmail() : comercio.getEmail());
			comercioOriginal = guardaFoto(foto, comercioOriginal);
			comercioOriginal.setRazonSocial(comercio.getRazonSocial());
			comercioOriginal.setDireccion(comercio.getDireccion());
			comercioOriginal.setDescripcion(comercio.getDescripcion());
			comercioOriginal.setRepeatPass(comercio.getRepeatPass().equals("") ? "valida" : comercio.getRepeatPass());
			comercioService.save(comercioOriginal);

		} else {
			return false;
		}
		log.info("Contraseña");
//	}

//		clienteService.save(cliente);
		return true;
	}

	private void eliminarFoto(Comercio comercioOriginal, @Valid Comercio comercio, MultipartFile foto) {
		// TODO Auto-generated method stub
		if (!foto.getOriginalFilename().equals("")) {
			uploadFileService.delete(comercioOriginal.getImage());
		}
	}
}
