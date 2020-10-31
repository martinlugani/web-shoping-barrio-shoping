package com.edu.proyecto.models.service;

import com.edu.proyecto.models.entity.Cliente;

public interface IClienteService {
	public void save(Cliente cliente);
	public Cliente findByName(String name);
}
