package com.edu.proyecto.models.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.edu.proyecto.models.entity.UnidadMedida;

public interface IUnidadMedidaDao extends CrudRepository<UnidadMedida, Long> {

	
	public List<UnidadMedida> findByNombreLikeIgnoreCase(String term);
	
	public UnidadMedida findByNombre(String nombre);
	
}
