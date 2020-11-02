package com.edu.proyecto.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.edu.proyecto.models.entity.Cliente;
import com.edu.proyecto.models.entity.Comercio;
import com.edu.proyecto.models.entity.ItemPedido;
import com.edu.proyecto.models.entity.Pedido;
import com.edu.proyecto.models.entity.Producto;
import com.edu.proyecto.models.service.IClienteService;
import com.edu.proyecto.models.service.IComercioService;
import com.edu.proyecto.models.service.IPedidoService;
import com.edu.proyecto.models.service.IProductoServices;

@Controller
@RequestMapping("/pedido")
@SessionAttributes("pedido")
public class PedidoController {

	@Autowired
	private IProductoServices productoService;
	@Autowired
	private IComercioService comercioService;
	@Autowired
	private IClienteService clienteService;
	@Autowired
	private IPedidoService pedidoService;
	private static final Logger log = LoggerFactory.getLogger(PedidoController.class);

	@Secured("ROLE_CLIENTE")
	@PostMapping("/guardar")
	public String guardar(@Valid Pedido pedido, @RequestParam(name = "item_id[]", required = false) Long[] itemId,
			@RequestParam(name = "cantidad[]", required = false) Integer[] cantidad,
			@RequestParam(value = "comercio", required = false) Long idComercio, RedirectAttributes flash,
			SessionStatus status,Authentication auth) {
		pedido = new Pedido();
		Comercio comercio=comercioService.findById(idComercio);
		Cliente cliente =clienteService.findByName(auth.getName());
		pedido.setCliente(cliente);
		pedido.setComercio(comercio);
	
		for (int i = 0; i < itemId.length; i++) {
			Producto producto = productoService.findById(itemId[i]);

			ItemPedido linea = new ItemPedido();
			linea.setCantidad(cantidad[i]);
			linea.setProducto(producto);
			
			pedido.addItem(linea);
			
			log.info("ID: " + itemId[i].toString() + ", cantidad: " + cantidad[i].toString());
		}
		
		pedido.calculaTotal();
		
		log.info("metodo pedido/guardar  id comercio " + idComercio);
//		log.info("metodo pedido/guardar " + pedido.toString());
		
		status.setComplete();
		pedidoService.save(pedido);
		return "redirect:/";
	}

	@GetMapping("/{id}")
	public String obtenerProductoComercio(@PathVariable(value = "id") Long comercioId, Model model,
			HttpServletRequest request, Authentication auth) {
		List<Producto> productos = productoService.findAllByComercioIdOrderByNombre(comercioId);
		model.addAttribute("comercio", comercioId);
		if (request.isUserInRole("ROLE_CLIENTE")) {
			Pedido pedido = new Pedido();

			
			model.addAttribute("pedido", pedido);
		}
		Pedido pedido = new Pedido();

		model.addAttribute("pedido", pedido);

		System.out.println(productos);
		model.addAttribute("productos", productos);

		return "producto/listado-productos";
//		return "pruevas";
	}
}
