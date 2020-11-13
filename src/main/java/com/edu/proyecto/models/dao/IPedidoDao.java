package com.edu.proyecto.models.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.edu.proyecto.models.entity.Comercio;
import com.edu.proyecto.models.entity.Pedido;

public interface IPedidoDao extends CrudRepository<Pedido, Long> {

	public List<Pedido> findAllByComercio(Comercio comercio);

	public Optional<Pedido> findById(Long id);

}
