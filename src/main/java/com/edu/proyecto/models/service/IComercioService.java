package com.edu.proyecto.models.service;

import java.util.List;

import com.edu.proyecto.models.entity.Comercio;

public interface IComercioService {
	public Comercio findById(Long id);
	public List<Comercio> findAll();
	public Comercio findByUsername(String username);
	public void save(Comercio comercioOriginal);
	Comercio saveError(Comercio comercio) throws Exception;
	public Comercio findByImage(String image);
}
