package com.edu.proyecto.models.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edu.proyecto.models.dao.IClienteDao;
import com.edu.proyecto.models.entity.Cliente;

@Service
public class ClienteServiceImpl implements IClienteService{
	@Autowired
	private IClienteDao clienteDao;
	
	
	
//	public void save(Cliente cliente) {
//		// TODO Auto-generated method stub
//		
//	}

	@Override
	@Transactional
	public void save(Cliente cliente) {
		// TODO Auto-generated method stub
		
		clienteDao.save(cliente);
	}



	@Override
	public Cliente findByName(String name) {
		// TODO Auto-generated method stub
		return clienteDao.findByUsername(name);
	}
	
}
