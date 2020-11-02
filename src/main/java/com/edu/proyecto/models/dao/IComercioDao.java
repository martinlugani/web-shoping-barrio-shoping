package com.edu.proyecto.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.edu.proyecto.models.entity.Comercio;

public interface IComercioDao extends CrudRepository<Comercio, Long> {
	public Comercio findByUsername(String username);
}
