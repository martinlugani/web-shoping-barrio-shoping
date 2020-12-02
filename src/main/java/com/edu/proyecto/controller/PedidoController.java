package com.edu.proyecto.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
import org.springframework.web.bind.annotation.PutMapping;
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
			SessionStatus status, Authentication auth) {
		pedido = new Pedido();
		Comercio comercio = comercioService.findById(idComercio);
		Cliente cliente = clienteService.findByName(auth.getName());
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
			HttpServletRequest request, Authentication auth, HttpSession session) {

		session.setAttribute("valor", false);
		List<Producto> productos = productoService.findAllByComercioIdOrderByNombre(comercioId);
		model.addAttribute("comercio", comercioId);
//		log.info("pedido dat" .concat(session.getAttribute("imagencli").toString()));
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

	@Secured("ROLE_COMERCIO")
	@GetMapping("/lista")
	public String listarPedidos(Model model, Authentication auth) {
		Comercio comercio = comercioService.findByUsername(auth.getName());

		List<Pedido> pedidos = pedidoService.findByComercio(comercio);

		log.info("comercio ".concat(comercio.toString()));
		for (Pedido pedido : pedidos) {
			log.info("pedido: ".concat(pedido.getComercio().getNicname()));
			log.info("Cliente".concat(pedido.getCliente().getNicname()));
		}

		model.addAttribute("pedidos", pedidos);

		return "comercio/lista-pedidos";
	}

	@Secured("ROLE_CLIENTE")
	@GetMapping("/lista/cliente")
	public String listarPedidosCliente(Model model, Authentication auth) {
		Cliente cliente = clienteService.findByName(auth.getName());

//		log.info("cliente ".concat(cliente.toString()));
		for (Pedido pedido : cliente.getPedidos()) {
			log.info("pedido: ".concat(pedido.getComercio().getNicname()));
			log.info("Cliente".concat(pedido.getCliente().getNicname()));
		}

		model.addAttribute("pedidos", cliente.getPedidos());

		return "comercio/lista-pedidos";
	}

	@Secured("ROLE_CLIENTE")
	@GetMapping("/cliente/ver/{id}")
	public String mostrarPedidoCliente(@PathVariable(value = "id") Long idPedido, @PathVariable(value = "id") Long id,
			Model model, Authentication auth) {
		Pedido pedido = pedidoService.findById(idPedido);

		model.addAttribute("pedido", pedido);

		for (ItemPedido item : pedido.getItems()) {
			log.info("item ".concat(item.getProducto().getNombre()));
		}
		List<String> estados = new ArrayList<String>();
		estados.add("Solicitado");
		estados.add("En preparación");
		estados.add("Terminado");
		estados.add("Entregado");
		model.addAttribute("estados", estados);
		model.addAttribute("url", "/pedido/modificar/cliente/");
		return "comercio/pedido-form";
	}

	@Secured("ROLE_COMERCIO")
	@GetMapping("/ver/{id}")
	public String mostrarPedido(@PathVariable(value = "id") Long idPedido, @PathVariable(value = "id") Long id,
			Model model, Authentication auth) {
		model = setModel(model, idPedido);
		return "comercio/pedido-form";
	}

	@Secured("ROLE_COMERCIO")
	@PostMapping("/modificar/{id}")
	public String modificarPedido(@PathVariable(value = "id") Long idPedido, @Valid Pedido ped) {
		Pedido pedido = pedidoService.findById(idPedido);

		pedido.setEstado(ped.getEstado());

		pedidoService.save(pedido);

		return "redirect:/pedido/ver/" + idPedido;
	}

	@Secured("ROLE_CLIENTE")
	@PostMapping("/modificar/cliente/{id}")
	public String modificarPedidoCliente(@PathVariable(value = "id") Long idPedido, @Valid Pedido ped, Model model) {
		Pedido pedido = pedidoService.findById(idPedido);
		if (pedido.getEntrega() == null) {
			pedido.setEntrega(new Date());

			pedido.setEstado("Entregado");
		} else {

			pedido.setEntrega(null);
			pedido.setEstado("Terminado");
		}
		pedidoService.save(pedido);
		model.addAttribute("success", "Su pedido se ha guardado");
		model = setModel(model, idPedido);
		return "comercio/pedido-form";
	}

	private Model setModel(Model model, Long idPedido) {
		Pedido pedido = pedidoService.findById(idPedido);

		model.addAttribute("pedido", pedido);

		for (ItemPedido item : pedido.getItems()) {
			log.info("item ".concat(item.getProducto().getNombre()));
		}
		List<String> estados = new ArrayList<String>();
		estados.add("Solicitado");
		estados.add("En preparación");
		estados.add("Terminado");

		model.addAttribute("estados", estados);
		model.addAttribute("url", "/pedido/modificar/");

		return model;

	}
}
