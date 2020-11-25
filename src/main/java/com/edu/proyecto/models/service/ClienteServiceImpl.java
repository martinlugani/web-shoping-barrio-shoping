package com.edu.proyecto.models.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edu.proyecto.models.dao.IClienteDao;
import com.edu.proyecto.models.entity.Cliente;

@Service
public class ClienteServiceImpl implements IClienteService {
	private final Logger log = LoggerFactory.getLogger(getClass());
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

	@Override
	public Cliente findById(Long id) {
		// TODO Auto-generated method stub
		return clienteDao.findById(id).orElse(null);
	}

	@Override
	public Cliente saveError(Cliente cliente) throws Exception {
		// TODO Auto-generated method stub

		if (checkUserAvaible(cliente) && checkPassword(cliente)) {
			BCryptPasswordEncoder pass = new BCryptPasswordEncoder();
			cliente.setPassword(pass.encode(cliente.getPassword()));
			log.info("La contraseña es " + cliente.getPassword());
			cliente = clienteDao.save(cliente);
		}
		return cliente;

	}

	private boolean checkPassword(Cliente cliente) throws Exception {
		// TODO Auto-generated method stub
		if (!cliente.getPassword().equals(cliente.getRepeatPass())) {
			throw new Exception("Contraseña y confirme contraseña diferente");
		}
		return true;
	}

	private boolean checkUserAvaible(Cliente cliente) throws Exception {
		// TODO Auto-generated method stub
		Cliente cleinteenco = clienteDao.findByUsername(cliente.getNicname());
		if (cleinteenco != null) {
			throw new Exception("Este nombre de usuario ya existe");
		}

		return true;
	}

}
