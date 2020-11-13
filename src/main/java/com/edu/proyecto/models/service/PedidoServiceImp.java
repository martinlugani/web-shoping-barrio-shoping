package com.edu.proyecto.models.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edu.proyecto.models.dao.IClienteDao;
import com.edu.proyecto.models.dao.IPedidoDao;
import com.edu.proyecto.models.entity.Cliente;
import com.edu.proyecto.models.entity.Comercio;
import com.edu.proyecto.models.entity.Pedido;
@Service
public class PedidoServiceImp implements IPedidoService {
	@Autowired
	private IPedidoDao pedidoDao;
	@Autowired
	private IClienteDao clienteDao;
	@Override
	@Transactional
	public void save(Pedido pedido) {
		// TODO Auto-generated method stub
		pedidoDao.save(pedido);
	}

	@Override
	@Transactional(readOnly = true)
	public Pedido findAll() {
		// TODO Auto-generated method stub
		return (Pedido) pedidoDao.findAll();
	}
	@Transactional(readOnly = true)
	public List<Pedido> findByComercio(Comercio comercio){
		return pedidoDao.findAllByComercio(comercio);
	}

	@Override
	@Transactional(readOnly = true)
	public Pedido findById(Long id) {
		// TODO Auto-generated method stub
		return pedidoDao.findById(id).orElse(null);
	}

	

	@Override
	public Cliente findByIdCliente(Long id) {
		// TODO Auto-generated method stub
		return clienteDao.findById(id).orElse(null) ;
	}
}
