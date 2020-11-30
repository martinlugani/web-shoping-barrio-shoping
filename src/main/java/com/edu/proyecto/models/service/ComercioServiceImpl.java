package com.edu.proyecto.models.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.edu.proyecto.models.dao.IComercioDao;
import com.edu.proyecto.models.entity.Cliente;
import com.edu.proyecto.models.entity.Comercio;
@Service
public class ComercioServiceImpl implements IComercioService {

	private final Logger log = LoggerFactory.getLogger(getClass());
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

	@Override
	public void save(Comercio comercio) {
		// TODO Auto-generated method stub
		comercioDao.save(comercio);
	}
	@Override
	public Comercio saveError(Comercio comercio) throws Exception {
		// TODO Auto-generated method stub

		if (checkUserAvaible(comercio) && checkPassword(comercio)) {
			BCryptPasswordEncoder pass = new BCryptPasswordEncoder();
			comercio.setPassword(pass.encode(comercio.getPassword()));
			log.info("La contraseña es " + comercio.getPassword());
			comercio = comercioDao.save(comercio);
		}
		return comercio;

	}
	private boolean checkPassword(Comercio comercio) throws Exception {
		// TODO Auto-generated method stub
		if (!comercio.getPassword().equals(comercio.getRepeatPass())) {
			throw new Exception("Contraseña y confirme contraseña diferente");
		}
		return true;
	}

	private boolean checkUserAvaible(Comercio comercio) throws Exception {
		// TODO Auto-generated method stub
		Comercio cleinteenco = comercioDao.findByUsername(comercio.getNicname());
		if (cleinteenco != null) {
			throw new Exception("Este nombre de usuario ya existe");
		}

		return true;
	}
	public Comercio findByImage(String image) {
		return comercioDao.findByImage(image);
	}
}
