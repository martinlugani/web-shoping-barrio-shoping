package com.edu.proyecto.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.edu.proyecto.models.entity.Cliente;
import com.edu.proyecto.models.entity.Role;
import com.edu.proyecto.models.service.IClienteService;
import com.edu.proyecto.models.service.IComercioService;
import com.edu.proyecto.models.service.IProductoServices;
import com.edu.proyecto.models.service.IUploadFileService;
import com.sun.xml.bind.v2.runtime.output.Encoded;

@Controller

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
	@Autowired
	private IUploadFileService uploadFileService;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Secured("ROLE_CLIENTE")
	@GetMapping("/ver")
	public String mostrarCliente(Model model, Authentication auth) {

		Cliente cliente = clienteService.findByName(auth.getName());

		cliente.setPassword("");

		model.addAttribute("titulo", "Ver cliente");
		model.addAttribute("cliente", cliente);
		return "usuario/ver";
	}

	@RequestMapping(value = "/form")
	public String crear(Model model, RedirectAttributes flash) {

		Cliente cliente = new Cliente();
		model.addAttribute("imagencli", "");
		model.addAttribute("cliente", cliente);

		return "form";
	}

	@GetMapping("/elimina/foto/{id}")
	public String elimina() {
		return "redirect: /cliente/imagenCliente/image-not-found.png";
	}

	@GetMapping(value = "/imagenCliente/{filename:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable(value = "filename") String filename) {
		log.info("Entro imagen cliente cargar");
		Resource recurso = null;

		if (filename.equals("image-not-found.png")) {

		}

		try {
			recurso = uploadFileService.load(filename, null);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
				.body(recurso);
	}

	@RequestMapping(value = "/form", method = RequestMethod.POST)
	public String guardar(@Valid @ModelAttribute Cliente cliente, BindingResult result, Model model,
			RedirectAttributes flash, @RequestParam("file") MultipartFile foto)
			throws SQLIntegrityConstraintViolationException {
		log.info("Entro Sssss");

//		log.error(foto.getOriginalFilename());
		if (validarCliente(cliente)) {

			if (modificaCliente(cliente, foto)) {
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
		cliente = guardaFoto(foto, cliente);

//		 userDetail.save(cliente.getUsuario());
		// guarda Cliente

		List<Role> roles = new LinkedList<>();
		roles.add(new Role());
		roles.get(0).setAuthority("ROLE_CLIENTE");
		cliente.setRoles(roles);
		try {
			clienteService.saveError(cliente);

		} catch (Exception e) {
			// TODO: handle exception
			log.error("Error  " + e.getMessage());
			model.addAttribute("dupliErr", e.getMessage());
			model.addAttribute("result", result);
			log.error("Error  "+ result.toString());
			return "form";
		}
		flash.addFlashAttribute("success", "El cliente se ha creado con exito");
		return "redirect:/login";
	}

	/**
	 * Guarda foto si la hay
	 * 
	 * @param foto    MultipartFile
	 * @param cliente Cliente
	 */
	private Cliente guardaFoto(MultipartFile foto, @Valid Cliente cliente) {
		if (!foto.isEmpty()) {

			String uniqueFile = null;
			try {
				uniqueFile = uploadFileService.copy(foto, null);
			} catch (IOException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			cliente.setImagen(uniqueFile);

		}
		return cliente;

	}

	/**
	 * Comprueva si el cliente existe en el sistema
	 * 
	 * @param cliente Cliente
	 * @return Boolean
	 */
	private boolean validarCliente(@Valid Cliente cliente) {
		// comprueva que exista el usuario
		if (cliente.getId() == null) {

			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param cliente Cliente class
	 * @param foto    MultipartFile
	 * @return Boolean
	 */

	private boolean modificaCliente(@Valid Cliente cliente, MultipartFile foto) {

		// comprueva que exista el usuario

		Cliente clienteOriginal = clienteService.findById(cliente.getId());

//		Comprueva que el paswor sea valido
		if (passwordEncoder.matches(cliente.getPassword(), clienteOriginal.getPassword())) {

			// si la contraseña nueva tiene valores
//			if (cliente.getRepeatPass() != null && cliente.getRepeatPass().length() == 0) {
//				cliente.setRepeatPass("repeatPass");
//				log.info(clienteOriginal.toString().concat(" repeat ".concat(cliente.getRepeatPass())));
			if (cliente.getRepeatPass().length() > 0) {
//			comprueba que la original y la nueva sean diferentes
				if (!passwordEncoder.matches(cliente.getRepeatPass(), clienteOriginal.getPassword())) {

//				asigna la nueva contraseña
					clienteOriginal.setPassword(passwordEncoder.encode(cliente.getRepeatPass()));
				}
			}
			eliminarFoto(clienteOriginal, cliente, foto);
//			Aca  guarda ya sea si es null guarda la original si no guarda la nueva
			clienteOriginal.setEmail((cliente.getEmail() == null) ? clienteOriginal.getEmail() : cliente.getEmail());
			clienteOriginal = guardaFoto(foto, clienteOriginal);
			clienteOriginal.setRepeatPass(cliente.getRepeatPass().equals("") ? "valida" : cliente.getRepeatPass());
			clienteService.save(clienteOriginal);
		} else {
			return false;
		}
		log.info("Contraseña");
//	}

//		clienteService.save(cliente);
		return true;
	}

	private void eliminarFoto(Cliente clienteOriginal, @Valid Cliente cliente, MultipartFile foto) {
		// TODO Auto-generated method stub
		if (!foto.getOriginalFilename().equals("")) {
			uploadFileService.delete(clienteOriginal.getImagen());
		}
	}

}
