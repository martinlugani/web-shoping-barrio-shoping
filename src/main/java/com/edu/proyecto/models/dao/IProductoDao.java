package com.edu.proyecto.models.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.edu.proyecto.models.entity.Producto;

public interface IProductoDao extends CrudRepository<Producto, Long> {
	public List<Producto> findAllByComercioIdOrderByNombre(Long idComercio);
}
