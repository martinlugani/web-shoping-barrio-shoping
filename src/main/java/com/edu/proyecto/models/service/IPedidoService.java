package com.edu.proyecto.models.service;

import java.util.List;

import com.edu.proyecto.models.entity.Cliente;
import com.edu.proyecto.models.entity.Comercio;
import com.edu.proyecto.models.entity.Pedido;

public interface IPedidoService {
	public void save(Pedido pedido);

	public Pedido findAll();

	public List<Pedido> findByComercio(Comercio comercio);

	public Pedido findById(Long id);
	public Cliente findByIdCliente(Long id);
}
