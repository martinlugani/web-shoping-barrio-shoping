package com.edu.proyecto.models.service;

import java.util.List;

import com.edu.proyecto.models.entity.Producto;
import com.edu.proyecto.models.entity.UnidadMedida;

public interface IProductoServices {
	public void save(Producto producto);

	public List<UnidadMedida> findByNombreLikeIgnoreCase(String term);

	public List<UnidadMedida> findAllUnidad();

	public UnidadMedida findByNombreUnidad(String nombre);

	public List<Producto> findAll();

	public List<UnidadMedida> findAllMedida();

	public List<Producto> findAllByComercioIdOrderByNombre(Long idComercio);

	public Producto findById(Long id);
}
