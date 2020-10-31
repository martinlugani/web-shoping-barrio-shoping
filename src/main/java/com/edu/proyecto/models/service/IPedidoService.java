package com.edu.proyecto.models.service;

import com.edu.proyecto.models.entity.Pedido;

public interface IPedidoService {
	public void save(Pedido pedido);
	public Pedido findAll();
}
