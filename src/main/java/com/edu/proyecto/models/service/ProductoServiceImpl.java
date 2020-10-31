package com.edu.proyecto.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edu.proyecto.models.dao.IProductoDao;
import com.edu.proyecto.models.dao.IUnidadMedidaDao;
import com.edu.proyecto.models.entity.Producto;
import com.edu.proyecto.models.entity.UnidadMedida;

@Service
public class ProductoServiceImpl implements IProductoServices {
	@Autowired
	private IProductoDao productoDao;
	@Autowired
	private IUnidadMedidaDao unidadDao;

	@Override
	@Transactional
	public void save(Producto producto) {
		// TODO Auto-generated method stub
		productoDao.save(producto);
	}

	@Override
	@Transactional(readOnly = true)
	public List<UnidadMedida> findByNombreLikeIgnoreCase(String term) {
		// TODO Auto-generated method stub
		return unidadDao.findByNombreLikeIgnoreCase("%" + term + "%");
	}

	@Override
	@Transactional(readOnly = true)
	public List<UnidadMedida> findAllUnidad() {
		// TODO Auto-generated method stub
		return (List<UnidadMedida>) unidadDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public UnidadMedida findByNombreUnidad(String nombre) {
		// TODO Auto-generated method stub
		return unidadDao.findByNombre(nombre);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Producto> findAll() {
		// TODO Auto-generated method stub
		return (List<Producto>) productoDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public List<UnidadMedida> findAllMedida() {
		// TODO Auto-generated method stub
		return (List<UnidadMedida>) unidadDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Producto> findAllByComercioIdOrderByNombre(Long idComercio) {
		// TODO Auto-generated method stub
		return productoDao.findAllByComercioIdOrderByNombre(idComercio);
	}

	@Override
	@Transactional(readOnly = true)
	public Producto findById(Long id) {
		// TODO Auto-generated method stub
		return productoDao.findById(id).orElse(null);
	}

	

}
