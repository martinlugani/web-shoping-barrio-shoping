package com.edu.proyecto.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.edu.proyecto.models.entity.Usuario;

public interface IUsuarioDao extends CrudRepository<Usuario	, Long> {
	public Usuario findByUsername(String username);
}
