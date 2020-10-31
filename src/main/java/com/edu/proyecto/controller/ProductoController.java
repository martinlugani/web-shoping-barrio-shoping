package com.edu.proyecto.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.edu.proyecto.models.entity.Cliente;
import com.edu.proyecto.models.entity.Comercio;
import com.edu.proyecto.models.entity.Pedido;
import com.edu.proyecto.models.entity.Producto;
import com.edu.proyecto.models.entity.UnidadMedida;
import com.edu.proyecto.models.entity.Usuario;
import com.edu.proyecto.models.service.IClienteService;
import com.edu.proyecto.models.service.IComercioService;
import com.edu.proyecto.models.service.IProductoServices;
import com.edu.proyecto.models.service.IUploadFileService;
import com.edu.proyecto.models.service.UploadFileServiceImpl;

@Controller
@RequestMapping("/productos")
public class ProductoController {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private IComercioService comercioService;
	@Autowired
	private IClienteService clienteService;
	@Autowired
	private IProductoServices productoService;

	@Autowired
	private IUploadFileService uploadFileService;

	@GetMapping(value = "/uploads/{id}/{filename:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable(value = "id") Long id,
			@PathVariable(value = "filename") String filename) {

		Resource recurso = null;

		try {
			recurso = uploadFileService.load(filename, id);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
				.body(recurso);
	}

	

	@Secured({ "ROLE_COMERCIO" })
	@RequestMapping(value = "/form/{comercioId}")
	public String crear(@PathVariable(value = "comercioId") Long comercioId, Model model, RedirectAttributes flash) {

		Comercio comercio = comercioService.findById(comercioId);
		Producto producto = new Producto();
		producto.setComercio(comercio);
		List<UnidadMedida> medidas = productoService.findAllMedida();
//		flash.addFlashAttribute("mensage", "1");
		producto.setState(true);
		model.addAttribute("mensaje", "Solo a efectos de desarrollo 1".concat(comercio.toString()));
		model.addAttribute("producto", producto);
		model.addAttribute("medidas", medidas);
		return "comercio/form_producto";
	}

	@RequestMapping(value = "/form/{comercioId}", method = RequestMethod.POST)
	public String guardar(@PathVariable(value = "comercioId") Long comercioId, @Valid Producto producto,
			BindingResult result, Model model, @RequestParam("file") MultipartFile foto) {

		producto.setComercio(comercioService.findById(comercioId));

		producto.setUnidad(productoService.findByNombreUnidad(producto.getUnidad().getNombre()));
		log.info("Entro en guarda de producto");
		if (result.hasErrors()) {
			log.error("Error en el campo");
			List<UnidadMedida> medidas = productoService.findAllMedida();
			model.addAttribute("medidas", medidas);
			return "comercio/form_producto";
		}

		if (!foto.isEmpty()) {
//			para borrar una foto
//			if (producto.getId() != null && producto.getId() > 0 && producto.getFotoUrl() != null
//					&& producto.getFotoUrl().length() > 0) {
//				uploadFileService.delete(producto.getFotoUrl());
//			}
			String uniqueFilename = null;
			try {
				uniqueFilename = uploadFileService.copy(foto, comercioId);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			producto.setFotoUrl(uniqueFilename);

		}

//		System.out.println(
//				"Entro en guardar producto".concat(producto.toString().concat(producto.getUnidad().getNombre())));
//		producto.getComercio().setId(comercioId);
		productoService.save(producto);

		// Importante decidir a que pagina pertenece este link
		return "redirect:/productos/form/" + producto.getComercio().getId();
	}

	@GetMapping(value = "/buscar-unidad/{term}", produces = { "application/json" })
	public @ResponseBody List<UnidadMedida> cargaUnidad(@PathVariable String term) {
		System.out.println("Entro aca");

		return productoService.findByNombreLikeIgnoreCase(term);
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
}
