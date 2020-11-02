package com.edu.proyecto.models.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edu.proyecto.models.dao.IComercioDao;
import com.edu.proyecto.models.entity.Comercio;
@Service
public class ComercioServiceImpl implements IComercioService {

	
	@Autowired
	private IComercioDao comercioDao;
	
	@Override
	@Transactional(readOnly= true)
	public Comercio findById(Long id) {
		// TODO Auto-generated method stub
		return comercioDao.findById(id).orElse(null);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Comercio> findAll() {
		// TODO Auto-generated method stub
		return (List<Comercio>) comercioDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Comercio findByUsername(String username) {
		// TODO Auto-generated method stub
		return comercioDao.findByUsername(username);
	}

}
