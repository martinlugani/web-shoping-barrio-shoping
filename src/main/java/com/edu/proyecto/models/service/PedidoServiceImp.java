package com.edu.proyecto.models.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edu.proyecto.models.dao.IPedidoDao;
import com.edu.proyecto.models.entity.Pedido;
@Service
public class PedidoServiceImp implements IPedidoService {
	@Autowired
	private IPedidoDao pedidoDao;
	@Override
	public void save(Pedido pedido) {
		// TODO Auto-generated method stub
		pedidoDao.save(pedido);
	}

	@Override
	public Pedido findAll() {
		// TODO Auto-generated method stub
		return (Pedido) pedidoDao.findAll();
	}

}
